package al.qa.so;

import al.qa.so.exc.SOException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexey Lyanguzov.
 */
class FieldNames {
    private final Map<Class<? extends BaseScreen>, FieldNamesMap> allFieldNames = new HashMap<>();

    String getFieldName(Class<? extends BaseScreen>  screenPartClass, Integer key){
        return getBaseScreenFields(screenPartClass).get(key);
    }

    void setFieldName(Class<? extends BaseScreen> screenPartClass, Integer key, String value){
        FieldNamesMap screenPartClassFields = getBaseScreenFields(screenPartClass);
        screenPartClassFields.put(key, value);
    }

    private FieldNamesMap getBaseScreenFields(Class<? extends BaseScreen> screenPartClass){
        FieldNamesMap screenPartClassFields = allFieldNames.get(screenPartClass);
        if(screenPartClassFields == null){
            screenPartClassFields = new FieldNamesMap();
            allFieldNames.put(screenPartClass, screenPartClassFields);
        }
        return screenPartClassFields;
    }

    private class FieldNamesMap extends HashMap<Integer, String>{

    }

}
