package al.qa.so.coverage;

import htmlflow.HtmlWriterComposite;
import htmlflow.TextNode;
import htmlflow.elements.ElementType;

/**
 * @author Alexey Lyanguzov.
 */
public class HtmlStyleBlock<T> extends HtmlWriterComposite<T, HtmlStyleBlock<T>> {

    public HtmlStyleBlock<T> code(String msg){
        addChild(new TextNode<T>(msg));
        return this;
    }

    @Override
    public String getElementName() {
        return ElementType.STYLE.toString();
    }
}
