package al.qa.so.selenide;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import java.lang.reflect.Proxy;

/**
 * @author Alexey Lyanguzov.
 */
public class ByResolver implements IByResolver<SelenideElement> {
    public static ByResolver INSTANCE = new ByResolver();

    private ByResolver() {
    }

    public SelenideElement create(By by){
        return (SelenideElement) Proxy.newProxyInstance(
                SelenideElement.class.getClassLoader(),
                new Class<?>[]{SelenideElement.class},
                new SOElementProxy(by)
        );
    }
}
