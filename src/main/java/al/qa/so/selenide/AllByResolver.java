package al.qa.so.selenide;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import java.lang.reflect.Proxy;
import java.util.List;

import static com.codeborne.selenide.Selenide.$$;

/**
 * @author Alexey Lyanguzov.
 */
public class AllByResolver {
    public static final AllByResolver INSTANCE = new AllByResolver();

    private AllByResolver() {
    }

    public ElementsCollection xpath(String value){
        return createElementsCollection(By.xpath(value));
    }

    private ElementsCollection createElementsCollection(By by){
        return new ElementsCollectionWrapper(by);
    }

}
