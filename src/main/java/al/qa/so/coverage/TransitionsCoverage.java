package al.qa.so.coverage;

/**
 * @author Alexey Lyanguzov.
 */
public class TransitionsCoverage extends CoverageInfo {
    private final String toScreen;

    TransitionsCoverage(String name, String toScreen) {
        super(name);
        this.toScreen = toScreen;
    }
}
