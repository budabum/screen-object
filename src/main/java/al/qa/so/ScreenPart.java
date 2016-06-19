package al.qa.so;

import al.qa.so.exc.SOException;
import al.qa.so.selenide.AllByResolver;
import al.qa.so.selenide.ByResolver;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * @author Alexey Lyanguzov.
 */
public interface ScreenPart {
    default String name(){
        return this.getClass().getSimpleName();
    }

    default <T, R extends BaseScreen> R perform(Consumer<T> proc){
        return perform(proc, null);
    }

    default SelenideElement $(By by){
        return ByResolver.INSTANCE.create(by);
    }

    default SelenideElement $(By by, int index){
        throw new SOException("Not yet implemented");
    }

    default SelenideElement $(String selector){
        return ByResolver.INSTANCE.cssSelector(selector);
    }

    default SelenideElement $(String selector, int index){
        throw new SOException("Not yet implemented");
    }

    default SelenideElement $(WebElement element){
        throw new SOException("Not yet implemented");
    }

    default ElementsCollection $$(By by){
        return AllByResolver.INSTANCE.create(by);
    }

    default ElementsCollection $$(String selector){
        return AllByResolver.INSTANCE.cssSelector(selector);
    }

    default ElementsCollection $$(Collection<? extends WebElement> collection){
        throw new SOException("Not yet implemented");
    }

    @SuppressWarnings("all")
    default <T, R extends BaseScreen> R perform(Consumer<T> proc, T argument){
        return Manager.perform(proc, argument);
    }
}
