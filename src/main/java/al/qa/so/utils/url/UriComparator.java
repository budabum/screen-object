package al.qa.so.utils.url;

import al.qa.so.exc.SOException;
import al.qa.so.utils.Utils;
import org.slf4j.Logger;

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
    private static final Logger LOG = Utils.getLogger();

    public static boolean areEquals(URI a, URI b, Class<? extends UrlComparisonStrategy> strategy){
        return areEquals(a, b, getUrlPartsFor(strategy));
    }

    @SuppressWarnings("all")
    public static boolean areEquals(URI a, URI b, UriPart...parts){
        Map<UriPart, String> splitA = splitUrl(a);
        Map<UriPart, String> splitB = splitUrl(b);
        LOG.trace("Will compare URI parts: {}", Arrays.asList(parts));
        boolean res = true;
        for(UriPart p : parts){
            String valA = splitA.get(p);
            String valB = splitB.get(p);
            res = res && valA.equals(valB);
            LOG.trace("Comparing '{}' ({} == {}) = {}", p, valA, valB, res);
        }
        return res;
    }

    private static UriPart[] getUrlPartsFor(Class<? extends UrlComparisonStrategy> strategy){
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
                throw new SOException(e);
            }
            urlParts.put(p, partValue);
        });
        return urlParts;
    }

    public interface CompareAll extends UrlComparisonStrategy{}
    public interface CompareWithoutQuery extends UrlComparisonStrategy{}
    @SuppressWarnings("all")
    public interface CompareWithoutFragment extends UrlComparisonStrategy{}
    @SuppressWarnings("all")
    public interface CompareWithPathOnly extends UrlComparisonStrategy{}
}

