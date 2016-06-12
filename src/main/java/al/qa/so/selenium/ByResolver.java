package al.qa.so.selenium;

import al.qa.so.selenide.SOElementProxy;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import java.lang.reflect.Proxy;

/**
 * @author Alexey Lyanguzov.
 */
public class ByResolver {
    public static ByResolver INSTANCE = new ByResolver();

    private ByResolver() {
    }

    public SelenideElement className(String value){
        return createSelenideElement(By.className(value));
    }

    public SelenideElement cssSelector(String value){
        return createSelenideElement(By.cssSelector(value));
    }

    public SelenideElement id(String value){
        return createSelenideElement(By.id(value));
    }

    public SelenideElement linkText(String value){
        return createSelenideElement(By.linkText(value));
    }

    public SelenideElement name(String value){
        return createSelenideElement(By.name(value));
    }

    public SelenideElement partialLinkText(String value){
        return createSelenideElement(By.partialLinkText(value));
    }

    public SelenideElement tagName(String value){
        return createSelenideElement(By.tagName(value));
    }

    public SelenideElement xpath(String value){
        return createSelenideElement(By.xpath(value));
    }

    private SelenideElement createSelenideElement(By by){
        return (SelenideElement) Proxy.newProxyInstance(
                this.getClass().getClassLoader(),
                new Class<?>[]{SelenideElement.class},
                new SOElementProxy(by)
        );
    }
}
