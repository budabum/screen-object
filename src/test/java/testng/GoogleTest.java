package testng;

import al.qa.so.SO;
import com.codeborne.selenide.Configuration;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import screens.GoogleScreens;

import static al.qa.so.SO.navigateTo;
import static screens.GoogleScreens.SEARCH_PAGE;

/**
 * @author Alexey Lyanguzov.
 */
public class GoogleTest {
    @BeforeSuite
    private void doit(){
        Configuration.baseUrl = "https://www.google.ru";
        Configuration.browser = "chrome";
        Configuration.browserSize = "1024x768";

        SO.addScreens(GoogleScreens.class);
        SO.CONFIG.dryRun = true;
//        SO.CONFIG.reportWebDriverInteraction = false;
    }

    @BeforeMethod
    private void initStepRecorder(){
        SO.getStepRecorder().init();
    }
    @AfterMethod
    private void printRecordedSteps(){
        SO.getStepRecorder().printSteps();
    }

    @Test
    public void selenideExamplePageObjectTest(){
        navigateTo(SEARCH_PAGE)
            .search("selenide")
            .ensure()
                .returnedResultsCount(10)
                .allSearchResultContains("selenide");
    }

}
