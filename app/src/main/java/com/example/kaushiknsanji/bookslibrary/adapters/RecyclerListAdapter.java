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

package com.example.kaushiknsanji.bookslibrary.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.kaushiknsanji.bookslibrary.R;
import com.example.kaushiknsanji.bookslibrary.models.BookInfo;
import com.example.kaushiknsanji.bookslibrary.observers.OnAdapterItemClickListener;
import com.example.kaushiknsanji.bookslibrary.observers.OnAdapterItemDataSwapListener;
import com.example.kaushiknsanji.bookslibrary.utils.TextAppearanceUtility;
import com.example.kaushiknsanji.bookslibrary.workers.BooksDiffLoader;
import com.example.kaushiknsanji.bookslibrary.workers.ImageDownloaderFragment;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter Class of the RecyclerView whose layout 'R.layout.books_list_item' is inflated by
 * {@link com.example.kaushiknsanji.bookslibrary.adapterviews.RecyclerViewFragment}
 * that is used along with the {@link android.support.v7.widget.LinearLayoutManager}
 * to bind and display a list of {@link BookInfo} objects
 *
 * @author Kaushik N Sanji
 */
public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ViewHolder>
        implements LoaderManager.LoaderCallbacks<DiffUtil.DiffResult> {

    //Constant used for logs
    private static final String LOG_TAG = RecyclerListAdapter.class.getSimpleName();

    //Constants used for the Diff Loader's Bundle arguments
    private static final String OLD_LIST_STR_KEY = "BookInfo.Old";
    private static final String NEW_LIST_STR_KEY = "BookInfo.New";

    //Stores the layout resource of the list item that needs to be inflated manually
    private int mLayoutRes;

    //Stores the reference to the Context
    private Context mContext;

    //Stores the List of BookInfo objects which is the Dataset of the Adapter
    private List<BookInfo> mBookInfoList;

    //Stores a reference to the font face to be used for the Book Title
    private Typeface mTitleTextTypeface;

    //Stores the reference to the Listener OnAdapterItemClickListener
    private OnAdapterItemClickListener mItemClickListener;

    //Stores the reference to the Listener OnAdapterItemDataSwapListener
    private OnAdapterItemDataSwapListener mItemDataSwapListener;

    /**
     * Constructor of the Adapter {@link RecyclerListAdapter}
     *
     * @param context   is the Context of the Activity/Application
     * @param resource  is the layout resource ID of the item view ('R.layout.books_list_item')
     * @param bookInfos is the list of BookInfo objects which is the Dataset of the Adapter
     */
    public RecyclerListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<BookInfo> bookInfos) {
        mContext = context;
        mLayoutRes = resource;
        mBookInfoList = bookInfos;

        //Saving an instance of the font face for the Book Title
        mTitleTextTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/garamond_bold.ttf");
    }

    /**
     * Method that returns the reference to the Context used
     *
     * @return the reference to the Context used
     */
    private Context getContext() {
        return mContext;
    }

    /**
     * Method that registers the {@link OnAdapterItemClickListener} for the parent Fragment/Activity
     * to receive item click events
     *
     * @param listener is the instance of the Fragment/Activity implementing the {@link OnAdapterItemClickListener}
     */
    public void setOnAdapterItemClickListener(OnAdapterItemClickListener listener) {
        mItemClickListener = listener;
    }

    /**
     * Method that registers the {@link OnAdapterItemDataSwapListener} for the
     * {@link com.example.kaushiknsanji.bookslibrary.BookSearchActivity}
     * to receive event callbacks
     *
     * @param listener is the instance of the Activity implementing the {@link OnAdapterItemDataSwapListener}
     */
    public void setOnAdapterItemDataSwapListener(OnAdapterItemDataSwapListener listener) {
        mItemDataSwapListener = listener;
    }

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflating the item Layout view
        //Passing False as we are attaching the View ourselves
        View itemView = LayoutInflater.from(getContext()).inflate(mLayoutRes, parent, false);

        //Instantiating the ViewHolder to initialize the reference to the view components in the item layout
        //and returning the same
        return new ViewHolder(itemView);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     *
     * @param viewHolder The ViewHolder which should be updated to represent the contents of the
     *                   item at the given position in the data set.
     * @param position   The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        //Retrieving the BookInfo object at the current item position
        BookInfo bookInfo = mBookInfoList.get(position);

        //Retrieving the Context
        Context context = getContext();

        //Populating the data onto the Template View using the BookInfo object: START

        //Updating the Book Image if link is present
        String imageURLStr = bookInfo.getImageLinkForItemInfo();
        if (!TextUtils.isEmpty(imageURLStr)) {
            //Loading the Image when link is present
            ImageDownloaderFragment
                    .newInstance(((FragmentActivity) getContext()).getSupportFragmentManager(), position)
                    .executeAndUpdate(viewHolder.bookImageView,
                            imageURLStr,
                            position);
        } else {
            //Resetting to the default Book image when link is absent
            viewHolder.bookImageView.setImageResource(R.drawable.ic_book);
        }

        //Updating the Title
        viewHolder.titleTextView.setText(bookInfo.getTitle());
        //Setting the Title font
        viewHolder.titleTextView.setTypeface(mTitleTextTypeface);

        //Updating the Authors
        viewHolder.authorTextView.setText(bookInfo.getAuthors(context.getString(R.string.no_authors_found_default_text)));

        //Updating the Publisher
        viewHolder.publisherTextView.setText(bookInfo.getPublisher(context.getString(R.string.no_publisher_found_default_text)));

        //Updating the Published Date : START
        String publishedDateStr = "";
        try {
            publishedDateStr = bookInfo.getPublishedDateInMediumFormat(context.getString(R.string.no_published_date_default_text));
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Error occurred while parsing and formatting the Published date", e);
        } finally {
            viewHolder.publishedDateTextView.setText(publishedDateStr);
        }
        //Updating the Published Date : END

        //Updating the Count of Pages and the Book Type
        int pageCount = bookInfo.getPageCount();
        String bookTypeStr = bookInfo.getBookType();
        viewHolder.pagesTextView.setText(context.getString(R.string.pages_book_type, pageCount, bookTypeStr));

        //Updating the Categories
        viewHolder.categoriesTextView.setText(bookInfo.getCategories(context.getString(R.string.no_categories_found_default_text)));

        //Updating the Book's Ratings
        viewHolder.bookRatingBarView.setRating(bookInfo.getBookRatings());

        //Updating the Count of Ratings
        viewHolder.ratingCountTextView.setText(bookInfo.getBookRatingCount());

        //Updating the Price of the Book : START
        //Ensuring that the view components are visible
        if (viewHolder.listPriceTextView.getVisibility() == View.GONE) {
            viewHolder.listPriceTextView.setVisibility(View.VISIBLE);
        }
        if (viewHolder.retailPriceTextView.getVisibility() == View.GONE) {
            viewHolder.retailPriceTextView.setVisibility(View.VISIBLE);
        }

        if (bookInfo.isForSale()) {
            //When the book is FOR SALE in the user's locale, then the prices will be available
            if (bookInfo.isDiscounted()) {
                //When the price is discounted

                //Setting the List Price with the Strikethrough Text
                TextAppearanceUtility.setStrikethroughText(viewHolder.listPriceTextView, bookInfo.getListPrice());
                //Setting the Retail Price
                viewHolder.retailPriceTextView.setText(bookInfo.getRetailPrice());
            } else {
                //When both the List price and Retail price are same

                //Hiding the List price field as there is no discount to be shown
                viewHolder.listPriceTextView.setVisibility(View.GONE);

                //Setting the Retail Price
                viewHolder.retailPriceTextView.setText(bookInfo.getRetailPrice());
            }
        } else {
            //Updating with a default message when the book is NOT FOR SALE

            //Hiding the Retail Price field as the book is NOT FOR SALE
            viewHolder.retailPriceTextView.setVisibility(View.GONE);

            //Setting the List Price field with the default message
            viewHolder.listPriceTextView.setText(context.getString(R.string.not_for_sale_price_text));
        }
        //Updating the Price of the Book : END

        //Populating the data onto the Template View using the BookInfo object: END
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter
     * which is the total number of {@link BookInfo} objects in the adapter
     */
    @Override
    public int getItemCount() {
        return mBookInfoList.size();
    }

    /**
     * Method that computes the difference between the current and the new list of
     * {@link BookInfo} objects and sends the result to the adapter to notify the changes
     * on the item data and reload accordingly
     *
     * @param newBookInfos is the new list of {@link BookInfo} objects which is the Dataset of the Adapter
     */
    public void swapItemData(@NonNull List<BookInfo> newBookInfos) {
        //Loading the List of BookInfo objects as Bundle arguments to be passed to a Loader
        Bundle args = new Bundle();
        args.putParcelableArrayList(OLD_LIST_STR_KEY, (ArrayList<? extends Parcelable>) mBookInfoList);
        args.putParcelableArrayList(NEW_LIST_STR_KEY, (ArrayList<? extends Parcelable>) newBookInfos);
        //Initiating a loader to execute the difference computation in a background thread
        ((FragmentActivity) getContext()).getSupportLoaderManager().restartLoader(BooksDiffLoader.BOOK_DIFF_LOADER, args, this);
    }

    /**
     * Internal Method called by the Loader {@link BooksDiffLoader}
     * after the difference computation between the current and the new list of
     * {@link BookInfo} objects to notify the adapter of the changes required
     * with respect to the data
     *
     * @param diffResult   the result obtained after the difference computation between two sets of
     *                     {@link BookInfo} objects
     * @param newBookInfos is the new list of {@link BookInfo} objects which is the Dataset of the Adapter
     */
    private void doSwapItemData(DiffUtil.DiffResult diffResult, @NonNull List<BookInfo> newBookInfos) {
        Log.d(LOG_TAG, "doSwapItemData: Started");
        //Clearing the Adapter's data to load the new list of BookInfo objects
        mBookInfoList.clear();
        mBookInfoList.addAll(newBookInfos);

        //Informing the adapter about the changes required, so that it triggers the notify accordingly
        diffResult.dispatchUpdatesTo(this);

        //Dispatching the Item Data Swap event to the listener
        mItemDataSwapListener.onItemDataSwapped();
    }

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<DiffUtil.DiffResult> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case BooksDiffLoader.BOOK_DIFF_LOADER:
                //Preparing the Diff Loader and returning the instance
                List<BookInfo> oldBookInfoList = args.getParcelableArrayList(OLD_LIST_STR_KEY);
                List<BookInfo> newBookInfoList = args.getParcelableArrayList(NEW_LIST_STR_KEY);
                return new BooksDiffLoader(getContext(), oldBookInfoList, newBookInfoList);
            default:
                return null;
        }
    }

    /**
     * Called when a previously created loader has finished its load.
     * This is where we notify the Adapter with the result of the difference computation
     *
     * @param loader     The Loader that has finished.
     * @param diffResult The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(@NonNull Loader<DiffUtil.DiffResult> loader, DiffUtil.DiffResult diffResult) {
        if (diffResult != null) {
            //When there is a result of the difference computation
            switch (loader.getId()) {
                case BooksDiffLoader.BOOK_DIFF_LOADER:
                    //Update the New Data to the Adapter and notify the changes in the data
                    doSwapItemData(diffResult, ((BooksDiffLoader) loader).getNewBookInfoList());
                    break;
            }
        }
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(@NonNull Loader<DiffUtil.DiffResult> loader) {
        //No-op, just invalidating the loader
        loader = null;
    }

    /**
     * ViewHolder class for caching View components of the template item view
     */
    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        //Declaring the View components of the template item view
        private ImageView bookImageView;
        private TextView titleTextView;
        private TextView authorTextView;
        private TextView publisherTextView;
        private TextView publishedDateTextView;
        private TextView pagesTextView;
        private TextView categoriesTextView;
        private RatingBar bookRatingBarView;
        private TextView ratingCountTextView;
        private TextView listPriceTextView;
        private TextView retailPriceTextView;

        /**
         * Constructor of the ViewHolder
         *
         * @param itemView is the inflated item layout View passed
         *                 for caching its View components
         */
        ViewHolder(final View itemView) {
            super(itemView);

            //Doing the view lookup for each of the item layout view's components
            bookImageView = itemView.findViewById(R.id.list_item_image_id);
            titleTextView = itemView.findViewById(R.id.list_item_title_text_id);
            authorTextView = itemView.findViewById(R.id.list_item_author_text_id);
            publisherTextView = itemView.findViewById(R.id.list_item_publ_text_id);
            publishedDateTextView = itemView.findViewById(R.id.list_item_publ_date_text_id);
            pagesTextView = itemView.findViewById(R.id.list_item_pages_text_id);
            categoriesTextView = itemView.findViewById(R.id.list_item_categories_text_id);
            bookRatingBarView = itemView.findViewById(R.id.list_item_ratingbar_id);
            ratingCountTextView = itemView.findViewById(R.id.list_item_rating_count_text_id);
            listPriceTextView = itemView.findViewById(R.id.list_item_list_price_text_id);
            retailPriceTextView = itemView.findViewById(R.id.list_item_retail_price_text_id);

            //Setting the Click Listener on the Item View
            itemView.setOnClickListener(this);
        }

        /**
         * Handles the item view being clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            //Retrieving the position of the item view clicked
            int adapterPosition = getAdapterPosition();
            if (adapterPosition > RecyclerView.NO_POSITION) {
                //Verifying the validity of the position before proceeding

                //Propagating the call to the Listener with the selected Item's data
                mItemClickListener.onItemClick(mBookInfoList.get(adapterPosition));
            }
        }
    }
}
