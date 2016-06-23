package screens.google;

import al.qa.so.BaseScreen;
import al.qa.so.anno.ActionType;
import al.qa.so.anno.ScreenParams;
import al.qa.so.anno.Trait;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;

import static al.qa.so.ActAs.Action;

/**
 * @author Alexey Lyanguzov.
 */
@ScreenParams(urls="/anything")
public class UnvisitedGoogleScreen extends BaseScreen {

    @Trait
    @FindBy(name = "some element")
    private SelenideElement someElement;

    @ActionType(Action)
    public UnvisitedGoogleScreen someAction(){
        return perform(p ->{
            //body
        });
    }

}
