package al.qa.so.utils;

import al.qa.so.exc.SOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Alexey Lyanguzov.
 */
public class Utils {

    public static Logger getLogger(){
        return getLogger(3);
    }

    public static Logger getLogger(int backtrace){
        String loggerName = Thread.currentThread().getStackTrace()[backtrace].getClassName();
        return LoggerFactory.getLogger(loggerName);
    }

    public static URI buildUri(String strUri){
        try {
            return new URI(strUri);
        } catch (URISyntaxException e) {
            throw new SOException(e);
        }
    }

    public static Object getStaticFieldValue(Field field){
        try {
            return field.get(null);
        } catch (IllegalAccessException e) {
            throw new SOException(e);
        }
    }

}
