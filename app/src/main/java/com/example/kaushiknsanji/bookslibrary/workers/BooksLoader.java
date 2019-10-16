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

package com.example.kaushiknsanji.bookslibrary.workers;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.kaushiknsanji.bookslibrary.R;
import com.example.kaushiknsanji.bookslibrary.models.BookInfo;
import com.example.kaushiknsanji.bookslibrary.utils.BookClientPaginationUtility;
import com.example.kaushiknsanji.bookslibrary.utils.BookClientUtility;
import com.example.kaushiknsanji.bookslibrary.utils.NetworkUtility;
import com.example.kaushiknsanji.bookslibrary.utils.PreferencesObserverUtility;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * {@link AsyncTaskLoader} class for extracting the Book Volumes
 * on the Search executed by the User, in a worker thread
 *
 * @author Kaushik N Sanji
 */
public class BooksLoader extends AsyncTaskLoader<List<BookInfo>> {

    //Integer Constant used for the Search Loader
    public final static int BOOK_SEARCH_LOADER = 100;
    //Constant used for logs
    private static final String LOG_TAG = BooksLoader.class.getSimpleName();
    //Stores the Search String executed by the User
    private String mSearchQueryStr;

    //Saves the result of the Search Query which is a List of BookInfo objects
    private List<BookInfo> mBookInfoList;

    //Boolean that stores the Network Connectivity state
    private boolean mIsNetworkConnected = false;

    /**
     * Constructor of the Loader {@link BooksLoader}
     *
     * @param context        is the reference to Activity Context
     * @param searchQueryStr is the Search Query executed by the User
     */
    public BooksLoader(Context context, String searchQueryStr) {
        super(context);
        mSearchQueryStr = searchQueryStr;
    }

    /**
     * Called on a worker thread to perform the actual load and to return
     * the result of the load operation.
     *
     * @return The result of the load operation which is a List of {@link BookInfo} objects
     * retrieved for the search done
     * @throws android.os.OperationCanceledException if the load is canceled during execution.
     */
    @Override
    public List<BookInfo> loadInBackground() {
        //Retrieving the reference to Context
        Context context = getContext();

        //Proceeding to extract data when the Internet Connectivity is established
        if (NetworkUtility.isNetworkConnected(context)) {
            //Updating the Connectivity status to True
            mIsNetworkConnected = true;

            //Preparing the URL for the Search Query
            URL searchURL = createURL(BookClientUtility.VOL_BASE_URL, mSearchQueryStr,
                    PreferencesObserverUtility.getPreferenceKeysToExclude(context));
            Log.d(LOG_TAG, "loadInBackground: searchURL " + searchURL);

            //Executing the Search and extracting the Book volumes returned
            List<BookInfo> bookInfos = BookClientUtility.searchAndExtractVolumes(searchURL);

            //Calculating the probable index of last page for pagination: START
            if (bookInfos != null && bookInfos.size() > 0) {
                //Retrieving the Preferences to get the current setting values
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

                //Retrieving the 'maxResults' (Results per page) setting value
                int itemsPerPage = preferences.getInt(context.getString(R.string.pref_results_per_page_key),
                        context.getResources().getInteger(R.integer.pref_results_per_page_default_value));

                //Retrieving the 'startIndex' (Page to Display) setting value
                int startIndex = preferences.getInt(context.getString(R.string.pref_page_to_display_key),
                        context.getResources().getInteger(R.integer.pref_page_to_display_default_value));

                //Retrieving the 'endIndex' preference value
                int endIndex = preferences.getInt(context.getString(R.string.pref_page_to_display_max_value_key),
                        startIndex);

                //Finding the next value of the last page index based on the current 'startIndex' & 'maxResults' values
                //(Normalizing setting values to 0, as 0 is the actual first page index)
                int lastPageIndex = BookClientPaginationUtility.getLastPageIndex(searchURL, itemsPerPage, startIndex - 1, endIndex - 1);
                //Normalizing the result to 1 as the first page index
                lastPageIndex += 1;

                if (lastPageIndex > endIndex) {
                    //Updating 'endIndex' preference value only when the newly determined value
                    //is greater than its previously determined value

                    //Opening the Editor to update the value of the last page index found
                    SharedPreferences.Editor prefEditor = preferences.edit();
                    prefEditor.putInt(context.getString(R.string.pref_page_to_display_max_value_key),
                            lastPageIndex); //updating the 'endIndex'
                    prefEditor.apply(); //Applying the changes

                    Log.d(LOG_TAG, "loadInBackground: lastPageIndex updated to " + lastPageIndex);
                }

            }
            //Calculating the probable index of last page for pagination: END

            //Returning the result
            return bookInfos;
        }

        //Updating the Connectivity status to False as it is not active
        mIsNetworkConnected = false;

        //For all else, returning null
        return null;
    }

    /**
     * Sends the result of the load to the registered listener. Should only be called by subclasses.
     *
     * @param newData the result of the load
     */
    @Override
    public void deliverResult(List<BookInfo> newData) {
        if (isReset()) {
            //Ignoring the result if the loader is already reset
            newData = null;
            //Returning when the loader is already reset
            return;
        }

        //Storing a reference to old data as we are about to deliver the result
        List<BookInfo> oldBookInfoList = mBookInfoList;
        mBookInfoList = newData;

        if (isStarted()) {
            //Delivering the result when the loader is started
            super.deliverResult(mBookInfoList);
        }

        //Invalidating the old data when not required during pagination
        if (oldBookInfoList != null && oldBookInfoList != mBookInfoList) {
            if (mBookInfoList != null && mBookInfoList.size() == 0) {
                //Retaining the old data if the current search during pagination did not yield any result
                mBookInfoList = oldBookInfoList;
            } else {
                //Invalidating the old data as the current search has data or is null
                oldBookInfoList = null;
            }
        }
    }

    /**
     * Subclasses must implement this to take care of loading their data,
     * as per {@link #startLoading()}.  This is not called by clients directly,
     * but as a result of a call to {@link #startLoading()}.
     */
    @Override
    protected void onStartLoading() {
        if (mBookInfoList != null) {
            //Deliver the result immediately if already retrieved
            deliverResult(mBookInfoList);
        }

        if (takeContentChanged() || mBookInfoList == null) {
            //Force a new load when the data is not yet retrieved
            //or the content has changed
            forceLoad();
        }
    }

    /**
     * Subclasses must implement this to take care of stopping their loader,
     * as per {@link #stopLoading()}.  This is not called by clients directly,
     * but as a result of a call to {@link #stopLoading()}.
     * This will always be called from the process's main thread.
     */
    @Override
    protected void onStopLoading() {
        //Canceling the load if any as the loader has entered Stopped state
        cancelLoad();
    }

    /**
     * Subclasses must implement this to take care of resetting their loader,
     * as per {@link #reset()}.  This is not called by clients directly,
     * but as a result of a call to {@link #reset()}.
     * This will always be called from the process's main thread.
     */
    @Override
    protected void onReset() {
        //Ensuring the loader has stopped
        onStopLoading();

        //Releasing the resources associated with the loader
        releaseResources();
    }

    /**
     * Called if the task was canceled before it was completed.  Gives the class a chance
     * to clean up post-cancellation and to properly dispose of the result.
     *
     * @param data The value that was returned by {@link #loadInBackground}, or null
     *             if the task threw {@link android.os.OperationCanceledException}.
     */
    @Override
    public void onCanceled(List<BookInfo> data) {
        //Canceling any asynchronous load
        super.onCanceled(data);

        //Releasing the resources associated with the loader, as the loader is canceled
        releaseResources();
    }

    /**
     * Method to release the resources associated with the loader
     */
    private void releaseResources() {
        //Invalidating the Loader data
        if (mBookInfoList != null) {
            mBookInfoList = null;
        }
    }

    /**
     * Method that creates a URL using a String containing the URL
     * and the Search String passed
     *
     * @param urlString     is String containing the URL to be used
     * @param searchString  is the Search String to be appended
     * @param keysToExclude is the List of Keys that are to be excluded from appending it to the URL
     * @return URL object of the URL String passed
     */
    private URL createURL(final String urlString, final String searchString, final List<String> keysToExclude) {
        //Returning NULL when the URL String or the Search String is empty
        if (TextUtils.isEmpty(urlString) || TextUtils.isEmpty(searchString)) {
            return null;
        }

        //Parsing the URL String
        Uri uriObject = Uri.parse(urlString);

        //Building the URL with the search string
        Uri.Builder uriBuilder = uriObject.buildUpon();
        uriBuilder.appendQueryParameter("q", searchString);

        //Getting the Context reference
        Context context = getContext();

        //Retrieving the preference key string, that is, 'startIndex'
        String startIndexPrefKeyStr = context.getString(R.string.pref_page_to_display_key);

        //Using the Preferences to build the URI with its values: START
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Set<? extends Map.Entry<String, ?>> prefEntries = preferences.getAll().entrySet();

        //Iterating over the Map of Preferences and building the URI
        for (Map.Entry<String, ?> prefEntry : prefEntries) {
            //Get the Key
            String prefKeyStr = prefEntry.getKey();

            if (!keysToExclude.contains(prefKeyStr)) {
                //If the key is not in the exclusion list passed

                //Get the associated Value
                Object prefValueObj = prefEntry.getValue();

                //Casting the object value to string if present
                String prefValueStr = "";
                if (prefValueObj != null) {
                    prefValueStr = String.valueOf(prefValueObj);
                }

                /*
                 * Continue to the Next iteration
                 * when the Key is for Content Type setting and its value is 'none'
                 * as it has no meaning in the Book API
                 */
                if (prefKeyStr.equals(context.getString(R.string.pref_content_type_key))
                        && prefValueStr.equals(context.getString(R.string.pref_content_type_default))) {
                    continue;
                }

                //Appending the Key and Value to the URI when the Value is not empty
                if (!TextUtils.isEmpty(prefValueStr)) {
                    if (prefKeyStr.equals(startIndexPrefKeyStr)) {
                        //Normalizing the value of 'startIndex' setting to 0, as 0 is the first page
                        int startIndex = Integer.parseInt(prefValueStr);
                        uriBuilder.appendQueryParameter(prefKeyStr, String.valueOf(startIndex - 1));
                    } else {
                        //When the setting key is not the 'startIndex' key
                        uriBuilder.appendQueryParameter(prefKeyStr, prefValueStr);
                    }
                }

            }

        }
        //Using the Preferences to build the URI with its values: END

        //Forming the URL using the URI built
        URL urlObject = null;
        try {
            urlObject = new URL(uriBuilder.toString());
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error occurred while forming the URL\n", e);
        }

        //Returning the URL Object formed
        return urlObject;
    }

    /**
     * Method that returns the evaluated Network Connectivity status
     *
     * @return a Boolean representing the state of Internet Connectivity
     * <br/><b>TRUE</b> if the Internet Connectivity is established
     * <br/><b>FALSE</b> otherwise
     */
    public boolean getNetworkConnectivityStatus() {
        return mIsNetworkConnected;
    }

}