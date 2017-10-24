package com.example.kaushiknsanji.bookslibrary.utils;

import android.content.Context;
import android.text.TextUtils;

import com.example.kaushiknsanji.bookslibrary.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility Class that manages the Preference Keys to exclude
 * from or include in the Preference Listener and also in the
 * preparation of the Search URL
 *
 * @author Kaushik N Sanji
 */
public class PreferencesObserverUtility {

    /**
     * Method that returns the List of Preference Keys
     * that are to be excluded while preparing the Search URL and also for the Preference Listener
     *
     * @return List of Strings that contain the Preference Keys to be excluded
     */
    public static List<String> getPreferenceKeysToExclude(Context context) {
        //Initializing an ArrayList of Strings for the Keys to be excluded
        ArrayList<String> keysToExclude = new ArrayList<>();

        //Adding the Preference Keys to be excluded
        keysToExclude.add(context.getString(R.string.pref_page_to_display_max_value_key));
        keysToExclude.add(context.getString(R.string.pref_reset_settings_key));
        keysToExclude.add(context.getString(R.string.pref_last_displayed_page_key));

        //Returning the exclusion list
        return keysToExclude;
    }

    /**
     * Method to add another key to an existing exclusion list
     *
     * @param keysToExclude is the List of Strings that contain the Preference Keys to be excluded
     * @param key           is an additional Preference Key string to be excluded
     */
    public static void addKeyToExclude(List<String> keysToExclude, String key) {
        //Checking initially if the Key is not empty and the list does NOT contain the Key yet
        if (!TextUtils.isEmpty(key) && !keysToExclude.contains(key)) {
            //Adding to the exclusion list
            keysToExclude.add(key);
        }
    }

    /**
     * Method to remove a key from an existing exclusion list
     *
     * @param keysToExclude is the List of Strings that contain the Preference Keys to be excluded
     * @param key           is the Preference Key string to be removed from exclusion
     */
    public static void removeKeyToInclude(List<String> keysToExclude, String key) {
        //Checking initially if the Key is not empty and the list does contain the Key
        if (!TextUtils.isEmpty(key) && keysToExclude.contains(key)) {
            //Removing from the exclusion list
            keysToExclude.remove(key);
        }
    }

}
