package al.qa.so.anno;

import al.qa.so.utils.url.UriComparator;
import al.qa.so.utils.url.UrlComparisonStrategy;

import java.lang.annotation.*;

/**
 * @author Alexey Lyanguzov.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ScreenParams {
    String[] urls();
    Class<? extends UrlComparisonStrategy> urlComparisonStrategy() default UriComparator.CompareAll.class;
}
