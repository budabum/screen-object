package screens;

import al.qa.so.BaseScreen;
import al.qa.so.anno.ScreenParams;
import al.qa.so.anno.Trait;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

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

    @Trait
    private SelenideElement searchField = by.xpath("//input[@aria-label='Запрос']");
    @Trait
    private SelenideElement findButton = by.xpath("//button[contains(@class,'suggest2-form')]");

    public SearchResultsScreen search(String searchPhrase){
        return (SearchResultsScreen) transition(p->{
            searchField.setValue(searchPhrase);
            findButton.click();
        });
    }

}
