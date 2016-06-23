package screens;

import al.qa.so.ScreenRegister;
import screens.google.GoogleResultsPage;
import screens.google.GoogleSearchPage;
import screens.google.UnvisitedGoogleScreen;
import screens.test.TestScreen1;

/**
 * @author Alexey Lyanguzov.
 */
public class GoogleScreens implements ScreenRegister{

    public static Class<GoogleResultsPage> SEARCH_RESULTS_PAGE = GoogleResultsPage.class;
    public static Class<GoogleSearchPage> SEARCH_PAGE = GoogleSearchPage.class;
    public static Class<UnvisitedGoogleScreen> UNVISITED_PAGE = UnvisitedGoogleScreen.class;

}
