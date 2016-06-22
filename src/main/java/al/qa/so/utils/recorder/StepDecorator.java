package al.qa.so.utils.recorder;

/**
 * @author Alexey Lyanguzov.
 */
public interface StepDecorator {

    default String onScreen(Object...args){
        return String.format("On screen %s", args);
    }

    default String actionCall(Object...args){
        return String.format("Do action %s(%s)", args);
    }

    default String transitionCall(Object...args){
        return String.format("Do transition '%s' from %s", args);
    }

    default String transitionExpectation(Object...args){
        return String.format(" ... expecting to be on %s", args);
    }

    default String performCheck(Object...args){
        return String.format("Check %s", args);
    }

    default String driverInteraction(Object...args){
        return String.format("%s %s", args);
    }

    default String driverInteractionCheck(Object...args){
        return String.format("... expecting that %s %s", args);
    }

}
