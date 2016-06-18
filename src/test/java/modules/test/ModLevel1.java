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
public class ModLevel1 extends ElementsContainer implements ScreenPart {
    @FindBy
    public SelenideElement selenideElementPublic1;

    @FindBy
    public ModLevel2 modLevel2;

    @FindBy
    private SelenideElement selenideElementPrivate1;

    @FindBy
    SelenideElement selenideElementLocal1;

    @FindBy
    public WebElement webElementPublicL1;

    @ActionType(ActAs.Action)
    public ModLevel2 action1Level1(){
        return null;
    }

    @ActionType(ActAs.Action)
    public TestScreen1 action2Level1(){
        return null;
    }

    @ActionType(ActAs.Transition)
    public TestScreen2 transition1Level1(){
        return null;
    }
}
