package screens;

import al.qa.so.ScreenRegister;
import screens.yandex.ImageSearchResultsScreen;
import screens.yandex.ImagesScreen;
import screens.yandex.MainScreen;
import screens.yandex.SearchResultsScreen;

/**
 * @author Alexey Lyanguzov.
 */
public class YandexScreens implements ScreenRegister{

    public static Class<MainScreen> MAIN_SCREEN = MainScreen.class;
    public static Class<ImagesScreen> IMAGES_SCREEN = ImagesScreen.class;
    public static Class<SearchResultsScreen> SEARCH_RESULTS_SCREEN = SearchResultsScreen.class;
    public static Class<ImageSearchResultsScreen> IMAGE_SEARCH_RESULTS_SCREEN = ImageSearchResultsScreen.class;

}
