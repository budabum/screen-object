package al.qa.so.coverage;

import al.qa.so.BaseScreen;
import al.qa.so.exc.SOCoverageException;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Alexey Lyanguzov.
 */
public class ScreensCoverage extends CoverageInfo {
    private final Map<String, ActionsCoverage> actions = new LinkedHashMap<>();
    private final Map<String, ChecksCoverage> checks = new LinkedHashMap<>();
    private final Map<String, ElementsCoverage> elements = new LinkedHashMap<>();
    private final Map<String, TransitionsCoverage> transitions = new LinkedHashMap<>();


    public ScreensCoverage(Class<? extends BaseScreen> screenClass) {
        super(screenClass.getSimpleName());
    }

    public ScreensCoverage addAction(String methodName){
        ActionsCoverage coverageInfo = new ActionsCoverage(methodName);
        if(actions.containsKey(coverageInfo.getName())){
            throw new SOCoverageException("Action %s is already added to coverage model for screen %s",
                coverageInfo.getName(), this.getName());

        }
        actions.put(coverageInfo.getName(), coverageInfo);
        return this;
    }

    public ScreensCoverage addCheck(String methodName){
        ChecksCoverage coverageInfo = new ChecksCoverage(methodName);
        if(checks.containsKey(coverageInfo.getName())){
            throw new SOCoverageException("Check %s is already added to coverage model for screen %s",
                coverageInfo.getName(), this.getName());

        }
        checks.put(coverageInfo.getName(), coverageInfo);
        return this;
    }

    public ScreensCoverage addElement(String methodName){
        ElementsCoverage coverageInfo = new ElementsCoverage(methodName);
        if(elements.containsKey(coverageInfo.getName())){
            throw new SOCoverageException("Element %s is already added to coverage model for screen %s",
                coverageInfo.getName(), this.getName());

        }
        elements.put(coverageInfo.getName(), coverageInfo);
        return this;
    }

    public ScreensCoverage addTransition(String methodName, String toScreen){
        TransitionsCoverage coverageInfo = new TransitionsCoverage(methodName, toScreen);
        if(transitions.containsKey(coverageInfo.getName())){
            throw new SOCoverageException("Transition %s is already added to coverage model for screen %s",
                coverageInfo.getName(), this.getName());

        }
        transitions.put(coverageInfo.getName(), coverageInfo);
        return this;
    }

    @Override
    public String toString() {
        return String.format("\n\tActions: %s\n\tChecks: %s\n\tElements: %s \n\tTransitions: %s\n",
            actions, checks, elements, transitions);
    }
}
