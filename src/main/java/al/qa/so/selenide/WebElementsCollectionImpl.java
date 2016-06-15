package al.qa.so.selenide;

import com.codeborne.selenide.impl.WebElementsCollection;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexey Lyanguzov.
 */
public class WebElementsCollectionImpl implements WebElementsCollection {
    private final String description;
    private final List<WebElement> elements = new ArrayList<>();

    public WebElementsCollectionImpl(String description) {
        this.description = description;
    }

    public void add(WebElement element){
        elements.add(element);
    }

    @Override
    public List<WebElement> getActualElements() {
        return elements;
    }

    @Override
    public String description() {
        return description;
    }
}
