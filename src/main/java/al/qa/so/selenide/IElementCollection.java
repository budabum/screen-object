package al.qa.so.selenide;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * @author Alexey Lyanguzov.
 */
public interface IElementCollection<E> {
//    void addNewElement(E element);
//    ElementsCollection getElementsCollection();


    ElementsCollection shouldHaveSize(int expectedSize);

    ElementsCollection shouldBe(CollectionCondition... conditions);

    ElementsCollection shouldHave(CollectionCondition... conditions);

    ElementsCollection filter(Condition condition);

    ElementsCollection filterBy(Condition condition);

    ElementsCollection exclude(Condition condition);

    ElementsCollection excludeWith(Condition condition);

    SelenideElement find(Condition condition);

    SelenideElement findBy(Condition condition);

    String[] getTexts();



//    @Override
    E get(int index);

    SelenideElement first();

    SelenideElement last();

//    @Override
    int size();

//    @Override
    Iterator<E> iterator();

//    @Override
    ListIterator<E> listIterator(int index);

//    @Override
    String toString();
}
