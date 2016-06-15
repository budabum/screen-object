package al.qa.so.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Alexey Lyanguzov.
 */
public class StepRecorder {
    @SuppressWarnings("all")
    private final String SCREEN_CHANGE = " ";
    @SuppressWarnings("all")
    private final String ACTION_CALL = "   ";
    @SuppressWarnings("all")
    private final String DRIVER_INTERACTION = "     ";

    private boolean doAdd = true;

    private final List<String[]> steps = new ArrayList<>();

    public void init(){
        steps.clear();
        setDoAdd(true);
    }

    public void setDoAdd(boolean doAdd) {
        this.doAdd = doAdd;
    }

    public void onScreen(String msg, Object...args){
        addStep(SCREEN_CHANGE, msg, args);
    }

    public void actionCall(String msg, Object...args){
        String message = String.format(msg, args);
        Utils.getLogger().info(message);
        addStep(ACTION_CALL, msg, args);
    }

    public void performCheck(String msg, Object...args){
        String message = String.format(msg, args);
        Utils.getLogger().info(message);
        addStep(ACTION_CALL, msg, args);
    }

    public void driverInteraction(String msg, Object...args){
        addStep(DRIVER_INTERACTION, msg, args);
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
