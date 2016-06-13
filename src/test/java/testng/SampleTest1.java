package testng;

import al.qa.so.SO;
import com.codeborne.selenide.Configuration;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import screens.TestScreens;

import static al.qa.so.SO.navigateTo;
import static al.qa.so.SO.onScreen;
import static screens.TestScreens.*;

/**
 * @author Alexey Lyanguzov.
 */
public class SampleTest1 {

    @BeforeSuite
    private void before1(){
        Configuration.baseUrl = "";
        Configuration.browser = "chrome";
        Configuration.screenshots = false;

        SO.addScreens(TestScreens.class);
    }

    @Test
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

    @Test
    public void testSearchImages(){
        String phrase = "table";
        navigateTo(IMAGES_SCREEN)
            .search(phrase)
            .ensure(c->{c
                .returnedResultsCount(295);
            });
        onScreen(IMAGES_SCREEN)
            .changeSize("Маленький");
    }

}
