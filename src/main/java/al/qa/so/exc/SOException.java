package al.qa.so.exc;

/**
 * @author Alexey Lyanguzov.
 */
public class SOException extends RuntimeException {
    public SOException(String message) {
        super(message);
    }

    public SOException(String message, Object...args) {
        super(String.format(message, args));
    }

    public SOException(Throwable cause) {
        super(cause);
    }
}
