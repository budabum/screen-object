package screens.yandex;

import al.qa.so.ActAs;
import al.qa.so.anno.ActionType;
import al.qa.so.BaseScreen;
import al.qa.so.anno.ScreenParams;
import al.qa.so.anno.Trait;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Selenide.$;

/**
 * @author Alexey Lyanguzov.
 */

@ScreenParams(
        urls = {
                "https://yandex.ru",
                "https://yandex.ru/",
        })
public class MainScreen extends BaseScreen {

    @Trait @FindBy(xpath = "//input[@aria-label='Запрос']")
    private SelenideElement searchField;

    @Trait @FindBy(xpath = "//button[contains(@class,'suggest2-form')]")
    private SelenideElement findButton;

    @ActionType(ActAs.Transition)
    public SearchResultsScreen search(String searchPhrase){
        return perform(p->{
            searchField.setValue(searchPhrase);
            findButton.click();
        });
    }

}
