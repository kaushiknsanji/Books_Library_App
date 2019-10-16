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

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.preference.DialogPreference;
import android.util.AttributeSet;

import com.example.kaushiknsanji.bookslibrary.R;

/**
 * Custom {@link DialogPreference} class for {@link android.widget.NumberPicker}
 *
 * @author Kaushik N Sanji
 */
public class NumberPickerPreference extends DialogPreference {

    //Constant used for logs
    private static final String LOG_TAG = NumberPickerPreference.class.getSimpleName();
    //Constant used as the Default fallback value when the preference value is not available
    private static final int FALLBACK_VALUE = 0;
    //Stores the reference to the Number Picker Layout Resource
    private final int mDialogLayoutResourceId = R.layout.pref_number_picker_dialog;
    //Stores the value of the Number Picked by the User
    private int mValue;

    public NumberPickerPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public NumberPickerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        //Delegate to other constructor
        this(context, attrs, defStyleAttr, 0);
    }

    public NumberPickerPreference(Context context, AttributeSet attrs) {
        //Delegate to other constructor
        //Use the dialogPreferenceStyle as the default style
        this(context, attrs, R.attr.dialogPreferenceStyle);
    }

    public NumberPickerPreference(Context context) {
        //Delegate to other constructor
        this(context, null);
    }

    /**
     * Getter Method for retrieving the current Number Picked Value
     *
     * @return current integer value of the preference
     */
    public int getValue() {
        return mValue;
    }

    /**
     * Setter Method for saving the Number Picked by the User
     * to the {@link android.content.SharedPreferences} and to the member {@link #mValue}
     *
     * @param numberPickedValue is the integer value being picked by the user
     */
    public void setValue(int numberPickedValue) {
        this.mValue = numberPickedValue;

        //Saving the value to the SharedPreferences
        persistInt(mValue);
    }

    /**
     * Called when a Preference is being inflated and the default value
     * attribute needs to be read.
     *
     * @param a     The set of attributes.
     * @param index The index of the default value attribute.
     * @return The default value of this preference type.
     */
    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, FALLBACK_VALUE);
    }

    /**
     * Method to set the initial value of the Preference.
     *
     * @param restorePersistedValue True to restore the persisted value;
     *                              false to use the given <var>defaultValue</var>.
     * @param defaultValue          The default value for this Preference. Only use this
     *                              if <var>restorePersistedValue</var> is false.
     */
    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        setValue(restorePersistedValue ? getPersistedInt(FALLBACK_VALUE) : (int) defaultValue);
    }

    /**
     * Returns the layout resource that is used as the content View for
     * subsequent dialogs.
     *
     * @return The layout resource of the dialog.
     */
    @Override
    public int getDialogLayoutResource() {
        return mDialogLayoutResourceId;
    }

    /**
     * Hook allowing a Preference to generate a representation of its internal
     * state that can later be used to create a new instance with that same
     * state. This state contains only information that is not persistent
     * or can be reconstructed later.
     *
     * @return A Parcelable object containing the current dynamic state of
     * this Preference, or null if there is nothing interesting to save.
     */
    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        //Checking if the preference state is persisted
        if (isPersistent()) {
            //No need to save the state as it is persistent
            return superState;
        }

        //Creating an instance of a custom BaseSavedState
        SavedState savedState = new SavedState(superState);
        //Saving the state with the current preference value
        savedState.stateValue = getValue();

        return savedState;
    }

    /**
     * Hook allowing a Preference to re-apply a representation of its internal
     * state that had previously been generated by {@link #onSaveInstanceState}.
     *
     * @param state The saved state that had previously been returned by
     *              {@link #onSaveInstanceState}.
     */
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        //Checking whether the state was saved in the onSaveInstanceState
        if (state == null || !state.getClass().equals(SavedState.class)) {
            //Calling to the superclass when the state was never saved
            super.onRestoreInstanceState(state);
            return;
        }

        //Casting the saved state to the custom BaseSavedState
        SavedState savedState = (SavedState) state;
        //Passing the super state to the superclass
        super.onRestoreInstanceState(savedState.getSuperState());

        //Restoring the state to the member to reflect the current state
        setValue(savedState.stateValue);
    }

    /**
     * Inner class that defines the state of the Preference
     */
    private static class SavedState extends BaseSavedState {
        //Standard Parcelable CREATOR object using an instance of this class
        public static final Parcelable.Creator<SavedState> CREATOR
                = new Creator<SavedState>() {

            @Override
            public SavedState createFromParcel(Parcel source) {
                //Returning the instance of this class
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }

        };
        //Holds the setting's value for persisting the preference value
        int stateValue;

        public SavedState(Parcel source) {
            super(source);
            //Get the current preference value
            stateValue = source.readInt();
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            //Write the preference value
            dest.writeInt(stateValue);
        }
    }

}
