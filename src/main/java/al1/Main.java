package al1;

import al1.screens.SearchResultsScreen;
import al1.screens.MainScreen;
import al1.qa.so.SO;
import com.codeborne.selenide.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static al1.qa.so.SO.navigateTo;
import static com.codeborne.selenide.Selenide.page;

/**
 * @author Alexey Lyanguzov.
 */
public class Main {

    private static Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        LOG.info("Start");

        Configuration.baseUrl = "";
        Configuration.screenshots = false;

//        UriComparator.areEquals(
//                Utils.buildUri("https://yandex.ru/search/"),
//                Utils.buildUri("https://yandex.ru/search/?lr=2&msid=1465483635.08259.20957.25260&text=Something"),
//                UriComparator.CompareWithPathOnly.class
//        );
//        System.out.println(
//                Arrays.asList(UriComparator.getUrlPartsFor(UriComparator.CompareWithoutQuery.class))
//        );
//        System.exit(0);

        SO.addScreens(
                MainScreen.class,
                SearchResultsScreen.class);

        navigateTo(MainScreen.class)
                .search("Something");

        LOG.info("Finish");
    }



}
