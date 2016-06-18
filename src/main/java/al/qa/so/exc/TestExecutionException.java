package al.qa.so.exc;

/**
 * @author Alexey Lyanguzov.
 */
public class TestExecutionException extends SOException {
    public TestExecutionException(String message) {
        super(message);
    }

    public TestExecutionException(String message, Object... args) {
        super(message, args);
    }

    public TestExecutionException(Throwable cause) {
        super(cause);
    }
}
