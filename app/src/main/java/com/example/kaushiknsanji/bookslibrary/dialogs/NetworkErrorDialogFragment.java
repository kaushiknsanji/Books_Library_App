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
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kaushiknsanji.bookslibrary.R;
import com.example.kaushiknsanji.bookslibrary.utils.TextAppearanceUtility;

/**
 * {@link DialogFragment} class to display a Network Error Dialog
 * when a Network Connectivity issue is encountered.
 * This Dialog provides a button that takes the user to the System's Network Settings page
 *
 * @author Kaushik N Sanji
 */
public class NetworkErrorDialogFragment extends DialogFragment
        implements View.OnClickListener {

    //Constant used as a Fragment Tag identifier
    public static final String DIALOG_FRAGMENT_TAG = NetworkErrorDialogFragment.class.getSimpleName();
    //Constant used for Logs
    private static final String LOG_TAG = NetworkErrorDialogFragment.class.getSimpleName();

    public static NetworkErrorDialogFragment newInstance() {
        //Returning the DialogFragment Instance
        return new NetworkErrorDialogFragment();
    }

    /**
     * Method invoked by the system to create the Dialog to be shown.
     * The layout 'R.layout.network_error_dialog' for Network Error
     * will be inflated and returned as Dialog.
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

        //Inflating the Network Error Dialog Layout 'R.layout.network_error_dialog'
        //(Passing null as we are attaching the layout ourselves to a Dialog)
        View networkErrorLayoutView = LayoutInflater.from(getActivity()).inflate(R.layout.network_error_dialog, null);

        //Retrieving the dialog's message to embed an icon in the text
        TextView networkErrorMsgTextView = networkErrorLayoutView.findViewById(R.id.network_error_text_id);
        TextAppearanceUtility.replaceTextWithImage(getContext(), networkErrorMsgTextView);

        //Retrieving the action buttons
        Button positiveButton = networkErrorLayoutView.findViewById(R.id.network_error_settings_btn_id);
        Button negativeButton = networkErrorLayoutView.findViewById(R.id.network_error_cancel_btn_id);

        //Setting the click listener on the Buttons
        positiveButton.setOnClickListener(this);
        negativeButton.setOnClickListener(this);

        //Setting this prepared layout onto the dialog's builder
        dialogBuilder.setView(networkErrorLayoutView);

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
        //Evaluating based on View's id
        switch (view.getId()) {
            case R.id.network_error_settings_btn_id:
                //When the Positive Button is clicked

                //Creating an Intent to launch the Network Settings
                Intent networkIntent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                //Verifying that the Intent will resolve to an Activity
                if (networkIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                    //Launching the Activity if resolved
                    startActivity(networkIntent);
                }

                dismiss(); //Dismissing the dialog in the end

                break;
            case R.id.network_error_cancel_btn_id:
                //When the Negative Button is clicked

                //Dismissing the dialog without doing any other operation
                dismiss();
                break;
        }
    }

}
