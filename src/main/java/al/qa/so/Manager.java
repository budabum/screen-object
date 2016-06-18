package al.qa.so;

import al.qa.so.anno.ScreenParams;
import al.qa.so.exc.SOException;
import al.qa.so.selenide.AllByResolver;
import al.qa.so.selenide.ByResolver;
import al.qa.so.utils.StepRecorder;
import al.qa.so.utils.Utils;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.ElementsContainer;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;

import static al.qa.so.coverage.Model.COVERAGE;

/**
 * @author Alexey Lyanguzov.
 */
class Manager {
    private static final Logger LOG = Utils.getLogger();

    private static final List<Class<? extends BaseScreen>> screens = new ArrayList<>();
    private static BaseScreen currentScreen = DefaultScreen.INSTANCE;
    private static final StepRecorder stepRecorder = new StepRecorder();

    private static final Map<SelenideElement, String> fieldNames = new HashMap<>();

    //TODO: should not it be in SO?
    static <T extends BaseScreen> Class<T> register(Class<T> screenClass){
        checkScreen(screenClass);
        screens.add(screenClass);
        COVERAGE.addScreen(screenClass);
        return screenClass;
    }

    static String getFieldName(SelenideElement key){
        String name = fieldNames.get(key);
        LOG.trace("Found field name: {} for key {}", name, key.hashCode());
        return name;
    }

    @SuppressWarnings("unchecked")
    static <T extends BaseScreen> T getCurrentScreen(){
        return (T)currentScreen;
    }

    static StepRecorder getStepRecorder(){
        return stepRecorder;
    }

    private static <T extends BaseScreen> T setCurrentScreen(T newCurrentScreen){
        BaseScreen oldCurrentScreen = currentScreen;
        currentScreen = newCurrentScreen;
        LOG.info("Setting current screen => {}", currentScreen.name());
        setFieldNames();
        if(!oldCurrentScreen.name().equals(newCurrentScreen.name())){
            stepRecorder.onScreen("On screen %s", currentScreen.name());
        }
        return getCurrentScreen();
    }

    @SuppressWarnings("unchecked")
    static <T extends BaseScreen> T onScreen(Class<T> targetScreenClass, boolean forceNavigation){
        T targetScreen;
        String targetScreenName = getName(targetScreenClass);
        LOG.info("Navigating from {} to {}", getCurrentScreen().name(), targetScreenName);
        if(isOnScreen(targetScreenName)){
            LOG.debug("Screen {} is already opened", targetScreenName);
            return (T)currentScreen;
        }
        else if(forceNavigation){
            targetScreen = open(targetScreenClass);
        }
        else{
            String msg = targetScreenName.equals(currentScreen.name()) ?
                String.format("Target screen %s not actually opened", targetScreenName) :
                String.format("Expected to be on %s but actually on %s", targetScreenName, currentScreen.name());
            throw new SOException(msg);
        }
        return setCurrentScreen(targetScreen);
    }

    @SuppressWarnings("unchecked")
    static <T, R extends BaseScreen> R perform(Consumer<T> proc, T argument) {
        String methodName = getMethodName();
        Method method = getMethod(methodName);
        ActionType actionType = method.getAnnotation(ActionType.class);
        if(actionType == null){
            throw new SOException("Trying to perform action %s on screen %s which misses annotation @%s",
                methodName, currentScreen.name(), ActionType.class.getSimpleName());
        }
        ActAs actAs = actionType.value();
        switch(actAs){
            case Action:
                return doAction(methodName, proc, argument);
            case Check:
                return doCheck(methodName, proc, argument);
            case Transition:
                return doTransition(methodName, proc, argument);
        }
        throw new SOException("Unexpected call of perform for %s", methodName);
    }

    @SuppressWarnings("unchecked")
    private static <T, R extends BaseScreen> R doAction(String actionName, Consumer<T> proc, T argument) {
        stepRecorder.actionCall("Do action %s(%s)", actionName, currentScreen.name());
        proc.accept(argument);
        return getCurrentScreen();
    }

    @SuppressWarnings("unchecked")
    private static <T, R extends BaseScreen> R doCheck(String checkName, Consumer<T> proc, T argument) {
        stepRecorder.performCheck("Check %s", checkName);
        proc.accept(argument);
        return getCurrentScreen();
    }

    @SuppressWarnings("unchecked")
    private static <T, R extends BaseScreen> R doTransition(String transitionName, Consumer<T> proc, T argument) {
        R targetScreen = getTargetScreen(transitionName);
        stepRecorder.actionCall("Do transition '%s' from %s", transitionName, currentScreen.name());
        proc.accept(argument);
        stepRecorder.actionCall(" ... expecting to be on %s", targetScreen.name());
        if(!targetScreen.isOpened()){
            throw new SOException("Screen %s is not opened. Current screen is %s",
                    targetScreen.name(), currentScreen.name());
        }
        LOG.trace("Transition is done");
        return setCurrentScreen(targetScreen);
    }

    private static void setFieldNames() {
        Arrays.stream(getCurrentScreen().getClass().getDeclaredFields()).forEach(field->{
            if(field.getType().isAssignableFrom(SelenideElement.class)){
                field.setAccessible(true);
                try {
                    SelenideElement key = (SelenideElement)field.get(getCurrentScreen());
                    fieldNames.put(key, field.getName());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static <T extends BaseScreen> String getName(Class<T> screenClass){
        String name = screenClass.getSimpleName();
        if(!screens.contains(screenClass)){
            throw new SOException("Screen "+ name +" is not registered");
        }
        return name;
    }

    private static <T extends BaseScreen> void checkScreen(Class<T> screen){
        String className = screen.getSimpleName();
        LOG.trace("Checking screen class: {}", className);
        ScreenParams[] urls = screen.getAnnotationsByType(ScreenParams.class);
        if(urls == null || urls.length == 0) {
            throw new SOException("Class "+ className +" has no url! Set annotation @ScreenParams.");
        }
    }

    private static Method getMethod(String methodName){
        LOG.trace("Looking for method {}", methodName);
        Method meth = findMethod(methodName, currentScreen);
        if(meth == null){
            Iterator it = currentScreen.getContainers().iterator();
            while(meth == null && it.hasNext()){
                ElementsContainer container = (ElementsContainer)it.next();
                meth = findMethod(methodName, container);
            }
        }
        if(meth == null){
            throw new SOException("Unable to find method {} on screen {} and its parts", methodName, currentScreen.name());
        }
        LOG.trace("Found method {}", meth);
        return meth;
    }

    private static Method findMethod(String methodName, Object obj){
        Method[] methods = obj.getClass().getMethods();
        for(Method m : methods){
            if(m.getName().equals(methodName)){
                return m;
            }
        }
        return null;
    }

    private static String getMethodName() {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[5];
        return stackTraceElement.getMethodName();
    }

    @SuppressWarnings("unchecked")
    private static <T extends BaseScreen> T getTargetScreen(String methodName){
        Method theMethod = getMethod(methodName);
        return instantiateScreen((Class<T>)theMethod.getReturnType());
    }

    private static boolean isOnScreen(String targetScreenName){
        LOG.debug("Checking if already on screen {}", targetScreenName);
        return currentScreen.name().equals(targetScreenName) && currentScreen.isOpened(false);
    }

    private static <T extends BaseScreen> T open(Class<T> screenClass) {
        String screenName = screenClass.getSimpleName();
        LOG.debug("Opening screen {}", screenName);
        T screenInstance = instantiateScreen(screenClass);
        screenInstance._open();
        if(!screenInstance.isOpened()){
            throw new SOException("Screen " + screenName + " is not opened!");
        }
        return screenInstance;
    }

    private static <T extends BaseScreen> T instantiateScreen(Class<T> screenClass) {
        try{
            return initElements(screenClass.newInstance());
        } catch (InstantiationException | IllegalAccessException exc) {
            throw new SOException(exc);
        }
    }

    private static <T extends ScreenPart> T initElements(T screenPart){
        return initElements(screenPart, (BaseScreen)screenPart);
    }

    private static <T extends ScreenPart> T initElements(T screenPart, BaseScreen parentScreen){
        String screenPartTypeName = (BaseScreen.class.isAssignableFrom(screenPart.getClass())) ? "screen" : "module";
        LOG.trace("Will instantiate {} {}", screenPartTypeName, screenPart.name());
        Arrays.stream(screenPart.getClass().getDeclaredFields()).forEach(f -> {
            f.setAccessible(true);
            String fieldName = f.getName();
            FindBy findBy = f.getAnnotation(FindBy.class); //TODO: cover FindAll etc
            if(findBy == null){
                LOG.trace("Skipping field {} due to it does not have @FindBy", fieldName);
            }
            else{
                LOG.trace("Instantiating field {}", fieldName);
                Class<?> type = f.getType();
                try {
                    if(WebElement.class.isAssignableFrom(type)){
                        SelenideElement selenideElement = ByResolver.INSTANCE.resolve(f);
                        f.set(screenPart, selenideElement);
                        LOG.trace("... OK: SelenideElement: {}", fieldName);
                    }
                    else if(type.isAssignableFrom(ElementsCollection.class)){
                        ElementsCollection elementsCollection = AllByResolver.INSTANCE.resolve(f);
                        f.set(screenPart, elementsCollection);
                        LOG.trace("... OK: ElementsCollection: {}", fieldName);
                    }
                    else if(ElementsContainer.class.isAssignableFrom(type)){
                        ElementsContainer container = (ElementsContainer) f.getType().newInstance();
                        container.setSelf(ByResolver.INSTANCE.resolve(f));
                        f.set(screenPart, container);
                        initElements((ScreenPart) container, parentScreen);
                        parentScreen.addContainer(container);
                        LOG.trace("... OK: ElementsContainer: {}", fieldName);
                    }
                    else {
                        throw new SOException("Unable to instantiate field with type: %s", type);
                    }
                } catch (IllegalAccessException | InstantiationException e) {
                    throw new SOException(e);
                }
            }
        });
        return screenPart;
    }
}
