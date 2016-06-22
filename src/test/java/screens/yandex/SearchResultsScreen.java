package screens.yandex;

import al.qa.so.ActAs;
import al.qa.so.anno.ActionType;
import al.qa.so.BaseScreen;
import al.qa.so.Checker;
import al.qa.so.anno.ScreenParams;
import al.qa.so.anno.Trait;
import al.qa.so.utils.url.UriComparator;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;

import static al.qa.so.ActAs.Check;
import static al.qa.so.ActAs.Transition;
import static com.codeborne.selenide.Condition.visible;

/**
 * @author Alexey Lyanguzov.
 */
@SuppressWarnings("unused")
@ScreenParams(
        urls="https://yandex.ru/search/",
        urlComparisonStrategy = UriComparator.CompareWithoutQuery.class
)
public class SearchResultsScreen extends BaseScreen<SearchResultsScreen> implements Checker {
    private static final String RESULTS_LIST_XPATH = "//div[contains(@class,'serp-list') and parent::node()[@class='content__left']]";

    @Trait @FindBy(xpath = RESULTS_LIST_XPATH)
    private SelenideElement searchResultsList;

    @FindBy(xpath = "//input[@type='search']")
    private SelenideElement searchField;

    @FindBy(xpath = "//button[contains(@class,'suggest2-form')]")
    private SelenideElement findButton;

//    @FindBy(xpath = "//div[contains(@class,'serp-list') and parent::node()[@class='content__left']]//div[@class='text organic__text']")
//    private ElementsCollection resultTexts;
    private ElementsCollection resultTexts = allby.xpath(RESULTS_LIST_XPATH + "//div[@class='text organic__text']");


    /******** TRANSITIONS *********/

    @ActionType(Transition)
    public SearchResultsScreen search(String searchPhrase){
        return perform(p->{
            searchField.setValue(searchPhrase);
            findButton.shouldBe(visible);
            findButton.click();
        });
    }

    /******** CHECKS *********/

    @ActionType(Check)
    public SearchResultsScreen returnedResultsCount(int size){
        return perform(c -> resultTexts.shouldHaveSize(size));
    }

    @ActionType(Check)
    public SearchResultsScreen allSearchResultContains(String phrase){
        return perform(c->{
            resultTexts.stream().forEach(p -> p.should(Condition.matchText("(?i)"+phrase)));
        });
    }

}

