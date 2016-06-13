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
    private final String ACTION_CALL = "   ";
    @SuppressWarnings("all")
    private final String DRIVER_INTERACTION = "     ";

    private final List<String[]> steps = new ArrayList<>();

    public void init(){
        steps.clear();
    }

    public void onScreen(String msg, Object...args){
        steps.add(new String[]{SCREEN_CHANGE, String.format(msg, args)});
    }

    public void actionCall(String msg, Object...args){
        String message = String.format(msg, args);
        Utils.getLogger().info(message);
        steps.add(new String[]{ACTION_CALL, message});
    }

    public void performCheck(String msg, Object...args){
        String message = String.format(msg, args);
        Utils.getLogger().info(message);
        steps.add(new String[]{ACTION_CALL, message});
    }

    public void driverInteraction(String msg, Object...args){
        steps.add(new String[]{DRIVER_INTERACTION, String.format(msg, args)});
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

}
