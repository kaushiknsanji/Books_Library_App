package com.example.kaushiknsanji.bookslibrary.observers;

import com.example.kaushiknsanji.bookslibrary.adapterviews.RecyclerViewFragment;
import com.example.kaushiknsanji.bookslibrary.models.BookInfo;

/**
 * Interface that declares methods to be implemented by the {@link RecyclerViewFragment}
 * displayed in the ViewPager to receive event callbacks related to the click action
 * on the item views displayed by the RecyclerView's Adapter
 *
 * @author Kaushik N Sanji
 */
public interface OnAdapterItemClickListener {

    /**
     * Method invoked when an Item on the Adapter is clicked
     *
     * @param itemBookInfo is the corresponding {@link BookInfo} object of the item view
     *                     clicked in the Adapter
     */
    void onItemClick(BookInfo itemBookInfo);

}
