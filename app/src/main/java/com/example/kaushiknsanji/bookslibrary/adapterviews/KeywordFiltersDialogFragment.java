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

package com.example.kaushiknsanji.bookslibrary.adapterviews;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.kaushiknsanji.bookslibrary.R;
import com.example.kaushiknsanji.bookslibrary.adapters.KeywordFiltersAdapter;
import com.example.kaushiknsanji.bookslibrary.models.KeywordFilter;

import java.util.ArrayList;

/**
 * An Adapter View Class for the List View Layout 'R.layout.keyword_filter_list_view'
 * that displays a Dialog for the "Search Keyword Filters" menu
 *
 * @author Kaushik N Sanji
 */
public class KeywordFiltersDialogFragment extends DialogFragment
        implements AdapterView.OnItemClickListener {

    //Constant used for logs
    private static final String LOG_TAG = KeywordFiltersDialogFragment.class.getSimpleName();

    //Constant used as an Identifier for Fragment
    public static final String FRAGMENT_TAG = LOG_TAG;
    //Instance of the DialogFragment
    private static KeywordFiltersDialogFragment mDialogFragment;
    //Instance of the interface to deliver action events
    private OnKeywordFilterSelectedListener mKeywordFilterSelectedListener;

    /**
     * Static constructor of the DialogFragment {@link KeywordFiltersDialogFragment}
     *
     * @return Instance of the DialogFragment {@link KeywordFiltersDialogFragment}
     */
    public static KeywordFiltersDialogFragment newInstance() {
        if (mDialogFragment == null) {
            //Creating a new Instance when not present
            mDialogFragment = new KeywordFiltersDialogFragment();
        }
        //Returning the Instance
        return mDialogFragment;
    }

    //Attaching the context to the fragment
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mKeywordFilterSelectedListener = (OnKeywordFilterSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnKeywordFilterSelectedListener");
        }
    }

    //Attaching the activity to the fragment
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mKeywordFilterSelectedListener = (OnKeywordFilterSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnKeywordFilterSelectedListener");
        }
    }

    /**
     * Overridden to build and display the custom Dialog container
     * for the layout "R.layout.keyword_filter_list_view"
     *
     * @param savedInstanceState The last saved instance state of the Fragment,
     *                           or null if this is a freshly created Fragment.
     * @return Returns a new Dialog instance for the Search Keyword Filters to be displayed
     * by the Fragment.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Initializing the AlertDialog Builder to build the Dialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        //Inflating the List View Layout
        //Passing NULL as we are attaching the layout to the Dialog
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.keyword_filter_list_view, null);

        //Retrieving the List View
        ListView listView = rootView.findViewById(R.id.keyword_filters_list_view_id);

        //Initializing ArrayList of KeywordFilter: START
        ArrayList<KeywordFilter> keywordFilterList = new ArrayList<>();
        String[] keywordFilterNameArray = getResources().getStringArray(R.array.keyword_filter_entries);
        String[] keywordFilterDescArray = getResources().getStringArray(R.array.keyword_filter_description);
        String[] keywordFilterValueArray = getResources().getStringArray(R.array.keyword_filter_values);
        int arrayLength = keywordFilterNameArray.length;
        for (int index = 0; index < arrayLength; index++) {
            keywordFilterList.add(new KeywordFilter(keywordFilterNameArray[index], keywordFilterDescArray[index], keywordFilterValueArray[index]));
        }
        //Initializing ArrayList of KeywordFilter: END

        //Initializing the ListView Adapter
        KeywordFiltersAdapter keywordFiltersAdapter = new KeywordFiltersAdapter(getActivity(), R.layout.keyword_filter_list_item, keywordFilterList);

        //Binding the Adapter to ListView
        listView.setAdapter(keywordFiltersAdapter);

        //Setting the OnItemClickListener on ListView
        listView.setOnItemClickListener(this);

        //Setting the inflated view on the Dialog
        dialogBuilder.setView(rootView);

        //Returning the dialog created
        return dialogBuilder.create();
    }

    /**
     * Callback method invoked when an item in this AdapterView has
     * been clicked.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Retrieving the data of selected item view
        KeywordFilter keywordFilterSelected = (KeywordFilter) parent.getItemAtPosition(position);
        //Sending the KeywordFilter's mFilterValue to the Listener to update the Search string
        mKeywordFilterSelectedListener.onKeywordFilterSelected(keywordFilterSelected.getFilterValue());
        //Dismissing the dialog
        dismiss();
    }

    //Called when the Activity is destroyed
    @Override
    public void onDetach() {
        super.onDetach();
        //Clearing the reference to the Activity to avoid leaking
        mKeywordFilterSelectedListener = null;
    }

    /**
     * Activity that creates an instance of this {@link KeywordFiltersDialogFragment}
     * needs to implement the interface to receive event callbacks
     */
    public interface OnKeywordFilterSelectedListener {
        /**
         * Callback Method of {@link KeywordFiltersDialogFragment}
         * that updates the Search string in SearchView
         * with the Search Keyword Filter selected for advanced searching
         *
         * @param filterValue is the Keyword String that needs to be appended to the
         *                    Search string in SearchView
         */
        void onKeywordFilterSelected(String filterValue);
    }

}
