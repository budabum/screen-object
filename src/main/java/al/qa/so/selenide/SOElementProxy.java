package al.qa.so.selenide;

import al.qa.so.SO;
import al.qa.so.utils.Utils;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.slf4j.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.$;

/**
 * @author Alexey Lyanguzov.
 */
public class SOElementProxy implements InvocationHandler {
    private static final Logger LOG = Utils.getLogger();
    private static final List<String> WEBDRIVER_INTERACTION = Stream.of(
        "click", "setValue", "val", "followLink"
    ).collect(Collectors.toList());

    private final By by;
    private SelenideElement realSelenideElement;

    public SOElementProxy(By by) {
        this.by = by;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(realSelenideElement == null) init();
//        System.out.println("Before calling method " + method.getName());
        doBeforeCall(method, args);
        Object res = method.invoke(realSelenideElement, args);
//        System.out.println("After calling method " + method.getName());
        return res;
    }

    private void init(){
        this.realSelenideElement = $(by);
    }

    private void reportWebDriverInteraction(Method method, Object[] args) {
        if(WEBDRIVER_INTERACTION.contains(method.getName())){
            String strArgs = (args == null || args.length == 0) ? "" :
                String.join(", ", Arrays.stream(args).map(Object::toString).collect(Collectors.toList()));
            LOG.debug("WebDriver Interaction: {}({}) on {} on {}",
                method.getName(), strArgs, SO.fieldName(realSelenideElement), SO.currentScreen().name());
        }
    }

    private void doBeforeCall(Method method, Object[] args) {
        reportWebDriverInteraction(method, args);
    }

}
