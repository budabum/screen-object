package al.qa.so;

import al.qa.so.anno.ScreenParams;
import al.qa.so.anno.Trait;
import al.qa.so.exc.ScreenObjectException;
import al.qa.so.selenium.ByResolver;
import al.qa.so.utils.Utils;
import al.qa.so.utils.url.UriComparator;
import al.qa.so.utils.url.UrlComparisonStrategy;
import com.codeborne.selenide.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.url;

/**
 * @author Alexey Lyanguzov.
 */
public abstract class BaseScreen {
    protected final ByResolver by = ByResolver.INSTANCE;

    private static Logger LOG = LoggerFactory.getLogger(BaseScreen.class);

    private final List<URI> uris = new ArrayList<>();
    private final Class<? extends UrlComparisonStrategy> urlComparisonStrategy;
    private final List<Field> traits = new ArrayList<>();

    public BaseScreen() {
        ScreenParams params = this.getClass().getAnnotation(ScreenParams.class);
        this.urlComparisonStrategy = params.urlComparisonStrategy();
        initWith(params);
    }

    public <C> C check(Class<C> checkerClass, Consumer<C> proc){
        C checker = null;
        try {
            checker = checkerClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ScreenObjectException(e);
        }
        proc.accept(checker);
        return checker;
    }

    protected String name(){
        return this.getClass().getSimpleName();
    }

    protected boolean isOpened(){
        return isOpened(true);
    }

    protected boolean isOpened(boolean waitForProgress){
        return waitForNoProgressIndicator(waitForProgress) &&
                waitForTraits() &&
                isUrlCorrect();
    }

    protected <T, R extends BaseScreen> R action(Consumer<T> proc){
        return action(proc, null);
    }

    @SuppressWarnings("unchecked")
    protected <T, R extends BaseScreen> R action(Consumer<T> proc, T argument){
        return Manager.doAction(proc, argument);
    }

    protected <T, R extends BaseScreen> R transition(Consumer<T> proc){
        return transition(proc, null);
    }

    @SuppressWarnings("unchecked")
    protected <T, R extends BaseScreen> R transition(Consumer<T> proc, T argument){
        return Manager.doTransition(proc, argument);
    }

    protected <T, R extends BaseScreen> R check(Consumer<T> proc){
        return check(proc, null);
    }

    @SuppressWarnings("unchecked")
    protected <T, R extends BaseScreen> R check(Consumer<T> proc, T argument){
        return Manager.doCheck(proc, argument);
    }

    void _open(){
        directOpen();
    }

    private void directOpen(){
        String url = mainUrl().toString();
        LOG.debug("Opening url: {}", url);
        open(url);
    }

    private void initUrls(String[] urls){
        uris.addAll(Arrays.stream(urls)
                .map(e -> {
                    String strUrl = Configuration.baseUrl + e;
                    LOG.trace("Adding url {} to screen {}", strUrl, name());
                    return Utils.buildUri(strUrl);
                })
                .collect(Collectors.toList()));
    }

    private void initTraits(){
        for(Field fld : this.getClass().getDeclaredFields()){
            if(fld.isAnnotationPresent(Trait.class)){
                this.traits.add(fld);
           }
        }
    }

    private void initWith(ScreenParams params){
        initUrls(params.urls());
        initTraits();
    }

    private boolean waitForNoProgressIndicator(boolean doWait){
        if(!doWait) return true;
        return true;
    }

    private boolean waitForTraits(){
        if(traits.isEmpty()){
            throw new ScreenObjectException("Screen " + name() + " does not have trait elements");
        }
        for(Field traitField : traits){
            SelenideElement trait = null;
            try {
                traitField.setAccessible(true);
                trait = (SelenideElement) traitField.get(this);
            } catch (IllegalAccessException e) {
                throw new ScreenObjectException(e);
            }
            trait.shouldBe(visible);
        }
        return true;
    }

    private URI mainUrl(){
        if(uris.isEmpty() || uris.get(0) == null){
            throw new ScreenObjectException("Url is not set for the screen %s", name());
        }
        return uris.get(0);
    }

    private boolean isUrlCorrect(){
        URI curUrl = Utils.buildUri(url());
        for(URI url : uris){
            LOG.debug("Comparing uris: {} and {}", curUrl, url);
            boolean res = UriComparator.areEquals(curUrl, url, urlComparisonStrategy);
            LOG.trace("Url comparison result: {}", res);
            if(res) return true;
        }
        return false;
    }

}
