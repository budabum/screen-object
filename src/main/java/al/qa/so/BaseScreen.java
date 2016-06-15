package al.qa.so;

import al.qa.so.anno.ScreenParams;
import al.qa.so.anno.Trait;
import al.qa.so.exc.ScreenObjectException;
import al.qa.so.selenide.AllByResolver;
import al.qa.so.selenide.ByResolver;
import al.qa.so.utils.Utils;
import al.qa.so.utils.url.UriComparator;
import al.qa.so.utils.url.UrlComparisonStrategy;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.url;

/**
 * @author Alexey Lyanguzov.
 */
public abstract class BaseScreen<ScreenChecker extends Checker> {
    protected final ByResolver by = ByResolver.INSTANCE;
    protected final AllByResolver allby = AllByResolver.INSTANCE;

    private static final Logger LOG = Utils.getLogger();

    private final List<URI> uris = new ArrayList<>();
    private final Class<? extends UrlComparisonStrategy> urlComparisonStrategy;
    private final List<Field> traits = new ArrayList<>();

    public BaseScreen() {
        ScreenParams params = this.getClass().getAnnotation(ScreenParams.class);
        this.urlComparisonStrategy = params.urlComparisonStrategy();
        initWith(params);
    }

    @SuppressWarnings("unchecked")
    public <T extends BaseScreen> T ensure(Consumer<ScreenChecker> proc){
        return ensure((ScreenChecker)this, proc);
    }

    @SuppressWarnings("all")
    public <T extends BaseScreen> T ensure(ScreenChecker checker, Consumer<ScreenChecker> proc){
        proc.accept(checker);
        return (T)this;
    }

    public String name(){
        return this.getClass().getSimpleName();
    }

    boolean isOpened(){
        return isOpened(true);
    }

    boolean isOpened(boolean waitForProgress){
        SO.getStepRecorder().setDoAdd(false);
        boolean result = waitForNoProgressIndicator(waitForProgress) &&
                waitForTraits() &&
                isUrlCorrect();
        SO.getStepRecorder().setDoAdd(true);
        return result;
    }

    protected <T, R extends BaseScreen> R action(Consumer<T> proc){
        return action(proc, null);
    }

    @SuppressWarnings("all")
    protected <T, R extends BaseScreen> R action(Consumer<T> proc, T argument){
        return Manager.doAction(proc, argument);
    }

    protected <T, R extends BaseScreen> R transition(Consumer<T> proc){
        return (R)transition(proc, null);
    }

    @SuppressWarnings("all")
    protected <T, R extends BaseScreen> R transition(Consumer<T> proc, T argument){
        return (R)Manager.doTransition(proc, argument);
    }

    protected <T, R extends BaseScreen> R check(Consumer<T> proc){
        return check(proc, null);
    }

    @SuppressWarnings("all")
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
            throw new ScreenObjectException("Screen %s does not have trait elements", name());
        }
        for(Field traitField : traits){
            SelenideElement trait;
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
