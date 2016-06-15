package modules;

import al.qa.so.ScreenPart;
import al.qa.so.anno.Trait;
import com.codeborne.selenide.ElementsContainer;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;
import screens.ImageSearchResultsScreen;

/**
 * @author Alexey Lyanguzov.
 */
public class ImageSearchModule extends ElementsContainer implements ScreenPart {
    @Trait
    @FindBy(xpath = "//input[@type='search']")
    private SelenideElement searchField;

    @FindBy(xpath = "//button[contains(@class,'suggest2-form')]")
    private SelenideElement findButton;

    public ImageSearchResultsScreen search(String searchPhrase){
        return transition(p->{
            searchField.setValue(searchPhrase);
            findButton.click();
        });
    }

}
