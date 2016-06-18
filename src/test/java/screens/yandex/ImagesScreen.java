package screens.yandex;

import al.qa.so.BaseScreen;
import al.qa.so.Checker;
import al.qa.so.anno.ScreenParams;
import al.qa.so.anno.Trait;
import modules.yandex.ImageSearchModule;
import org.openqa.selenium.support.FindBy;

/**
 * @author Alexey Lyanguzov.
 */
@SuppressWarnings("unused")
@ScreenParams(urls = "https://yandex.ru/images/")
public class ImagesScreen extends BaseScreen<ImagesScreen> implements Checker {

    @Trait
    @FindBy(xpath = "//span[@class='service__name' and text()='Картинки']")
    public ImageSearchModule searchModule;

}
