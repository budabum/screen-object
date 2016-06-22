package al.qa.so.utils.recorder;

/**
 * @author Alexey Lyanguzov.
 */
public class GherkinDecorator implements StepDecorator {

    @Override
    public String onScreen(Object... args) {
        return String.format("Given I am on screen %s", args);
    }

    @Override
    public String actionCall(Object... args) {
        return String.format("When I %s", args);
    }

    @Override
    public String transitionCall(Object... args) {
        return String.format("When I do %s to navigate to %s", args);
    }

    @Override
    public String transitionExpectation(Object... args) {
        return String.format("Then I expect to be on %s", args);
    }

    @Override
    public String performCheck(Object... args) {
        return String.format("Then I check that %s", args);
    }

    @Override
    public String driverInteraction(Object... args) {
        return String.format("And %s %s", args);
    }

    @Override
    public String driverInteractionCheck(Object... args) {
        return String.format("Then I expect that %s %s", args);
    }
}
