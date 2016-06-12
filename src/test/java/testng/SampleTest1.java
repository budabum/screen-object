package testng;

import al.qa.so.SO;
import com.codeborne.selenide.Configuration;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import screens.TestScreens;

import static al.qa.so.SO.navigateTo;
import static screens.TestScreens.MAIN_SCREEN;

/**
 * @author Alexey Lyanguzov.
 */
public class SampleTest1 {

    @BeforeSuite
    private void before1(){
        Configuration.baseUrl = "";
        Configuration.screenshots = false;

        SO.addScreens(TestScreens.class);
    }

    @Test
    public void testSearch(){
        String phrase = "something";
        navigateTo(MAIN_SCREEN)
            .search(phrase)
            .ensure(c->{c
                .returnedResultsCount(10)
                .allSearchResultContains(phrase);
            });
    }

}
