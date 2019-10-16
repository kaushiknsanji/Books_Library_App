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
 * Custom {@link DialogPreference} class for a Simple Confirmation on a sensitive action
 *
 * @author Kaushik N Sanji
 */
public class ConfirmationPreference extends DialogPreference {

    //Constant used for logs
    private static final String LOG_TAG = ConfirmationPreference.class.getSimpleName();

    //Stores the confirmation value given by the user
    private boolean mPositiveConfirmationResult;

    //Constant used as the Default fallback value when the preference value is not available
    private boolean FALLBACK_VALUE = false;

    public ConfirmationPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        //Propagating the call to super
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ConfirmationPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        //Delegating to other constructor
        this(context, attrs, defStyleAttr, 0);
    }

    public ConfirmationPreference(Context context, AttributeSet attrs) {
        //Delegating to other constructor
        //Using the dialogPreferenceStyle as the default dialog style
        this(context, attrs, R.attr.dialogPreferenceStyle);
    }

    public ConfirmationPreference(Context context) {
        //Delegating to other constructor
        this(context, null);
    }

    /**
     * Getter Method for retrieving the current preference value
     *
     * @return current boolean value of the preference
     */
    public boolean getValue() {
        return mPositiveConfirmationResult;
    }

    /**
     * Setter Method for saving the confirmation response selected by the User
     * to the {@link android.content.SharedPreferences} and to the member
     * {@link #mPositiveConfirmationResult}
     *
     * @param confirmationResult is the boolean value of the User's response to confirmation prompt
     */
    public void setValue(boolean confirmationResult) {
        this.mPositiveConfirmationResult = confirmationResult;

        //Saving the value to the SharedPreferences
        persistBoolean(mPositiveConfirmationResult);
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
        return a.getBoolean(index, FALLBACK_VALUE);
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
        setValue(restorePersistedValue ? getPersistedBoolean(FALLBACK_VALUE) : (boolean) defaultValue);
    }

    /**
     * Hook allowing a Preference to generate a representation of its internal
     * state that can later be used to create a new instance with that same
     * state. This state should only contain information that is not persistent
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
        savedState.confirmationResult = getValue();

        //Returning the saved state
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

        //Casting the saved state to custom BaseSavedState
        SavedState savedState = (SavedState) state;
        //Passing the super state to super class
        super.onRestoreInstanceState(savedState.getSuperState());

        //Restoring the saved state to the member to reflect the current state
        setValue(savedState.confirmationResult);
    }

    /**
     * Inner class that defines the state of the Preference
     */
    private static class SavedState extends BaseSavedState {

        //Standard Parcelable CREATOR object using an instance of this class
        public static final Parcelable.Creator<SavedState> CREATOR = new Creator<SavedState>() {
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
        boolean confirmationResult;

        public SavedState(Parcel source) {
            super(source);
            //Reading and saving the Preference value
            confirmationResult = source.readInt() == 1;
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            //Write the Preference value
            dest.writeInt(confirmationResult ? 1 : 0);
        }

    }

}
