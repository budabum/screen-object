package al1.screens;

import al1.qa.so.BaseScreen;
import al1.qa.so.anno.ScreenParams;
import al1.qa.so.anno.Trait;
import com.codeborne.selenide.SelenideElement;

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
        return transition(p->{
            searchField.setValue(searchPhrase);
            findButton.click();
        });
    }

}
