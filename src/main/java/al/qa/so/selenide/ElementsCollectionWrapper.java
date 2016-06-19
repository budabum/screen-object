package al.qa.so.selenide;

import al.qa.so.SO;
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import java.lang.reflect.Proxy;
import java.util.Iterator;
import java.util.ListIterator;

import static com.codeborne.selenide.Selenide.$$;

/**
 * @author Alexey Lyanguzov.
 */
//TODO: think over how to intercept that
class ElementsCollectionWrapper extends ElementsCollection implements IElementCollection<SelenideElement> {
    private By by;
    private ElementsCollection reaElementsCollection;
    private WebElementsCollectionImpl webElementsCollection;

    ElementsCollectionWrapper(By by) {
        super(new WebElementsCollectionImpl("DummyWrapper"));
        this.by = by;
        this.webElementsCollection = new WebElementsCollectionImpl("Wrapped: " + by);
    }

    @Override
    public ElementsCollection shouldHaveSize(int expectedSize) {
        if(SO.CONFIG.dryRun) { return this; }
        return getReaElementsCollection(expectedSize).shouldHaveSize(expectedSize);
    }

    @Override
    public ElementsCollection shouldBe(CollectionCondition... conditions) {
        if(SO.CONFIG.dryRun) { return this; }
        return getReaElementsCollection(conditions).shouldBe(conditions);
    }

    @Override
    public ElementsCollection shouldHave(CollectionCondition... conditions) {
        if(SO.CONFIG.dryRun) { return this; }
        return getReaElementsCollection(conditions).shouldHave(conditions);
    }

    @Override
    public ElementsCollection filter(Condition condition) {
        if(SO.CONFIG.dryRun) { return this; }
        return getReaElementsCollection(condition).filter(condition);
    }

    @Override
    public ElementsCollection filterBy(Condition condition) {
        if(SO.CONFIG.dryRun) { return this; }
        return getReaElementsCollection(condition).filterBy(condition);
    }

    @Override
    public ElementsCollection exclude(Condition condition) {
        if(SO.CONFIG.dryRun) { return this; }
        return getReaElementsCollection(condition).exclude(condition);
    }

    @Override
    public ElementsCollection excludeWith(Condition condition) {
        if(SO.CONFIG.dryRun) { return this; }
        return getReaElementsCollection(condition).excludeWith(condition);
    }

    @Override
    public SelenideElement find(Condition condition) {
        return getReaElementsCollection(condition).find(condition);
    }

    @Override
    public SelenideElement findBy(Condition condition) {
        return getReaElementsCollection(condition).findBy(condition);
    }

    @Override
    public String[] getTexts() {
        return getReaElementsCollection().getTexts();
    }

    @Override
    public SelenideElement get(int index) {
        return getReaElementsCollection().get(index);
    }

    @Override
    public SelenideElement first() {
        return getReaElementsCollection().first();
    }

    @Override
    public SelenideElement last() {
        return getReaElementsCollection().last();
    }

    @Override
    public int size() {
        return getReaElementsCollection().size();
    }

    @Override
    public Iterator<SelenideElement> iterator() {
        return getReaElementsCollection().iterator();
    }

    @Override
    public ListIterator<SelenideElement> listIterator(int index) {
        return getReaElementsCollection().listIterator(index);
    }

    private ElementsCollection getReaElementsCollection(Object...args) {
        if(reaElementsCollection == null){
            reaElementsCollection = proxyElements(by);
        }
//        System.out.println("MEth:" + Thread.currentThread().getStackTrace()[2].getMethodName());
        return reaElementsCollection;
    }

    private ElementsCollection proxyElements(By by){
        ElementsCollection orig = $$(by);
        orig.stream().forEach(e -> webElementsCollection.add(wrapToProxy(e)));
        return new ElementsCollection(webElementsCollection);
    }

    private SelenideElement wrapToProxy(SelenideElement origElement){
        return (SelenideElement) Proxy.newProxyInstance(
            SelenideElement.class.getClassLoader(),
            new Class<?>[]{SelenideElement.class},
            new SOElementProxy(origElement));
    }

}
