package al.qa.so.selenide;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import static com.codeborne.selenide.Selenide.$;

/**
 * @author Alexey Lyanguzov.
 */
public class SOElementProxy implements InvocationHandler {
    private final By by;
    private SelenideElement realSelenideElement;

    public SOElementProxy(By by) {
        this.by = by;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(realSelenideElement == null) init();
//        System.out.println("Before calling method " + method.getName());
        Object res = method.invoke(realSelenideElement, args);
//        System.out.println("After calling method " + method.getName());
        return res;
    }

    private void init(){
        this.realSelenideElement = $(by);
    }
}
