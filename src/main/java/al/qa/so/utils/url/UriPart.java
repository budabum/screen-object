package al.qa.so.utils.url;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Alexey Lyanguzov.
 */
public enum UriPart {
    Scheme,
    Host,
    Port,
    Path,
    Fragment(
            UriComparator.CompareWithoutFragment.class,
            UriComparator.CompareWithPathOnly.class),
    Query(
            UriComparator.CompareWithoutQuery.class,
            UriComparator.CompareWithPathOnly.class);

    private List<Class<? extends UrlComparisonStrategy>> excludeStrategies = new ArrayList<>();

    @SafeVarargs
    UriPart(Class<? extends UrlComparisonStrategy>...excludeWithStrategies) {
        this.excludeStrategies.addAll(Arrays.asList(excludeWithStrategies));
    }

    public boolean isExcludedWith(Class<? extends UrlComparisonStrategy> strategy){
        return excludeStrategies.contains(strategy);
    }
}
