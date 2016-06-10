package al1.screens;

import al1.qa.so.BaseScreen;
import al1.qa.so.anno.ScreenParams;
import al1.qa.so.anno.Trait;
import al1.qa.so.utils.url.UriComparator;
import com.codeborne.selenide.SelenideElement;

/**
 * @author Alexey Lyanguzov.
 */
@ScreenParams(
        urls="https://yandex.ru/search/",
        urlComparisonStrategy = UriComparator.CompareWithoutQuery.class
)
public class SearchResultsScreen extends BaseScreen {

    @Trait
    SelenideElement searchResultsList = by.xpath("//div[contains(@class,'serp-list') and parent::node()[@class='content__left']]");

}
