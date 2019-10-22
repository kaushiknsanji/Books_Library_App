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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

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

    //Bundle key constants used for the changes in payload content: START
    //For the Image Link
    public static final String PAYLOAD_BOOK_IMAGE_LINK_STR_KEY = "Payload.BookImageLink";
    //For the Title
    public static final String PAYLOAD_BOOK_TITLE_STR_KEY = "Payload.BookTitle";
    //For the Authors
    public static final String PAYLOAD_BOOK_AUTHORS_STR_KEY = "Payload.BookAuthors";
    //For the Publisher
    public static final String PAYLOAD_BOOK_PUBLISHER_STR_KEY = "Payload.BookPublisher";
    //For the Published Date
    public static final String PAYLOAD_BOOK_PUBLISHED_DATE_STR_KEY = "Payload.BookPublishedDate";
    //For the Type of the Book
    public static final String PAYLOAD_BOOK_TYPE_STR_KEY = "Payload.BookType";
    //For the Number of Pages in a Book
    public static final String PAYLOAD_BOOK_PAGE_COUNT_INT_KEY = "Payload.BookPageCount";
    //For the Categories
    public static final String PAYLOAD_BOOK_CATEGORIES_STR_KEY = "Payload.BookCategories";
    //For the Book's Rating
    public static final String PAYLOAD_BOOK_RATING_FLOAT_KEY = "Payload.BookRating";
    //For the Count of Ratings the Book has received
    public static final String PAYLOAD_BOOK_RATING_COUNT_STR_KEY = "Payload.BookRatingCount";
    //For whether the Book is for sale or not
    public static final String PAYLOAD_BOOK_FOR_SALE_BOOL_KEY = "Payload.Book.IsForSale";
    //For whether the Book price is discounted
    public static final String PAYLOAD_BOOK_DISCOUNTED_BOOL_KEY = "Payload.Book.IsDiscounted";
    //For the List price of the Book
    public static final String PAYLOAD_BOOK_LIST_PRICE_STR_KEY = "Payload.BookListPrice";
    //For the Retail price of the Book
    public static final String PAYLOAD_BOOK_RETAIL_PRICE_STR_KEY = "Payload.BookRetailPrice";
    //Bundle key constants used for the changes in payload content: END

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
        //Returning the result of the comparison of Book Title instead of Id,
        //since Id is always unique and this prevents the view from being reused even if their Titles are same
        return mOldBookInfoList.get(oldItemPosition).getTitle().equals(
                mNewBookInfoList.get(newItemPosition).getTitle()
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
        //Reading the objects at the position
        BookInfo oldBookInfo = mOldBookInfoList.get(oldItemPosition);
        BookInfo newBookInfo = mNewBookInfoList.get(newItemPosition);

        //Comparing the required contents of the BookInfo objects: START
        //(Returning FALSE when one of the contents are not same)

        //For the Image Link
        if (!TextUtils.equals(oldBookInfo.getImageLinkForItemInfo(), newBookInfo.getImageLinkForItemInfo())) {
            return false;
        }

        //For the Title
        if (!oldBookInfo.getTitle().equals(newBookInfo.getTitle())) {
            return false;
        }

        //For the Authors
        if (!oldBookInfo.getAuthors("").equals(newBookInfo.getAuthors(""))) {
            return false;
        }

        //For the Publisher
        if (!oldBookInfo.getPublisher("").equals(newBookInfo.getPublisher(""))) {
            return false;
        }

        //For the Published Date
        if (!oldBookInfo.getPublishedDateInMediumFormat("").equals(newBookInfo.getPublishedDateInMediumFormat(""))) {
            return false;
        }

        //For the Type of the Book
        if (!oldBookInfo.getBookType().equals(newBookInfo.getBookType())) {
            return false;
        }

        //For the Number of Pages in a Book
        if (oldBookInfo.getPageCount() != newBookInfo.getPageCount()) {
            return false;
        }

        //For the Categories
        if (!oldBookInfo.getCategories("").equals(newBookInfo.getCategories(""))) {
            return false;
        }

        //For the Book's Rating
        if (oldBookInfo.getBookRatings() != newBookInfo.getBookRatings()) {
            return false;
        }

        //For the Count of Ratings the Book has received
        if (!oldBookInfo.getBookRatingCount().equals(newBookInfo.getBookRatingCount())) {
            return false;
        }

        //For whether the Book is for sale or not
        if (oldBookInfo.isForSale() != newBookInfo.isForSale()) {
            return false;
        }

        //For whether the Book price is discounted
        if (oldBookInfo.isDiscounted() != newBookInfo.isDiscounted()) {
            return false;
        }

        //For the List price of the Book
        if (!oldBookInfo.getListPrice().equals(newBookInfo.getListPrice())) {
            return false;
        }

        //For the Retail price of the Book
        if (!oldBookInfo.getRetailPrice().equals(newBookInfo.getRetailPrice())) {
            return false;
        }

        //Comparing the required contents of the BookInfo objects: END

        //Returning TRUE when all the contents compared are same
        return true;
    }

    /**
     * When {@link #areItemsTheSame(int, int)} returns {@code true} for two items and
     * {@link #areContentsTheSame(int, int)} returns false for them, DiffUtil
     * calls this method to get a payload about the change.
     * <p>
     * For example, if you are using DiffUtil with {@link RecyclerView}, you can return the
     * particular field that changed in the item and your
     * {@link RecyclerView.ItemAnimator ItemAnimator} can use that
     * information to run the correct animation.
     * <p>
     * Default implementation returns {@code null}.
     *
     * @param oldItemPosition The position of the item in the old list
     * @param newItemPosition The position of the item in the new list
     * @return A payload object that represents the change between the two items.
     */
    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        //Using a Bundle to pass the changes that are to be made
        Bundle bundle = new Bundle(6); //There are only six items, hence fixing the capacity

        //Reading the objects at the position
        BookInfo oldBookInfo = mOldBookInfoList.get(oldItemPosition);
        BookInfo newBookInfo = mNewBookInfoList.get(newItemPosition);

        //Evaluating the differences and adding them to the Bundle: START

        //For the Image Link
        if (!TextUtils.equals(oldBookInfo.getImageLinkForItemInfo(), newBookInfo.getImageLinkForItemInfo())) {
            bundle.putString(PAYLOAD_BOOK_IMAGE_LINK_STR_KEY, newBookInfo.getImageLinkForItemInfo());
        }

        //For the Title
        if (!oldBookInfo.getTitle().equals(newBookInfo.getTitle())) {
            bundle.putString(PAYLOAD_BOOK_TITLE_STR_KEY, newBookInfo.getTitle());
        }

        //For the Authors
        if (!oldBookInfo.getAuthors("").equals(newBookInfo.getAuthors(""))) {
            bundle.putString(PAYLOAD_BOOK_AUTHORS_STR_KEY, newBookInfo.getAuthors(""));
        }

        //For the Publisher
        if (!oldBookInfo.getPublisher("").equals(newBookInfo.getPublisher(""))) {
            bundle.putString(PAYLOAD_BOOK_PUBLISHER_STR_KEY, newBookInfo.getPublisher(""));
        }

        //For the Published Date
        if (!oldBookInfo.getPublishedDateInMediumFormat("").equals(newBookInfo.getPublishedDateInMediumFormat(""))) {
            bundle.putString(PAYLOAD_BOOK_PUBLISHED_DATE_STR_KEY, newBookInfo.getPublishedDateInMediumFormat(""));
        }

        //For the Number of Pages in a Book and the Type of a Book
        if (oldBookInfo.getPageCount() != newBookInfo.getPageCount()
                || !oldBookInfo.getBookType().equals(newBookInfo.getBookType())) {
            bundle.putInt(PAYLOAD_BOOK_PAGE_COUNT_INT_KEY, newBookInfo.getPageCount());
            bundle.putString(PAYLOAD_BOOK_TYPE_STR_KEY, newBookInfo.getBookType());
        }

        //For the Categories
        if (!oldBookInfo.getCategories("").equals(newBookInfo.getCategories(""))) {
            bundle.putString(PAYLOAD_BOOK_CATEGORIES_STR_KEY, newBookInfo.getCategories(""));
        }

        //For the Book's Rating
        if (oldBookInfo.getBookRatings() != newBookInfo.getBookRatings()) {
            bundle.putFloat(PAYLOAD_BOOK_RATING_FLOAT_KEY, newBookInfo.getBookRatings());
        }

        //For the Count of Ratings the Book has received
        if (!oldBookInfo.getBookRatingCount().equals(newBookInfo.getBookRatingCount())) {
            bundle.putString(PAYLOAD_BOOK_RATING_COUNT_STR_KEY, newBookInfo.getBookRatingCount());
        }

        //For whether the Book is for sale or not, its discount and the Price
        if (oldBookInfo.isForSale() != newBookInfo.isForSale()
                || oldBookInfo.isDiscounted() != newBookInfo.isDiscounted()
                || !oldBookInfo.getListPrice().equals(newBookInfo.getListPrice())
                || !oldBookInfo.getRetailPrice().equals(newBookInfo.getRetailPrice())) {
            bundle.putBoolean(PAYLOAD_BOOK_FOR_SALE_BOOL_KEY, newBookInfo.isForSale());
            bundle.putBoolean(PAYLOAD_BOOK_DISCOUNTED_BOOL_KEY, newBookInfo.isDiscounted());
            bundle.putString(PAYLOAD_BOOK_LIST_PRICE_STR_KEY, newBookInfo.getListPrice());
            bundle.putString(PAYLOAD_BOOK_RETAIL_PRICE_STR_KEY, newBookInfo.getRetailPrice());
        }

        //Evaluating the differences and adding them to the Bundle: END

        //Returning null when no change is found
        if (bundle.size() == 0) return null;

        //Returning the Payload prepared
        return bundle;
    }

}
