package screens;

import al.qa.so.BaseScreen;
import al.qa.so.Checker;
import al.qa.so.anno.ScreenParams;
import al.qa.so.anno.Trait;
import al.qa.so.utils.url.UriComparator;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$$;

/**
 * @author Alexey Lyanguzov.
 */
@ScreenParams(
        urls="https://yandex.ru/search/",
        urlComparisonStrategy = UriComparator.CompareWithoutQuery.class
)
public class SearchResultsScreen extends BaseScreen<SearchResultsScreen> implements Checker {
    private static final String RESULTS_LIST_XPATH = "//div[contains(@class,'serp-list') and parent::node()[@class='content__left']]";

    @Trait
    private SelenideElement searchResultsList = by.xpath(RESULTS_LIST_XPATH);

    private SelenideElement searchField = by.xpath("//input[@type='search']");
    private SelenideElement findButton = by.xpath("//button[contains(@class,'suggest2-form')]");

//    ElementsCollection resultTexts = $$(By.xpath(RESULTS_LIST_XPATH + "//div[@class='text organic__text']"));
    ElementsCollection resultTexts = $$(By.xpath(RESULTS_LIST_XPATH + "//div[@class='text organic__text']"));


    /******** ACTIONS *********/

    public SearchResultsScreen search(String searchPhrase){
        return transition(p->{
            searchField.setValue(searchPhrase);
            findButton.click();
        });
    }

    /******** CHECKS *********/

    public SearchResultsScreen returnedResultsCount(int size){
        return check(c -> {
            resultTexts.shouldHaveSize(size);
        });
    }

    public SearchResultsScreen allSearchResultContains(String phrase){
        return check(c->{
            resultTexts.stream().forEach(p -> {
                p.should(Condition.matchText("(?i)"+phrase));
            });
        });
    }

}

