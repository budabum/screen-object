package al.qa.so.exc;

/**
 * @author Alexey Lyanguzov.
 */
public class TestExecutionException extends RuntimeException {
    public TestExecutionException(String message) {
        super(message);
    }

    public TestExecutionException(String message, Object...args) {
        super(String.format(message, args));
    }

    public TestExecutionException(Throwable cause) {
        super(cause);
    }
}
