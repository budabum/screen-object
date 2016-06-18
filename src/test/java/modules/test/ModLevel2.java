package modules.test;

import al.qa.so.ActAs;
import al.qa.so.ActionType;
import al.qa.so.ScreenPart;
import com.codeborne.selenide.ElementsContainer;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import screens.test.TestScreen1;
import screens.test.TestScreen2;

/**
 * @author Alexey Lyanguzov.
 */
public class ModLevel2 extends ElementsContainer implements ScreenPart {

    @FindBy
    public SelenideElement selenideElementPublic2;

    @FindBy
    private SelenideElement selenideElementPrivate2;

    @FindBy
    SelenideElement selenideElementLocal2;

    @FindBy
    public WebElement webElementPublicL2;

    @ActionType(ActAs.Action)
    public ModLevel2 action1Level2(){
        return null;
    }

    @ActionType(ActAs.Action)
    public TestScreen1 action2Level2(){
        return null;
    }

    @ActionType(ActAs.Transition)
    public TestScreen2 transition1Level2(){
        return null;
    }
}
