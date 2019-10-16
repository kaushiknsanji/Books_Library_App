/*
 * Copyright 2017 Kaushik N. Sanji
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
