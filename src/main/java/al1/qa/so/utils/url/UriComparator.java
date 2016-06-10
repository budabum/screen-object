package al1.qa.so.utils.url;

import al1.qa.so.exc.ScreenObjectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.Arrays;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author Alexey Lyanguzov.
 */
public class UriComparator {
    public static boolean areEquals(URI a, URI b){
        return areEquals(a, b, CompareAll.class);
    }

    private static Logger LOG = LoggerFactory.getLogger(UriComparator.class);

    public static boolean areEquals(URI a, URI b, Class<? extends UrlComparisonStrategy> strategy){
        return areEquals(a, b, getUrlPartsFor(strategy));
    }

    public static boolean areEquals(URI a, URI b, UriPart...parts){
        Map<UriPart, String> splitA = splitUrl(a);
        Map<UriPart, String> splitB = splitUrl(b);
        LOG.trace("Will compare URI parts: {}", Arrays.asList(parts));
        boolean res = true;
        for(UriPart p : parts){
            String valA = splitA.get(p);
            String valB = splitB.get(p);
            res = res && valA.equals(valB);
        }
        return res;
    }

    public static UriPart[] getUrlPartsFor(Class<? extends UrlComparisonStrategy> strategy){
        return Arrays.stream(UriPart.values()).filter(p -> !p.isExcludedWith(strategy)).toArray(UriPart[]::new);
    }

    private static Map<UriPart, String> splitUrl(URI uri){
        SortedMap<UriPart, String> urlParts = new TreeMap<>();
        Arrays.stream(UriPart.values()).forEach(p -> {
            String methodName = "get"  + p.name();
            String partValue = null;
            try {
                partValue = String.valueOf(uri.getClass().getMethod(methodName).invoke(uri));
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                throw new ScreenObjectException(e);
            }
            urlParts.put(p, partValue);
        });
        return urlParts;
    }

    public interface CompareAll extends UrlComparisonStrategy{}
    public interface CompareWithoutQuery extends UrlComparisonStrategy{}
    public interface CompareWithoutFragment extends UrlComparisonStrategy{}
    public interface CompareWithPathOnly extends UrlComparisonStrategy{}
}

