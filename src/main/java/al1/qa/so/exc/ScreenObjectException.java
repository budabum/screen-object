package al1.qa.so.exc;

/**
 * @author Alexey Lyanguzov.
 */
public class ScreenObjectException extends RuntimeException {
    public ScreenObjectException(String message) {
        super(message);
    }

    public ScreenObjectException(String message, Object...args) {
        super(String.format(message, args));
    }

    public ScreenObjectException(Throwable cause) {
        super(cause);
    }
}
