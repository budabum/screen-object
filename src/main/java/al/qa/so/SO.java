package al.qa.so;

import al.qa.so.exc.TestExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * @author Alexey Lyanguzov.
 */
public class SO {
    private static Logger LOG = LoggerFactory.getLogger(SO.class);

    @SafeVarargs
    public static void addScreens(Class<? extends BaseScreen>...screenClasses){
        Arrays.stream(screenClasses).forEach(Manager::register);
    }

    public static <T extends BaseScreen> T navigateTo(Class<T> screenClass)  {
        return Manager.onScreen(screenClass, true);
    }

    public static <T extends BaseScreen> T onScreen(Class<T> screenClass)  {
        return Manager.onScreen(screenClass, false);
    }

    public static void failTest(String msg, Object...args){
        String message = (args.length == 0) ? msg : String.format(msg, args);
        throw new TestExecutionException(message);
    }

}
