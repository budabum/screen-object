package al.qa.so.selenide;

import com.codeborne.selenide.ElementsCollection;
import org.openqa.selenium.By;

/**
 * @author Alexey Lyanguzov.
 */
public class AllByResolver implements IByResolver<ElementsCollection> {
    public static final AllByResolver INSTANCE = new AllByResolver();

    private AllByResolver() {
    }

    @Override
    public ElementsCollection create(By by) {
        return new ElementsCollectionWrapper(by);
    }

}
