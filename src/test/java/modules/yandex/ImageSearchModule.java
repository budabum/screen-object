package modules.yandex;

import al.qa.so.ScreenPart;
import al.qa.so.anno.ActionType;
import al.qa.so.anno.Trait;
import com.codeborne.selenide.ElementsContainer;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;
import screens.yandex.ImageSearchResultsScreen;

import static al.qa.so.ActAs.Transition;

/**
 * @author Alexey Lyanguzov.
 */
public class ImageSearchModule extends ElementsContainer implements ScreenPart {
    @Trait
    @FindBy(xpath = "//input[@type='search']")
    private SelenideElement searchField;

    @FindBy(xpath = "//button[contains(@class,'suggest2-form')]")
    private SelenideElement findButton;

    @ActionType(Transition)
    public ImageSearchResultsScreen search(String searchPhrase){
        return perform(p->{
            searchField.setValue(searchPhrase);
            findButton.click();
        });
    }

}
