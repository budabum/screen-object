package screens.yandex;

import al.qa.so.anno.ActionType;
import al.qa.so.BaseScreen;
import al.qa.so.Checker;
import al.qa.so.ActAs;
import al.qa.so.anno.ScreenParams;
import al.qa.so.anno.Trait;
import al.qa.so.utils.url.UriComparator;
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import modules.yandex.ImageSearchModule;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import static al.qa.so.ActAs.Action;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * @author Alexey Lyanguzov.
 */
@SuppressWarnings("unused")
@ScreenParams(
    urls = "https://yandex.ru/images/search",
    urlComparisonStrategy = UriComparator.CompareWithoutQuery.class
)
public class ImageSearchResultsScreen extends BaseScreen<ImageSearchResultsScreen> implements Checker {
    private static final String RESULTS_LIST_XPATH = "//div[contains(@class,'serp-list')]";

    @Trait
    @FindBy(xpath = "//span[@class='service__name' and text()='Картинки']")
    public ImageSearchModule searchModule;

    @FindBy(xpath = "//span[text()='Размер']/parent::node()")
    private SelenideElement sizeChooserButton;


    /******** ACTIONS *********/

    @ActionType(Action)
    public ImageSearchResultsScreen changeSizeTo(String sizeName){
        return perform(a-> {
            sizeChooserButton.shouldBe(visible);
            sizeChooserButton.should(appear);
            sizeChooserButton.shouldNotBe(hidden);
            sizeChooserButton.click();
        });
    }


    /******** CHECKS *********/

    @ActionType(ActAs.Check)
    public ImageSearchResultsScreen returnedResultsCount(int size){
        return perform(c -> {
            $("not exists").shouldNot(exist);
            ElementsCollection resultTexts = $$(By.xpath(RESULTS_LIST_XPATH + "//div[contains(@class, 'serp-item')]"));
            resultTexts.shouldHave(CollectionCondition.sizeGreaterThanOrEqual(size));
        });
    }
}
