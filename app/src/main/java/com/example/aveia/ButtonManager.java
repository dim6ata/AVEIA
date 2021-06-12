package com.example.aveia;

import android.content.Context;
import android.widget.RadioGroup;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class which offers button manipulation tools.
 */
public class ButtonManager {

    /**
     * Retrieves UI element id values and populates a map that is used to perform tasks based on id
     * of a selected element and its location within the list of queried elements.
     * @param idPrefix the string value of the element's id.
     * @param context the context from which this query occurs.
     * @param length the length of elements that need to be retrieved.
     * @return Map KEY = element id, VALUE = position in list of elements.
     */
    public static Map<Integer, Integer> retrieveButtons(String idPrefix, Context context, int length){

        Map<Integer, Integer> buttonPositionMap = new HashMap<>();
        for (int i = 0; i < length; i++){
            int id = context.getResources().getIdentifier(idPrefix + i, "id" , context.getPackageName());
            buttonPositionMap.put(id, i);
        }
        return buttonPositionMap;
    }


    /**
     * Checks radio buttons that are passed to this method by using an id that is specific to that view.
     * @param group RadioGroup instance that contains the group of buttons which will be switched between.
     * @param id the id of the desired radio button to be checked.
     */
    public static void checkRadioButton(RadioGroup group, int id){
        group.check(group.getChildAt(id).getId());
    }

}
