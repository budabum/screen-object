package screens;

import al.qa.so.BaseScreen;
import al.qa.so.Checker;
import al.qa.so.anno.ScreenParams;
import al.qa.so.anno.Trait;
import al.qa.so.utils.url.UriComparator;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.sleep;

/**
 * @author Alexey Lyanguzov.
 */
@ScreenParams(
    urls = {"https://yandex.ru/images/", "https://yandex.ru/images/search"},
    urlComparisonStrategy = UriComparator.CompareWithoutQuery.class)
public class ImagesScreen extends BaseScreen<ImagesScreen> implements Checker {
    private static final String RESULTS_LIST_XPATH = "//div[contains(@class,'serp-list')]";

    @Trait
    private SelenideElement searchField = by.xpath("//input[@type='search']");
    private SelenideElement findButton = by.xpath("//button[contains(@class,'suggest2-form')]");
    private SelenideElement sizeChooserButton = by.xpath("//span[text()='Размер']");

    public ImagesScreen search(String searchPhrase){
        return action(p->{
            searchField.setValue(searchPhrase);
            findButton.click();
        });
    }

    public ImagesScreen changeSize(String sizeName){
        sizeChooserButton.click();
        sleep(2000);
        return this;
    }

    /******** CHECKS *********/

    public ImagesScreen returnedResultsCount(int size){
        return check(c -> {
            ElementsCollection resultTexts = $$(By.xpath(RESULTS_LIST_XPATH + "//div[contains(@class, 'serp-item')]"));
            resultTexts.shouldHaveSize(size);
        });
    }
}
