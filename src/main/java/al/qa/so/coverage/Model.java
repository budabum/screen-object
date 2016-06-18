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
public class Model {
    public static final Model COVERAGE = new Model();

    private final Map<String, ScreensCoverage> screens = new LinkedHashMap<>();

    private Model() {
    }

    public ScreensCoverage getScreen(String name) {
        return screens.get(name);
    }

    public ScreensCoverage addScreen(Class<? extends BaseScreen> screenClass){
        ScreensCoverage coverageInfo = new ScreensCoverage(screenClass);
        if(screens.containsKey(coverageInfo.getName())){
            throw new SOCoverageException("Screen %s is already added to coverage model", coverageInfo.getName());
        }
        screens.put(coverageInfo.getName(), coverageInfo);
        return coverageInfo;
    }

    public void clear(){
        screens.clear();
    }

    @Override
    public String toString() {
        return screens.toString();
    }
}
