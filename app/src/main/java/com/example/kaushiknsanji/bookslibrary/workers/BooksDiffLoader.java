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
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.util.DiffUtil;

import com.example.kaushiknsanji.bookslibrary.models.BookInfo;
import com.example.kaushiknsanji.bookslibrary.utils.BooksDiffUtility;

import java.util.List;

/**
 * {@link AsyncTaskLoader} class that performs the difference computation between two sets
 * of {@link BookInfo} objects in the worker thread and returns the result to the
 * RecyclerView's Adapter to reload the data accordingly
 *
 * @author Kaushik N Sanji
 */
public class BooksDiffLoader extends AsyncTaskLoader<DiffUtil.DiffResult> {

    //Integer Constant used for the Diff Loader
    public final static int BOOK_DIFF_LOADER = 101;

    //Stores the current List of BookInfo objects to be compared
    private List<BookInfo> mOldBookInfoList;

    //Stores the New List of BookInfo objects to be compared
    private List<BookInfo> mNewBookInfoList;

    //Stores the result of the difference computation between the
    //current and new List of BookInfo Objects
    private DiffUtil.DiffResult mDiffResult;

    /**
     * Constructor of the Loader
     *
     * @param context      is the reference to the Activity/Application Context
     * @param oldBookInfos is the Current List of {@link BookInfo} objects to be compared
     * @param newBookInfos is the New List of {@link BookInfo} objects to be compared
     */
    public BooksDiffLoader(Context context, List<BookInfo> oldBookInfos, List<BookInfo> newBookInfos) {
        super(context);
        mOldBookInfoList = oldBookInfos;
        mNewBookInfoList = newBookInfos;
    }

    /**
     * Called on a worker thread to perform the actual load and to return
     * the result of the load operation.
     * <p>
     * Method performs the difference computation between
     * the current and new List of {@link BookInfo} objects passed and then returns its result.
     *
     * @return The result of the load operation which is the {@link android.support.v7.util.DiffUtil.DiffResult}
     * obtained after the difference computation between two sets of
     * {@link com.example.kaushiknsanji.bookslibrary.models.BookInfo} data
     * @throws android.os.OperationCanceledException if the load is canceled during execution.
     */
    @Override
    public DiffUtil.DiffResult loadInBackground() {
        //Instantiating the DiffUtil for difference computation
        BooksDiffUtility booksDiffUtility = new BooksDiffUtility(mOldBookInfoList, mNewBookInfoList);
        //Computing the Difference and returning the result
        return DiffUtil.calculateDiff(booksDiffUtility);
    }

    /**
     * Sends the result of the load to the registered listener. Should only be called by subclasses.
     *
     * @param diffResult the result of the load
     */
    @Override
    public void deliverResult(DiffUtil.DiffResult diffResult) {
        if (isReset()) {
            //Ignoring the result as the loader is already reset
            diffResult = null;
            //Returning when the loader is already reset
            return;
        }

        //Saving a reference to the old data as we are about to deliver the result
        DiffUtil.DiffResult oldDiffResult = mDiffResult;
        mDiffResult = diffResult;

        if (isStarted()) {
            //Delivering the result when the loader is started
            super.deliverResult(diffResult);
        }

        //Invalidating the old result as it is not required anymore
        if (oldDiffResult != null && oldDiffResult != mDiffResult) {
            oldDiffResult = null;
        }
    }

    /**
     * Subclasses must implement this to take care of loading their data,
     * as per {@link #startLoading()}.  This is not called by clients directly,
     * but as a result of a call to {@link #startLoading()}.
     */
    @Override
    protected void onStartLoading() {
        if (mDiffResult != null) {
            //Deliver the result immediately if already present
            deliverResult(mDiffResult);
        }

        if (takeContentChanged() || mDiffResult == null) {
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
    public void onCanceled(DiffUtil.DiffResult data) {
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
        if (mOldBookInfoList != null) {
            mOldBookInfoList = null;
        }

        if (mNewBookInfoList != null) {
            mNewBookInfoList = null;
        }

        if (mDiffResult != null) {
            mDiffResult = null;
        }

    }

    /**
     * Method that returns the new List of {@link BookInfo} objects passed to the loader
     *
     * @return The new List of {@link BookInfo} objects passed to the loader
     */
    public List<BookInfo> getNewBookInfoList() {
        return mNewBookInfoList;
    }

}
