package al.qa.so.selenide;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.pagefactory.Annotations;

import java.lang.reflect.Field;

/**
 * @author Alexey Lyanguzov.
 */
interface IByResolver<E> {
    default E resolve(Field field){
        return create((new Annotations(field).buildBy()));
    }

    default E className(String value){
        return create(By.xpath(value));
    };

    default E cssSelector(String value){
        return create(By.cssSelector(value));
    }

    default E id(String value){
        return create(By.id(value));
    }

    default E linkText(String value){
        return create(By.linkText(value));
    }

    default E name(String value){
        return create(By.name(value));
    }

    default E partialLinkText(String value){
        return create(By.partialLinkText(value));
    }

    default E tagName(String value){
        return create(By.tagName(value));
    }

    default E xpath(String value){
        return create(By.xpath(value));
    }

    E create(By by);
}
