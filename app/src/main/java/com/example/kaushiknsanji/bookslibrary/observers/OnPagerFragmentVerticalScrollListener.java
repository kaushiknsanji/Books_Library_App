package com.example.kaushiknsanji.bookslibrary.observers;

/**
 * Interface that declares methods to be implemented by the
 * {@link com.example.kaushiknsanji.bookslibrary.BookSearchActivity}
 * to receive event callbacks related to RecyclerView's scrolling action
 *
 * @author Kaushik N Sanji
 */
public interface OnPagerFragmentVerticalScrollListener {

    /**
     * Method invoked when the ViewPager's scroll has reached
     * the last three items in its Fragment
     * {@link com.example.kaushiknsanji.bookslibrary.adapterviews.RecyclerViewFragment}
     *
     * @param verticalScrollAmount is the amount of vertical scroll.
     *                             <br/>If >0 then scroll is moving towards the bottom;
     *                             <br/>If <0 then scroll is moving towards the top
     */
    void onBottomReached(int verticalScrollAmount);
}
