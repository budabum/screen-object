package al.qa.so.selenide;

import al.qa.so.SO;
import al.qa.so.exc.SOException;
import al.qa.so.utils.Utils;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.slf4j.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.$;

/**
 * @author Alexey Lyanguzov.
 */
class SOElementProxy implements InvocationHandler {
    private static final Logger LOG = Utils.getLogger();
    private static final List<String> WEBDRIVER_INTERACTION = Stream.of(
        "click", "setValue", "val", "followLink"
    ).collect(Collectors.toList());

    private static final Map<String, String> HUMANIZE = new HashMap<String, String>(){{
        put("click", "Click%s on");
        put("setValue", "Set value %s in");
    }};

    private final By by; // for SelenideElement
    private final SelenideElement origElement; //for ElementsCollection
    private SelenideElement realSelenideElement;
    private boolean isElementcollectionProxy = false;

    SOElementProxy(By by) {
        this(by, null);
    }

    SOElementProxy(SelenideElement element) {
        this(null, element);
        isElementcollectionProxy = true;
    }

    private SOElementProxy(By by, SelenideElement origElement) {
        this.by = by;
        this.origElement = origElement;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        if(method.getName().equals("hashCode")){
            return this.hashCode();
        }
        init();
        doBeforeCall(method, args);
        Object res = invokeMethod(method, realSelenideElement, args);
        doAfterCall(method, args);
        return res;
    }

    private Object invokeMethod(Method method, Object target, Object[] args){
        try {
            if(SO.CONFIG.dryRun) { return null; }
            return method.invoke(target, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new SOException(e);
        }
    }

    private String humanizeInteraction(String methodName, String strArgs){
        if(HUMANIZE.keySet().contains(methodName)){
            return String.format(HUMANIZE.get(methodName), strArgs);
        }
        return methodName + strArgs;
    }

    private String humanizeShould(String methodName, String strArgs){
        String[] shouldParts = methodName.replaceAll("([a-z])([A-Z])", "$1 $2").split(" ");
        String shouldName = String.join(" ", Arrays.stream(shouldParts).map(String::toLowerCase).collect(Collectors.toList()));
        return shouldName + " " + strArgs;
    }

    private void init(){
        if(realSelenideElement == null){
            if(isElementcollectionProxy){
                realSelenideElement = origElement;
            }
            else{
                realSelenideElement = $(by);
            }
        }
    }

    private void reportWebDriverInteraction(Method method, Object[] args) {
        String fieldName = SO.fieldName(hashCode());
        if(isWebdriveInteraction(method)){
            String strArgs = (args == null || args.length == 0) ? "" :
                "('" + String.join(", ", Arrays.stream(args).map(Object::toString).collect(Collectors.toList()))+"')";
            LOG.trace("WebDriver Interaction: {}({}) on {} on {}",
                method.getName(), strArgs, fieldName, SO.currentScreen().name());
            SO.getStepRecorder().driverInteraction(humanizeInteraction(method.getName(), strArgs), fieldName);
        }
        if(isShould(method)){
            Condition[] conditions;
            conditions = (args == null || args.length == 0) ? new Condition[]{} : (Condition[])args[0];
            String strArgs = String.join(", ", Arrays.stream(conditions).map(Object::toString).collect(Collectors.toList()));
//            String fieldName = SO.fieldName(hashCode());
            LOG.trace("{} {}", fieldName, humanizeShould(method.getName(), strArgs));
            SO.getStepRecorder().driverInteractionCheck(fieldName, humanizeShould(method.getName(), strArgs));
        }
    }

    private boolean isWebdriveInteraction(Method method){
        return WEBDRIVER_INTERACTION.contains(method.getName());
    }

    private boolean isShould(Method method){
        return method.getName().startsWith("should");
    }

    private void doBeforeCall(Method method, Object[] args) {
        reportWebDriverInteraction(method, args);
    }

    private void doAfterCall(Method method, Object[] args) {
    }

}
