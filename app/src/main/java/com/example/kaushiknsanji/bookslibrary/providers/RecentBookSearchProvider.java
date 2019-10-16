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
