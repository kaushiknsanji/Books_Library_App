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
