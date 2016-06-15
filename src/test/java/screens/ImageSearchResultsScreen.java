package screens;

import al.qa.so.BaseScreen;
import al.qa.so.Checker;
import al.qa.so.anno.ScreenParams;
import al.qa.so.anno.Trait;
import al.qa.so.utils.url.UriComparator;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.hidden;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;

/**
 * @author Alexey Lyanguzov.
 */
@ScreenParams(
    urls = "https://yandex.ru/images/search",
    urlComparisonStrategy = UriComparator.CompareWithoutQuery.class)
public class ImageSearchResultsScreen extends BaseScreen<ImageSearchResultsScreen> implements Checker {
    private static final String RESULTS_LIST_XPATH = "//div[contains(@class,'serp-list')]";

    @Trait
    private SelenideElement searchField = by.xpath("//input[@type='search']");
    private SelenideElement findButton = by.xpath("//button[contains(@class,'suggest2-form')]");
    private SelenideElement sizeChooserButton = by.xpath("//span[text()='Размер']/parent::node()");

    /******** TRANSITIONS *********/

    public ImageSearchResultsScreen search(String searchPhrase){
        return transition(p->{
            searchField.setValue(searchPhrase);
            findButton.click();
        });
    }

    /******** ACTIONS *********/

    public ImageSearchResultsScreen changeSizeTo(String sizeName){
        return action(a-> {
            sizeChooserButton.shouldBe(visible);
            sizeChooserButton.should(appear);
            sizeChooserButton.shouldNotBe(hidden);
            sizeChooserButton.click();
        });
    }

    /******** CHECKS *********/

    public ImageSearchResultsScreen returnedResultsCount(int size){
        return check(c -> {
            ElementsCollection resultTexts = $$(By.xpath(RESULTS_LIST_XPATH + "//div[contains(@class, 'serp-item')]"));
            resultTexts.shouldHaveSize(size);
        });
    }
}
