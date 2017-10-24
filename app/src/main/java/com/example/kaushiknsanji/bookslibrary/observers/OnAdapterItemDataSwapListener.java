package com.example.kaushiknsanji.bookslibrary.observers;

/**
 * Interface that declares methods to be implemented by the
 * {@link com.example.kaushiknsanji.bookslibrary.BookSearchActivity}
 * to receive event callbacks related to RecyclerView's Adapter data change
 *
 * @author Kaushik N Sanji
 */
public interface OnAdapterItemDataSwapListener {

    /**
     * Method invoked when the data on the RecyclerView's Adapter has been swapped successfully
     */
    void onItemDataSwapped();
}
