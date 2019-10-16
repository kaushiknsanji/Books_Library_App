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

package com.example.kaushiknsanji.bookslibrary.utils;

import android.support.v7.util.DiffUtil;

import com.example.kaushiknsanji.bookslibrary.models.BookInfo;

import java.util.List;

/**
 * Class that extends {@link android.support.v7.util.DiffUtil.Callback}
 * to analyse the difference between two sets of
 * {@link com.example.kaushiknsanji.bookslibrary.models.BookInfo} objects
 * used for updating the RecyclerView's Adapter data
 *
 * @author Kaushik N Sanji
 */
public class BooksDiffUtility extends DiffUtil.Callback {

    //Stores the current List of BookInfo objects to be compared
    private List<BookInfo> mOldBookInfoList;

    //Stores the New List of BookInfo objects to be compared
    private List<BookInfo> mNewBookInfoList;

    /**
     * Constructor of {@link BooksDiffUtility}
     *
     * @param oldBookInfos is the Current List of {@link BookInfo} objects to be compared
     * @param newBookInfos is the New List of {@link BookInfo} objects to be compared
     */
    public BooksDiffUtility(List<BookInfo> oldBookInfos, List<BookInfo> newBookInfos) {
        mOldBookInfoList = oldBookInfos;
        mNewBookInfoList = newBookInfos;
    }

    /**
     * Returns the size of the old list.
     *
     * @return The size of the old list.
     */
    @Override
    public int getOldListSize() {
        return mOldBookInfoList.size();
    }

    /**
     * Returns the size of the new list.
     *
     * @return The size of the new list.
     */
    @Override
    public int getNewListSize() {
        return mNewBookInfoList.size();
    }

    /**
     * Called by the DiffUtil to decide whether two object represent the same Item.
     *
     * @param oldItemPosition The position of the item in the old list
     * @param newItemPosition The position of the item in the new list
     * @return True if the two items represent the same object or false if they are different.
     */
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        //Returning the result of the comparison of Book Id
        return mOldBookInfoList.get(oldItemPosition).getBookId().equals(
                mNewBookInfoList.get(newItemPosition).getBookId()
        );
    }

    /**
     * Called by the DiffUtil when it wants to check whether two items have the same data.
     * DiffUtil uses this information to detect if the contents of an item has changed.
     * <p>
     * DiffUtil uses this method to check equality instead of {@link Object#equals(Object)}
     * so that you can change its behavior depending on your UI.
     * <p>
     * This method is called only if {@link #areItemsTheSame(int, int)} returns
     * {@code true} for these items.
     *
     * @param oldItemPosition The position of the item in the old list
     * @param newItemPosition The position of the item in the new list which replaces the
     *                        oldItem
     * @return True if the contents of the items are the same or false if they are different.
     */
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        //Returning the result of the comparison of Book Titles only as we assume
        //that there will be no change in a subset of the fields representing the BookInfo
        return mOldBookInfoList.get(oldItemPosition).getTitle().equals(
                mNewBookInfoList.get(newItemPosition).getTitle()
        );
    }

}
