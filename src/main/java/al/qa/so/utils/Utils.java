package al.qa.so.utils;

import al.qa.so.exc.ScreenObjectException;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Alexey Lyanguzov.
 */
public class Utils {

    public static URI buildUri(String strUri){
        try {
            return new URI(strUri);
        } catch (URISyntaxException e) {
            throw new ScreenObjectException(e);
        }
    }

}
