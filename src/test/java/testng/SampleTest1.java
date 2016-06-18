package testng;

import al.qa.so.SO;
import com.codeborne.selenide.Configuration;
import org.testng.annotations.*;
import screens.TestScreens;
import screens.YandexScreens;

import static al.qa.so.SO.navigateTo;
import static al.qa.so.SO.onScreen;
import static al.qa.so.coverage.Model.COVERAGE;
import static screens.YandexScreens.*;

/**
 * @author Alexey Lyanguzov.
 */
public class SampleTest1 {

    @BeforeSuite
    private void before1(){
        Configuration.baseUrl = "";
        Configuration.browser = "chrome";
        Configuration.browserSize = "1280x800";
        Configuration.screenshots = false;

        SO.addScreens(YandexScreens.class);
        SO.addScreens(TestScreens.class);
    }

    @AfterSuite
    private void after1(){
        System.out.println(COVERAGE);
    }

    @BeforeMethod
    private void initStepRecorder(){
        SO.getStepRecorder().init();
    }

    @AfterMethod
    private void printRecordedSteps(){
        SO.getStepRecorder().printSteps();
    }

    @Test(enabled = false)
    public void dummyTest(){}

    @Test(enabled = false)
    public void testSearchText(){
        String phrase = "something";
        String phrase2 = "anything";
        navigateTo(MAIN_SCREEN)
            .search(phrase)
            .ensure(c->{c
                .returnedResultsCount(10)
                .allSearchResultContains(phrase);
            });
        onScreen(SEARCH_RESULTS_SCREEN)
            .search(phrase2)
            .ensure(c->{c
                .returnedResultsCount(10)
                .allSearchResultContains(phrase2);
            });
    }

    @Test(enabled = !false)
    public void testSearchImages(){
        String phrase = "table";
        navigateTo(IMAGES_SCREEN)
            .searchModule.search(phrase)
            .ensure(c->{c
                .returnedResultsCount(290);
            });
        onScreen(IMAGE_SEARCH_RESULTS_SCREEN)
            .searchModule.search("chair")
            .changeSizeTo("Маленький");
    }

}
