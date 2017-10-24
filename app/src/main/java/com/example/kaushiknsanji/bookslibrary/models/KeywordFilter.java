package com.example.kaushiknsanji.bookslibrary.models;

/**
 * Model Class for holding the data of the different Search Keyword Filters
 * available for the Google's Book API
 *
 * @author Kaushik N Sanji
 */
public class KeywordFilter {

    //Stores the Search Keyword filter's Name
    private String mFilterName;

    //Stores the Search Keyword filter's Description
    private String mFilterDesc;

    //Stores the actual value of the Search Keyword Filter
    private String mFilterValue;

    /**
     * Constructor to initialize the {@link KeywordFilter}
     *
     * @param filterName  is a String containing Search Keyword filter's Name
     * @param filterDesc  is a String containing Search Keyword filter's Description
     * @param filterValue is a String containing Search Keyword filter's Value
     */
    public KeywordFilter(String filterName, String filterDesc, String filterValue) {
        this.mFilterName = filterName;
        this.mFilterDesc = filterDesc;
        this.mFilterValue = filterValue;
    }

    /**
     * Getter Method for {@link #mFilterName}
     *
     * @return String containing the value of Search Keyword filter's Name
     */
    public String getFilterName() {
        return mFilterName;
    }

    /**
     * Getter Method for {@link #mFilterDesc}
     *
     * @return String containing the value of Search Keyword filter's Description
     */
    public String getFilterDesc() {
        return mFilterDesc;
    }

    /**
     * Getter Method for {@link #mFilterValue}
     *
     * @return String containing the Search Keyword filter's Value
     */
    public String getFilterValue() {
        return mFilterValue;
    }

}
