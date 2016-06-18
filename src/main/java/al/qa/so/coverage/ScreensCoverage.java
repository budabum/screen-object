package al.qa.so.coverage;

import al.qa.so.BaseScreen;
import al.qa.so.exc.SOCoverageException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Alexey Lyanguzov.
 */
public class ScreensCoverage extends CoverageInfo {
    private final Map<String, ActionCoverage> actions = new LinkedHashMap<>();
    private final Map<String, CheckCoverage> checks = new LinkedHashMap<>();
    private final Map<String, ElementCoverage> elements = new LinkedHashMap<>();
    private final Map<String, TransitionsCoverage> transitions = new LinkedHashMap<>();


    ScreensCoverage(Class<? extends BaseScreen> screenClass) {
        super(screenClass.getSimpleName());
    }

    public ActionCoverage getAction(String name){
        return actions.get(name);
    }

    public CheckCoverage getCheck(String name){
        return checks.get(name);
    }

    public ElementCoverage getElements(String name){
        return elements.get(name);
    }

    public TransitionsCoverage getTransition(String name){
        return transitions.get(name);
    }

    public ScreensCoverage addAction(String methodName){
        ActionCoverage coverageInfo = new ActionCoverage(methodName);
        if(actions.containsKey(coverageInfo.getName())){
            throw new SOCoverageException("Action %s is already added to coverage model for screen %s",
                coverageInfo.getName(), this.getName());

        }
        actions.put(coverageInfo.getName(), coverageInfo);
        return this;
    }

    public ScreensCoverage addCheck(String methodName){
        CheckCoverage coverageInfo = new CheckCoverage(methodName);
        if(checks.containsKey(coverageInfo.getName())){
            throw new SOCoverageException("Check %s is already added to coverage model for screen %s",
                coverageInfo.getName(), this.getName());

        }
        checks.put(coverageInfo.getName(), coverageInfo);
        return this;
    }

    public ScreensCoverage addElement(String methodName){
        ElementCoverage coverageInfo = new ElementCoverage(methodName);
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
        return String.format("(%s)\n\tActions: %s\n\tChecks: %s\n\tElements: %s \n\tTransitions: %s\n",
            this.getHits(), actions, checks, elements, transitions);
    }
}
