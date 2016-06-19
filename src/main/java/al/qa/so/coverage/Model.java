package al.qa.so.coverage;

import al.qa.so.BaseScreen;
import al.qa.so.exc.SOCoverageException;
import htmlflow.HtmlView;

import java.io.PrintStream;
import java.util.LinkedHashMap;
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

    public void report(){
        HtmlView<?> html = new HtmlView<>();
        html.head().addChild(new HtmlStyleBlock<>()).code(
    "\n.covtable{border: thin solid black; text-align: left; }\n" +
    "BODY,TABLE {font-size: small}\n" +
    "H2{text-align:center;}\n" +
    "TH{border:thin solid black;text-align:center; background-color: #CCCCCC}\n" +
    "TD{border:thin solid black;text-align:right}\n" +
    "TD.namecol{border:thin solid black;text-align:left}\n" +
    "CAPTION{text-align: left; font-weight: bold}\n"
        );
        html
            .body()
            .heading(1, "Summary Report")
            .heading(1, "Screens Summary Report")
            .table().addAttr("width", "80%");

        try(PrintStream out = System.out){
//        try(PrintStream out = new PrintStream(new FileOutputStream("cov.report.hml"))){
            html.setPrintStream(out).write();
//        } catch (IOException e) {
//            throw new SOCoverageException(e);
        }
    }

    @Override
    public String toString() {
        return screens.toString();
    }
}
