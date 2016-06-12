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
    SelenideElement searchResultsList = by.xpath(RESULTS_LIST_XPATH);

//    ElementsCollection resultTexts = $$(By.xpath(RESULTS_LIST_XPATH + "//div[@class='text organic__text']"));

    public SearchResultsScreen returnedResultsCount(int size){
        return check(c -> {
            ElementsCollection resultTexts = $$(By.xpath(RESULTS_LIST_XPATH + "//div[@class='text organic__text']"));
            resultTexts.shouldHaveSize(size);
        });
    }

    public SearchResultsScreen allSearchResultContains(String phrase){
        return check(c->{
            ElementsCollection resultTexts = $$(By.xpath(RESULTS_LIST_XPATH + "//div[@class='text organic__text']"));
            resultTexts.stream().forEach(p -> {
                p.should(Condition.matchText("(?i)"+phrase));
            });
        });
    }

}

