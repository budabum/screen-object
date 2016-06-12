package al.qa.so;

import al.qa.so.anno.ScreenParams;

/**
 * @author Alexey Lyanguzov.
 */
@ScreenParams(urls="http://example.com")
public class DefaultScreen extends BaseScreen {
    static final DefaultScreen INSTANCE = new DefaultScreen();

    private DefaultScreen(){
    }

}
