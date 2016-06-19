package al.qa.so;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
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
        return Selenide.$(by);
    }

    default SelenideElement $(By by, int index){
        return Selenide.$(by, index);
    }

    default SelenideElement $(String selector){
        return Selenide.$(selector);
    }

    default SelenideElement $(String selector, int index){
        return Selenide.$(selector, index);
    }

    default SelenideElement $(WebElement element){
        return Selenide.$(element);
    }

    default ElementsCollection $$(By by){
        return Selenide.$$(by);
    }

    default ElementsCollection $$(String selector){
        return Selenide.$$(selector);
    }

    default ElementsCollection $$(Collection<? extends WebElement> collection){
        return Selenide.$$(collection);
    }

    @SuppressWarnings("all")
    default <T, R extends BaseScreen> R perform(Consumer<T> proc, T argument){
        return Manager.perform(proc, argument);
    }
}
