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
