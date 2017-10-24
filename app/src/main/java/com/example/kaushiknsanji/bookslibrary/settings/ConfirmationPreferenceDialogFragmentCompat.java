package com.example.kaushiknsanji.bookslibrary.settings;

import android.os.Bundle;
import android.support.v7.preference.PreferenceDialogFragmentCompat;
import android.view.View;

/**
 * {@link PreferenceDialogFragmentCompat} class that manages and displays the Confirmation Dialog
 * shown for the custom {@link ConfirmationPreference} class
 *
 * @author Kaushik N Sanji
 */
public class ConfirmationPreferenceDialogFragmentCompat extends PreferenceDialogFragmentCompat {

    //Constant used for logs
    private static final String LOG_TAG = ConfirmationPreferenceDialogFragmentCompat.class.getSimpleName();

    /**
     * Static Constructor of the PreferenceDialogFragmentCompat {@link ConfirmationPreferenceDialogFragmentCompat}
     *
     * @param key is the Preference key for the {@link ConfirmationPreference}
     * @return instance of {@link ConfirmationPreferenceDialogFragmentCompat}
     */
    public static ConfirmationPreferenceDialogFragmentCompat newInstance(String key) {
        ConfirmationPreferenceDialogFragmentCompat dialogFragmentCompat
                = new ConfirmationPreferenceDialogFragmentCompat();

        //Saving the Arguments in a Bundle: START
        Bundle bundle = new Bundle(1);
        bundle.putString(ARG_KEY, key);
        dialogFragmentCompat.setArguments(bundle);
        //Saving the Arguments in a Bundle: END

        //Returning the instance
        return dialogFragmentCompat;
    }

    /**
     * Binds views in the content View of the dialog to data.
     *
     * @param view The content View of the dialog, if it is custom.
     */
    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
    }

    /**
     * Invoked when the user submits a response through the dialog
     *
     * @param positiveResult boolean value indicated as True when the user clicks the positive button
     */
    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            //Saving the confirmation result on click of positive button

            //Retrieving the reference to ConfirmationPreference
            ConfirmationPreference confirmationPreference = getConfirmationPreference();
            //Notifying the OnPreferenceChangeListeners and persisting the value if True
            if (confirmationPreference.callChangeListener(positiveResult)) {
                //Saving the value
                confirmationPreference.setValue(positiveResult);
            }
        }
    }

    /**
     * Method that returns reference to {@link ConfirmationPreference}
     *
     * @return Reference to {@link ConfirmationPreference}
     */
    private ConfirmationPreference getConfirmationPreference() {
        return (ConfirmationPreference) getPreference();
    }

}
