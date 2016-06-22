package al.qa.so.utils.recorder;

import al.qa.so.SO;
import al.qa.so.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Alexey Lyanguzov.
 */
public class StepRecorder {
    @SuppressWarnings("all")
    private static final String SCREEN_CHANGE = " ";
    @SuppressWarnings("all")
    private static final String ACTION_CALL = "   ";
    @SuppressWarnings("all")
    private static final String DRIVER_INTERACTION = "     ";

//    private StepDecorator stepDecorator = new DefaultStepDecorator();
    private StepDecorator stepDecorator = new GherkinDecorator();

    private boolean doAdd = true;

    private final List<String[]> steps = new ArrayList<>();

    public void init(){
        steps.clear();
        setDoAdd(true);
    }

    public void setDoAdd(boolean doAdd) {
        this.doAdd = doAdd;
    }

    public void onScreen(Object...args){
        addStep(SCREEN_CHANGE, stepDecorator.onScreen(args));
    }

    public void actionCall(Object...args){
        String message = stepDecorator.actionCall(args);
        Utils.getLogger().info(message);
        addStep(ACTION_CALL, message);
    }

    public void transitionCall(Object...args){
        String message = stepDecorator.transitionCall(args);
        Utils.getLogger().info(message);
        addStep(ACTION_CALL, message);
    }

    public void transitionExpectation(Object...args){
        String message = stepDecorator.transitionExpectation(args);
        Utils.getLogger().info(message);
        addStep(ACTION_CALL, message);
    }

    public void performCheck(Object...args){
        String message = stepDecorator.performCheck(args);
        Utils.getLogger().info(message);
        addStep(ACTION_CALL, message);
    }

    public void driverInteraction(Object...args){
        if(SO.CONFIG.reportWebDriverInteraction){
            addStep(DRIVER_INTERACTION, stepDecorator.driverInteraction(args));
        }
    }

    public void driverInteractionCheck(Object...args){
        if(SO.CONFIG.reportWebDriverInteraction){
            addStep(DRIVER_INTERACTION, stepDecorator.driverInteractionCheck(args));
        }
    }

    public void printSteps(){
        StringBuffer sb = new StringBuffer();
        IntStream.range(1, 21).forEach(i -> sb.append("=#="));
        sb.append("\n").append(" # Test Steps:").append("\n");
        sb.append(" # ");
        IntStream.range(1, 18).forEach(i -> sb.append("-"));
        sb.append("\n");
        steps.stream().forEach(s -> sb.append(" # ").append(s[0]).append(s[1]).append("\n"));
        IntStream.range(1, 21).forEach(i -> sb.append("=#="));
        System.out.println(sb.toString());
    }

    private void addStep(String indent, String msg, Object...args){
        if(doAdd){
            steps.add(new String[]{indent, String.format(msg, args)});
        }
    }

}
