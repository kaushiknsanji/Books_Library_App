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

package com.example.kaushiknsanji.bookslibrary.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.kaushiknsanji.bookslibrary.R;

/**
 * {@link DialogFragment} class to display the Number Picker ('R.layout.page_number_picker_dialog')
 * for the user to select and jump to the page selected
 *
 * @author Kaushik N Sanji
 */
public class PaginationNumberPickerDialogFragment extends DialogFragment
        implements View.OnClickListener, NumberPicker.OnValueChangeListener {

    //Constant used as a Fragment Tag identifier
    public static final String DIALOG_FRAGMENT_TAG = PaginationNumberPickerDialogFragment.class.getSimpleName();
    //Constant used for logs
    private static final String LOG_TAG = PaginationNumberPickerDialogFragment.class.getSimpleName();
    //Bundle Key constants for Dialog Fragment Arguments
    private static final String NUMBER_PICKER_MAX_VALUE_INT_KEY = "NumberPicker.MaxValue";
    private static final String NUMBER_PICKER_MIN_VALUE_INT_KEY = "NumberPicker.MinValue";

    //Bundle Key constant used for restoring the value selected
    private static final String NUMBER_PICKER_SEL_VALUE_INT_KEY = "NumberPicker.SelectedValue";

    //Stores the number picked by the user
    private int mSelectedValue;

    //For the Settings SharedPreferences
    private SharedPreferences mPreferences;

    /**
     * Static constructor of the DialogFragment {@link PaginationNumberPickerDialogFragment}
     *
     * @param minValue is the Integer value of the Minimum number to be set on the Number Picker
     * @param maxValue is the Integer value of the Maximum number to be set on the Number Picker
     * @return Instance of the Fragment {@link PaginationNumberPickerDialogFragment}
     */
    public static PaginationNumberPickerDialogFragment newInstance(int minValue, int maxValue) {
        //Initializing this DialogFragment
        PaginationNumberPickerDialogFragment dialogFragment = new PaginationNumberPickerDialogFragment();

        //Storing the Arguments in a Bundle: START
        Bundle bundleArgs = new Bundle(2);
        bundleArgs.putInt(NUMBER_PICKER_MIN_VALUE_INT_KEY, minValue);
        bundleArgs.putInt(NUMBER_PICKER_MAX_VALUE_INT_KEY, maxValue);
        //Storing the Arguments in a Bundle: END

        //Setting the Arguments
        dialogFragment.setArguments(bundleArgs);

        //Returning the DialogFragment Instance
        return dialogFragment;
    }

    //Called when the Fragment is about to be created
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Retrieving the instance of SharedPreferences
        mPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
    }

    /**
     * Method invoked by the system to create the Dialog to be shown.
     * The layout 'R.layout.page_number_picker_dialog' which contains
     * the {@link android.widget.NumberPicker} will be inflated
     * and returned as a Dialog.
     *
     * @param savedInstanceState The last saved instance state of the Fragment,
     *                           or null if this is a freshly created Fragment.
     * @return Return a new Dialog instance to be displayed by the Fragment.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Building the Dialog using the AlertDialog.Builder
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        //Inflating the layout containing the NumberPicker
        //(Passing null as we are attaching the layout ourselves to a dialog)
        View numberPickerLayoutView = LayoutInflater.from(getContext()).inflate(R.layout.page_number_picker_dialog, null);

        //Retrieving the NumberPicker from the inflated layout
        NumberPicker numberPicker = numberPickerLayoutView.findViewById(R.id.page_number_picker_id);

        //Initializing the NumberPicker with its parameters
        if (getArguments() != null) {
            numberPicker.setMinValue(getArguments().getInt(NUMBER_PICKER_MIN_VALUE_INT_KEY));
            numberPicker.setMaxValue(getArguments().getInt(NUMBER_PICKER_MAX_VALUE_INT_KEY));
        }

        //Updating the preselected value of the NumberPicker: START
        if (savedInstanceState != null) {
            //Restoring the selected value when instance have been saved
            mSelectedValue = savedInstanceState.getInt(NUMBER_PICKER_SEL_VALUE_INT_KEY);
        } else {
            //Defaulting the selected value to the current value of the 'startIndex' (Page to Display) setting
            mSelectedValue = mPreferences.getInt(getString(R.string.pref_page_to_display_key),
                    getResources().getInteger(R.integer.pref_page_to_display_default_value));
        }
        numberPicker.setValue(mSelectedValue);
        //Updating the preselected value of the NumberPicker: END

        //Registering a Value change listener on NumberPicker
        numberPicker.setOnValueChangedListener(this);

        //Retrieving the action buttons
        Button positiveButton = numberPickerLayoutView.findViewById(R.id.page_number_picker_set_btn_id);
        Button negativeButton = numberPickerLayoutView.findViewById(R.id.page_number_picker_cancel_btn_id);

        //Setting the click listener on the buttons
        positiveButton.setOnClickListener(this);
        negativeButton.setOnClickListener(this);

        //Setting this prepared layout onto the dialog's builder
        dialogBuilder.setView(numberPickerLayoutView);

        //Returning the Dialog instance built
        return dialogBuilder.create();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {
        //Evaluating based on the View's id
        switch (view.getId()) {
            case R.id.page_number_picker_set_btn_id:
                //When the Positive button is clicked

                //Opening the Preferences Editor
                SharedPreferences.Editor prefEditor = mPreferences.edit();
                //Updating the 'startIndex' value
                prefEditor.putInt(getString(R.string.pref_page_to_display_key), mSelectedValue);
                prefEditor.apply(); //Applying the changes

                dismiss(); //Dismissing the dialog in the end

                //Displaying a Toast Message
                Toast.makeText(getContext(), getString(R.string.navigate_page_sel_msg, mSelectedValue), Toast.LENGTH_SHORT).show();

                break;
            case R.id.page_number_picker_cancel_btn_id:
                //When the Negative button is clicked

                //Dismissing the dialog without doing any other operation
                dismiss();
                break;
        }
    }

    /**
     * Called upon a change of the current value.
     *
     * @param picker The NumberPicker associated with this listener.
     * @param oldVal The previous value.
     * @param newVal The new value.
     */
    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        //Updating the NumberPicker value on value change
        mSelectedValue = newVal;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //Saving the current user selected NumberPicker value in the bundle
        outState.putInt(NUMBER_PICKER_SEL_VALUE_INT_KEY, mSelectedValue);
        super.onSaveInstanceState(outState);
    }
}
