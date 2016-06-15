package screens;

import al.qa.so.BaseScreen;
import al.qa.so.Checker;
import al.qa.so.anno.ScreenParams;
import al.qa.so.anno.Trait;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author Alexey Lyanguzov.
 */
@SuppressWarnings("unused")
@ScreenParams(urls = "https://yandex.ru/images/")
public class ImagesScreen extends BaseScreen<ImagesScreen> implements Checker {

    @Trait @FindBy(xpath = "//input[@type='search']")
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
