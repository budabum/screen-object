package screens.google;

import al.qa.so.BaseScreen;
import al.qa.so.anno.ActionType;
import al.qa.so.anno.ScreenParams;
import al.qa.so.anno.Trait;
import al.qa.so.utils.url.UriComparator;
import al.qa.so.utils.url.UrlComparisonStrategy;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import static al.qa.so.ActAs.Transition;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

/**
 * @author Alexey Lyanguzov.
 */
@ScreenParams(
    urls = "/",
    urlComparisonStrategy = UriComparator.CompareWithoutQuery.class)
public class GoogleSearchPage extends BaseScreen<GoogleSearchPage> {
    @Trait
    @FindBy(name = "q")
    private SelenideElement queryField;

    @ActionType(Transition)
    public GoogleResultsPage search(String query) {
        return perform(p->{
            queryField.setValue(query).pressEnter();
        });
    }
}
