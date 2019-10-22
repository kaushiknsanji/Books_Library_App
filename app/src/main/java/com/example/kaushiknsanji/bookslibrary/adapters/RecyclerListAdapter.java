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
import android.content.res.Resources;
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

import java.util.ArrayList;
import java.util.List;

import static com.example.kaushiknsanji.bookslibrary.utils.BooksDiffUtility.PAYLOAD_BOOK_AUTHORS_STR_KEY;
import static com.example.kaushiknsanji.bookslibrary.utils.BooksDiffUtility.PAYLOAD_BOOK_CATEGORIES_STR_KEY;
import static com.example.kaushiknsanji.bookslibrary.utils.BooksDiffUtility.PAYLOAD_BOOK_DISCOUNTED_BOOL_KEY;
import static com.example.kaushiknsanji.bookslibrary.utils.BooksDiffUtility.PAYLOAD_BOOK_FOR_SALE_BOOL_KEY;
import static com.example.kaushiknsanji.bookslibrary.utils.BooksDiffUtility.PAYLOAD_BOOK_IMAGE_LINK_STR_KEY;
import static com.example.kaushiknsanji.bookslibrary.utils.BooksDiffUtility.PAYLOAD_BOOK_LIST_PRICE_STR_KEY;
import static com.example.kaushiknsanji.bookslibrary.utils.BooksDiffUtility.PAYLOAD_BOOK_PAGE_COUNT_INT_KEY;
import static com.example.kaushiknsanji.bookslibrary.utils.BooksDiffUtility.PAYLOAD_BOOK_PUBLISHED_DATE_STR_KEY;
import static com.example.kaushiknsanji.bookslibrary.utils.BooksDiffUtility.PAYLOAD_BOOK_PUBLISHER_STR_KEY;
import static com.example.kaushiknsanji.bookslibrary.utils.BooksDiffUtility.PAYLOAD_BOOK_RATING_COUNT_STR_KEY;
import static com.example.kaushiknsanji.bookslibrary.utils.BooksDiffUtility.PAYLOAD_BOOK_RATING_FLOAT_KEY;
import static com.example.kaushiknsanji.bookslibrary.utils.BooksDiffUtility.PAYLOAD_BOOK_RETAIL_PRICE_STR_KEY;
import static com.example.kaushiknsanji.bookslibrary.utils.BooksDiffUtility.PAYLOAD_BOOK_TITLE_STR_KEY;
import static com.example.kaushiknsanji.bookslibrary.utils.BooksDiffUtility.PAYLOAD_BOOK_TYPE_STR_KEY;

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
        mTitleTextTypeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/garamond_bold.ttf");
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(mLayoutRes, parent, false);

        //Instantiating the ViewHolder to initialize the reference to the view components in the item layout
        //and returning the same
        return new ViewHolder(itemView);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *                   item at the given position in the data set.
     * @param position   The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Retrieving the BookInfo object at the current item position
        BookInfo bookInfo = mBookInfoList.get(position);

        //Binding the item view with the data at the position
        holder.bind(position, bookInfo);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method
     * should update the contents of the {@link ViewHolder#itemView} to reflect the item at
     * the given position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     * @param payloads A non-null list of merged payloads. Can be empty list if requires full
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            //Calling to Super when there is no payload
            super.onBindViewHolder(holder, position, payloads);
        } else {
            //When Payloads are present

            //Retrieving the Bundle from the payload
            Bundle bundle = (Bundle) payloads.get(0);

            //Getting the Resources instance
            Resources resources = holder.itemView.getResources();

            for (String keyStr : bundle.keySet()) {
                switch (keyStr) {
                    case PAYLOAD_BOOK_IMAGE_LINK_STR_KEY:
                        Log.d(LOG_TAG, "onBindViewHolder: Payload - image: " + bundle.getString(keyStr));
                        Log.d(LOG_TAG, "onBindViewHolder: Payload - position: " + position);
                        //Updating the Image
                        holder.updateBookImage(bundle.getString(keyStr), position);
                        break;
                    case PAYLOAD_BOOK_TITLE_STR_KEY:
                        //Updating the Title
                        holder.updateBookTitle(bundle.getString(keyStr));
                        break;
                    case PAYLOAD_BOOK_AUTHORS_STR_KEY:
                        //Updating the Authors
                        String authors = bundle.getString(keyStr);
                        holder.updateBookAuthors(
                                TextUtils.isEmpty(authors) ? resources.getString(R.string.no_authors_found_default_text) : authors
                        );
                        break;
                    case PAYLOAD_BOOK_PUBLISHER_STR_KEY:
                        //Updating the Publisher
                        String publisher = bundle.getString(keyStr);
                        holder.updateBookPublisher(
                                TextUtils.isEmpty(publisher) ? resources.getString(R.string.no_publisher_found_default_text) : publisher
                        );
                        break;
                    case PAYLOAD_BOOK_PUBLISHED_DATE_STR_KEY:
                        //Updating the Published Date
                        String publishedDate = bundle.getString(keyStr);
                        holder.updateBookPublishedDate(
                                TextUtils.isEmpty(publishedDate) ? resources.getString(R.string.no_published_date_default_text) : publishedDate
                        );
                        break;
                    case PAYLOAD_BOOK_PAGE_COUNT_INT_KEY:
                        //Updating the Count of Pages and the Book Type
                        holder.updateBookPageCountType(
                                bundle.getInt(keyStr),
                                bundle.getString(PAYLOAD_BOOK_TYPE_STR_KEY)
                        );
                        break;
                    case PAYLOAD_BOOK_CATEGORIES_STR_KEY:
                        //Updating the Categories
                        String categories = bundle.getString(keyStr);
                        holder.updateBookCategories(
                                TextUtils.isEmpty(categories) ? resources.getString(R.string.no_categories_found_default_text) : categories
                        );
                        break;
                    case PAYLOAD_BOOK_RATING_FLOAT_KEY:
                        //Updating the Book's Ratings
                        holder.updateBookRatings(bundle.getFloat(keyStr));
                        break;
                    case PAYLOAD_BOOK_RATING_COUNT_STR_KEY:
                        //Updating the Count of Ratings
                        holder.updateBookRatingCount(bundle.getString(keyStr));
                        break;
                    case PAYLOAD_BOOK_FOR_SALE_BOOL_KEY:
                        //Updating the Price of the Book
                        holder.updateBookPrice(
                                bundle.getBoolean(keyStr),
                                bundle.getBoolean(PAYLOAD_BOOK_DISCOUNTED_BOOL_KEY),
                                bundle.getString(PAYLOAD_BOOK_LIST_PRICE_STR_KEY),
                                bundle.getString(PAYLOAD_BOOK_RETAIL_PRICE_STR_KEY)
                        );
                        break;
                }
            }
        }
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
        //Informing the adapter about the changes required, so that it triggers the notify accordingly
        diffResult.dispatchUpdatesTo(this);

        //Clearing the Adapter's data to load the new list of BookInfo objects
        mBookInfoList.clear();
        mBookInfoList.addAll(newBookInfos);

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
                mItemClickListener.onItemClick(mBookInfoList.get(adapterPosition), adapterPosition);
            }
        }

        /**
         * Method that binds the views with the data at the position {@code bookInfo}
         *
         * @param position The position of the Item in the list
         * @param bookInfo The {@link BookInfo} data of the Book item at the {@code position}
         */
        void bind(int position, BookInfo bookInfo) {
            Log.d(LOG_TAG, "bind: position " + position);
            //Retrieving the Resources instance
            Resources resources = itemView.getResources();

            //Updating the Book Image if link is present
            updateBookImage(bookInfo.getImageLinkForItemInfo(), position);

            //Updating the Title
            updateBookTitle(bookInfo.getTitle());

            //Updating the Authors
            updateBookAuthors(bookInfo.getAuthors(resources.getString(R.string.no_authors_found_default_text)));

            //Updating the Publisher
            updateBookPublisher(bookInfo.getPublisher(resources.getString(R.string.no_publisher_found_default_text)));

            //Updating the Published Date
            updateBookPublishedDate(bookInfo.getPublishedDateInMediumFormat(resources.getString(R.string.no_published_date_default_text)));

            //Updating the Count of Pages and the Book Type
            updateBookPageCountType(bookInfo.getPageCount(), bookInfo.getBookType());

            //Updating the Categories
            updateBookCategories(bookInfo.getCategories(resources.getString(R.string.no_categories_found_default_text)));

            //Updating the Book's Ratings
            updateBookRatings(bookInfo.getBookRatings());

            //Updating the Count of Ratings
            updateBookRatingCount(bookInfo.getBookRatingCount());

            //Updating the Price of the Book
            updateBookPrice(bookInfo.isForSale(), bookInfo.isDiscounted(), bookInfo.getListPrice(), bookInfo.getRetailPrice());
        }

        /**
         * Method that updates the Book Image if link is present.
         *
         * @param imageLinkForItemInfo The Link for the Image to download the Bitmap from
         * @param position             The position of the Book Item in the list
         */
        void updateBookImage(String imageLinkForItemInfo, int position) {
            Log.d(LOG_TAG, "updateBookImage: Link: " + imageLinkForItemInfo + " position: " + position);
            ImageDownloaderFragment
                    .newInstance(((FragmentActivity) bookImageView.getContext()).getSupportFragmentManager(), position)
                    .executeAndUpdate(bookImageView,
                            imageLinkForItemInfo,
                            position);
        }

        /**
         * Method that updates the Book Title.
         *
         * @param title The Title of the Book
         */
        void updateBookTitle(String title) {
            //Setting the Title
            titleTextView.setText(title);
            //Setting the Title font
            titleTextView.setTypeface(mTitleTextTypeface);
        }

        /**
         * Method that updates the Book's Authors.
         *
         * @param authors The String list of Book's Authors
         */
        void updateBookAuthors(String authors) {
            authorTextView.setText(authors);
        }

        /**
         * Method that updates the publisher of the Book.
         *
         * @param publisher The Publisher of the Book
         */
        void updateBookPublisher(String publisher) {
            publisherTextView.setText(publisher);
        }

        /**
         * Method that updates the Published Date of the Book.
         *
         * @param publishedDateStr The Published Date of the Book
         */
        void updateBookPublishedDate(String publishedDateStr) {
            publishedDateTextView.setText(publishedDateStr);
        }

        /**
         * Method that updates the Number of Pages and the Type of Book.
         *
         * @param pageCount The Number of Pages of the Book
         * @param bookType  The Type of Book which says whether the Book is a Magazine or a Book
         */
        void updateBookPageCountType(int pageCount, String bookType) {
            pagesTextView.setText(itemView.getResources().getString(R.string.pages_book_type, pageCount, bookType));
        }

        /**
         * Method that updates the Categories of the Book.
         *
         * @param categories The String list of Categories of the Book
         */
        void updateBookCategories(String categories) {
            categoriesTextView.setText(categories);
        }

        /**
         * Method that updates the Book's Ratings.
         *
         * @param bookRatings The Float value of the Book's ratings
         */
        void updateBookRatings(float bookRatings) {
            bookRatingBarView.setRating(bookRatings);
        }

        /**
         * Method that updates the Count of Ratings the Book has received.
         *
         * @param bookRatingCount String value of the Count of Ratings the Book has received
         */
        void updateBookRatingCount(String bookRatingCount) {
            ratingCountTextView.setText(bookRatingCount);
        }

        /**
         * Method that updates the Price of the Book and its saleability.
         *
         * @param isForSale    Boolean that says whether the Book is For Sale or not in the User's Locale
         * @param isDiscounted Boolean that says whether the Book price is discounted or not
         * @param listPrice    String value of the List Price of the Book
         * @param retailPrice  String value of the Retail Price of the Book
         */
        void updateBookPrice(boolean isForSale, boolean isDiscounted, String listPrice, String retailPrice) {
            //Ensuring that the view components are visible
            if (listPriceTextView.getVisibility() == View.GONE) {
                listPriceTextView.setVisibility(View.VISIBLE);
            }
            if (retailPriceTextView.getVisibility() == View.GONE) {
                retailPriceTextView.setVisibility(View.VISIBLE);
            }

            if (isForSale) {
                //When the book is FOR SALE in the user's locale, then the prices will be available
                if (isDiscounted) {
                    //When the price is discounted

                    //Setting the List Price with the Strikethrough Text
                    TextAppearanceUtility.setStrikethroughText(listPriceTextView, listPrice);
                    //Setting the Retail Price
                    retailPriceTextView.setText(retailPrice);
                } else {
                    //When both the List price and Retail price are same

                    //Hiding the List price field as there is no discount to be shown
                    listPriceTextView.setVisibility(View.GONE);

                    //Setting the Retail Price
                    retailPriceTextView.setText(retailPrice);
                }
            } else {
                //Updating with a default message when the book is NOT FOR SALE

                //Hiding the Retail Price field as the book is NOT FOR SALE
                retailPriceTextView.setVisibility(View.GONE);

                //Setting the List Price field with the default message
                listPriceTextView.setText(itemView.getResources().getString(R.string.not_for_sale_price_text));
            }
        }

    }
}
