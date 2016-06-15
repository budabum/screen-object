package al.qa.so;

import al.qa.so.anno.ScreenParams;
import al.qa.so.exc.ScreenObjectException;
import al.qa.so.utils.StepRecorder;
import al.qa.so.utils.Utils;
import com.codeborne.selenide.SelenideElement;
import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;

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
        currentScreen = newCurrentScreen;
        LOG.info("Setting current screen => {}", currentScreen.name());
        setFieldNames();
        stepRecorder.onScreen("On screen %s", currentScreen.name());
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
            throw new ScreenObjectException(msg);
        }
        return setCurrentScreen(targetScreen);
    }

    @SuppressWarnings("unchecked")
    static <T, R extends BaseScreen> R doAction(Consumer<T> proc, T argument) {
        String actionName = getMethodName();
        stepRecorder.actionCall("Do action %s(%s)", actionName, currentScreen.name());
        proc.accept(argument);
        return getCurrentScreen();
    }

    @SuppressWarnings("unchecked")
    static <T, R extends BaseScreen> R doCheck(Consumer<T> proc, T argument) {
        String checkName = getMethodName();
        stepRecorder.performCheck("Check %s", checkName);
        proc.accept(argument);
        return getCurrentScreen();
    }

    @SuppressWarnings("unchecked")
    static <T, R extends BaseScreen> R doTransition(Consumer<T> proc, T argument) {
        String transitionName = getMethodName();
        R targetScreen = getTargetScreen(transitionName);
        stepRecorder.actionCall("Doing transition '%s' from %s", transitionName, currentScreen.name());
        proc.accept(argument);
        stepRecorder.actionCall(" ... expecting to be on %s", targetScreen.name());
        if(!targetScreen.isOpened()){
            throw new ScreenObjectException("Screen %s is not opened. Current screen is %s",
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
            throw new ScreenObjectException("Screen "+ name +" is not registered");
        }
        return name;
    }

    private static <T extends BaseScreen> void checkScreen(Class<T> screen){
        String className = screen.getSimpleName();
        LOG.trace("Checking screen class: {}", className);
        ScreenParams[] urls = screen.getAnnotationsByType(ScreenParams.class);
        if(urls == null || urls.length == 0) {
            throw new ScreenObjectException("Class "+ className +" has no url! Set annotation @ScreenParams.");
        }
    }

    private static Method getMethod(String methodName){
        Method[] methods = getCurrentScreen().getClass().getMethods();
        Method meth = Arrays.stream(methods).filter(m -> m.getName().equals(methodName)).findFirst().orElseGet(null);
        LOG.trace("Found method {}", meth);
        return meth;
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
            throw new ScreenObjectException("Screen " + screenName + " is not opened!");
        }
        return screenInstance;
    }

    private static <T extends BaseScreen> T instantiateScreen(Class<T> screenClass) {
        try{
            return screenClass.newInstance();
        } catch (InstantiationException | IllegalAccessException exc) {
            throw new ScreenObjectException(exc);
        }
    }
}
