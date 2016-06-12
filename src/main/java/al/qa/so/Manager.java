package al.qa.so;

import al.qa.so.anno.ScreenParams;
import al.qa.so.exc.ScreenObjectException;
import al.qa.so.utils.StepRecorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Alexey Lyanguzov.
 */
class Manager {
    private static Logger LOG = LoggerFactory.getLogger(Manager.class);

    private static final List<Class<? extends BaseScreen>> screens = new ArrayList<>();
    private static BaseScreen currentScreen = DefaultScreen.INSTANCE;

    //TODO: should not it be in SO?
    static <T extends BaseScreen> Class<T> register(Class<T> screenClass){
        checkScreen(screenClass);
        screens.add(screenClass);
        return screenClass;
    }

    @SuppressWarnings("unchecked")
    static <T extends BaseScreen> T getCurrentScreen(){
        return (T)currentScreen;
    }

    static <T extends BaseScreen> T setCurrentScreen(T newCurrentScreen){
        currentScreen = newCurrentScreen;
        LOG.trace(Arrays.asList(currentScreen.getClass().getDeclaredFields()).toString());
        return getCurrentScreen();
    }

    @SuppressWarnings("unchecked")
    static <T extends BaseScreen> T onScreen(Class<T> targetScreenClass, boolean forceNavigation){
        T targetScreen = null;
        String targetScreenName = getName(targetScreenClass);
        LOG.info("Navigating from {} to {}", getCurrentScreen().name(), targetScreenName);
        StepRecorder.onScreen(targetScreenName);
        if(isOnScreen(targetScreenName)){
            LOG.debug("Screen {} is already opened", targetScreenName);
            return (T)currentScreen;
        }
        else if(forceNavigation){
            targetScreen = open(targetScreenClass);
        }
        else{
            throw new ScreenObjectException(
                    "Expected to be on "+ targetScreenName +" but actually on " + currentScreen.name());
        }
        return setCurrentScreen(targetScreen);
    }

    @SuppressWarnings("unchecked")
    static <T, R extends BaseScreen> R doAction(Consumer<T> proc, T argument) {
        String actionName = getMethodName();
        LOG.info("Doing action {} on {}", actionName, currentScreen.name());
//        checkMember(actionName, Action.class);
        proc.accept(argument);
        return getCurrentScreen();
    }

    @SuppressWarnings("unchecked")
    static <T, R extends BaseScreen> R doCheck(Consumer<T> proc, T argument) {
        String checkName = getMethodName();
        LOG.info("Doing check {} on {}", checkName, currentScreen.name());
//        checkMember(actionName, Action.class);
        proc.accept(argument);
        return getCurrentScreen();
    }

    @SuppressWarnings("unchecked")
    static <T, R extends BaseScreen> R doTransition(Consumer<T> proc, T argument) {
        String transitionName = getMethodName();
        R targetScreen = (R) getTargetScreen(transitionName);
        LOG.info("Doing transition '{}' from {} to {}", transitionName, currentScreen.name(), targetScreen.name());
//        checkMember(transitionName, Transition.class);
        proc.accept(argument);
        if(!targetScreen.isOpened()){
            throw new ScreenObjectException("Screen %s is not opened. Current screen is %s",
                    targetScreen.name(), currentScreen.name());
        }
        LOG.trace("Transition is done");
        return setCurrentScreen(targetScreen);
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

//    private static void checkMember(String methodName, Class annotationClass){
//        Method method = getMethod(methodName);
//        if(method.getAnnotation(annotationClass) == null){
//            throw new ScreenObjectException("Method '"+ methodName +"' of class '"+ getCurrentScreen().name() +
//                    "' must be annotated as @"+annotationClass.getSimpleName());
//        }
//    }

    private static Method getMethod(String methodName){
        Method[] methods = getCurrentScreen().getClass().getMethods();
//        Method meth = Arrays.stream(methods).filter(m -> m.getName().equals(methodName)).findFirst().orElseGet(null);
        Method meth = Arrays.stream(methods).filter(m -> m.getName().equals(methodName)).findFirst().orElseGet(null);
        LOG.trace("Found method {}", meth);
        return meth;
    }

    private static String getMethodName() {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[5];
        return stackTraceElement.getMethodName();
    }

    private static BaseScreen getTargetScreen(String methodName){
        try {
            Method theMethod = getMethod(methodName);
            return (BaseScreen)theMethod.getReturnType().newInstance();
        }
        catch (InstantiationException | IllegalAccessException exc) {
            throw new ScreenObjectException(exc);
        }
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
        return setCurrentScreen(screenInstance);
    }

    private static <T extends BaseScreen> T instantiateScreen(Class<T> screenClass) {
        try{
            return screenClass.newInstance();
        } catch (InstantiationException | IllegalAccessException exc) {
            throw new ScreenObjectException(exc);
        }
    }
}
