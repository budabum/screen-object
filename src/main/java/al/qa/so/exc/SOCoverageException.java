package al.qa.so.exc;

/**
 * @author Alexey Lyanguzov.
 */
public class SOCoverageException extends SOException {
    public SOCoverageException(String message) {
        super(message);
    }

    public SOCoverageException(String message, Object... args) {
        super(message, args);
    }

    public SOCoverageException(Throwable cause) {
        super(cause);
    }
}
