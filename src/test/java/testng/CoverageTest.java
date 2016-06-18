package testng;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import screens.yandex.ImageSearchResultsScreen;
import screens.yandex.ImagesScreen;
import screens.yandex.MainScreen;
import screens.yandex.SearchResultsScreen;

import static al.qa.so.coverage.Model.COVERAGE;

/**
 * @author Alexey Lyanguzov.
 */
public class CoverageTest {

    @BeforeMethod
    public void clenaModel(){
        COVERAGE.clear();
    }

    @Test
    public void createModel(){
        COVERAGE.addScreen(MainScreen.class);
        COVERAGE.addScreen(ImageSearchResultsScreen.class);
        COVERAGE.addScreen(ImagesScreen.class);
        COVERAGE.addScreen(SearchResultsScreen.class);

        System.out.println(COVERAGE);
    }

    @Test
    public void createModelWithChildren(){
        COVERAGE
            .addScreen(ImageSearchResultsScreen.class)
            .addAction("Act1")
            .addAction("Act2")
            .addTransition("Tr1", "Scr1");

        System.out.println(COVERAGE);
    }

}
