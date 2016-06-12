package screens;

import al.qa.so.ScreenRegister;

/**
 * @author Alexey Lyanguzov.
 */
public class TestScreens implements ScreenRegister{

    public static Class<MainScreen> MAIN_SCREEN = MainScreen.class;
    public static Class<ImagesScreen> IMAGES_SCREEN = ImagesScreen.class;
    public static Class<SearchResultsScreen> SEARCH_RESULTS_SCREEN = SearchResultsScreen.class;

}
