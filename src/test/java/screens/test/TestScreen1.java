package screens.test;

import al.qa.so.BaseScreen;
import al.qa.so.anno.ScreenParams;
import modules.test.ModLevel1;
import org.openqa.selenium.support.FindBy;

/**
 * @author Alexey Lyanguzov.
 */
@ScreenParams(urls="//dummy")
public class TestScreen1 extends BaseScreen {

    @FindBy
    protected ModLevel1 modLevel1;

}
