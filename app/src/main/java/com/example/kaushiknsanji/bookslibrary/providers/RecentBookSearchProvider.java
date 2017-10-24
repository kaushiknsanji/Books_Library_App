package com.example.kaushiknsanji.bookslibrary.providers;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Class for providing Recent Search Suggestions
 *
 * @author Kaushik N Sanji
 */
public class RecentBookSearchProvider extends SearchRecentSuggestionsProvider {

    //Initializing the AUTHORITY constant for the provider
    public static final String AUTHORITY = RecentBookSearchProvider.class.getName();

    //Initializing the Database Mode constant for the provider
    public static final int DATABASE_MODE = DATABASE_MODE_QUERIES;

    //Setting up the Recent Search Suggestions provider in the constructor
    public RecentBookSearchProvider() {
        setupSuggestions(AUTHORITY, DATABASE_MODE);
    }

}
