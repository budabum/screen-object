package al1.qa.so;

import al1.qa.so.anno.ScreenParams;

/**
 * @author Alexey Lyanguzov.
 */
@ScreenParams(urls="http://example.com")
public class DefaultScreen extends BaseScreen {
    static final DefaultScreen INSTANCE = new DefaultScreen();

    private DefaultScreen(){
    }

}
