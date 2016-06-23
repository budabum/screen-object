package screens.google;

import al.qa.so.BaseScreen;
import al.qa.so.anno.ActionType;
import al.qa.so.anno.ScreenParams;
import al.qa.so.anno.Trait;
import al.qa.so.utils.url.UriComparator;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import static al.qa.so.ActAs.Check;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;

/**
 * @author Alexey Lyanguzov.
 */
@ScreenParams(
    urls = "/",
    urlComparisonStrategy = UriComparator.CompareWithoutFragment.class)
public class GoogleResultsPage extends BaseScreen<GoogleResultsPage> {
    @Trait
    @FindBy(xpath = "(//*[@class='r'])[1]")
    private SelenideElement firstResult;

    @FindBy(className = "r")
    private ElementsCollection results;


    @ActionType(Check)
    public GoogleResultsPage returnedResultsCount(int size){
        return perform(c -> results.shouldHaveSize(size));
    }

    @ActionType(Check)
    public GoogleResultsPage firstSearchResults(String aText){
        return perform(c->{
            results.first().shouldHave(text(aText));
        });
    }
}
