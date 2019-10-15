# Books Library App

![GitHub](https://img.shields.io/github/license/kaushiknsanji/Books_Library_App)  ![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/kaushiknsanji/Books_Library_App)  ![GitHub repo size](https://img.shields.io/github/repo-size/kaushiknsanji/Books_Library_App)
![GitHub release (latest by date)](https://img.shields.io/github/v/release/kaushiknsanji/Books_Library_App)  ![GitHub All Releases](https://img.shields.io/github/downloads/kaushiknsanji/Books_Library_App/total) ![GitHub search hit counter](https://img.shields.io/github/search/kaushiknsanji/Books_Library_App/Book%20Listing%20App) ![Minimum API level](https://img.shields.io/badge/API-15+-yellow)

This App has been developed as part of the **Udacity Android Basics Nanodegree Course** for the Exercise Project **"Book Listing App"**. App connects to the [Google Books API](https://developers.google.com/books/) to retrieve the list of Books for the search done and then displays them in a BookShelf format.

---

## App Compatibility

Android device running with Android OS 4.0.4 (API Level 15) or above. Best experienced on Android Nougat 7.1 and above. 

---

## Rubric followed for the Project

* Contains a ListView that is populated with list items having information displayed with proper text wrapping.
* List items displays the author and title information.
* ListViews to be populated with properly parsed information, from the JSON Response.
* For the user to search the desired book, the screen has a search box (SearchView) that accepts a word or phrase.
* App should fetch the query related books via an HTTP request from Google Books API, using a class such as HttpUriRequest or HttpURLConnection.
* All network calls should occur off the UI Thread by making use of AsyncTask or similar threading object.
* On device rotation
	* The layout remains scrollable.
	* The app saves state and restores the list back to the previously scrolled position.
	* The UI properly adjusts so that all contents of each list item is still visible and not truncated/overlapped.
	* The Search button remains visible on the screen even after the device is rotated.
* Checks whether connected to internet or not. Also, validates for any occurrence of bad server response or lack of response.
* No external libraries to be used.
* A Default message is shown when there is no data to display for the search query made.
* A Default text is shown for guiding the user, on what to enter while searching.

---

### Things explored/developed in addition to the above defined Rubric

* Assisted Search Implementation with `SearchView`.
* Used `RecyclerView` in place of `ListView` and `GridView` for its advantages in performance and easy placeholders for custom item decoration.
* Custom `RecyclerView.ItemDecoration` for decorating each of the items in List/Grid with the Book shelf decoration.
* Explored `FragmentStatePagerAdapter` that displays the Fragments \(retaining their state\) for the `ViewPager`.
* Implemented `android.support.v7.preference.Preference` Preferences for the Settings.
* No external libraries are used for communicating with the REST API and also for loading the images. `AsyncTaskLoader` has been used for downloading the data and images in the background thread.
* Most layouts are designed using `ConstraintLayout` to flatten the layout hierachy as far as possible.
* Indeterminate progress bar is implemented with animation-list / AnimationDrawable.
* Spannables for decorating `TextViews` - [TextAppearanceUtility](/app/src/main/java/com/example/kaushiknsanji/bookslibrary/utils/TextAppearanceUtility.java) with strikethrough, image within text, selective text coloring and relative text resize.
* Custom Fonts for `TextViews`.
* `CardView` for displaying the information of a Book.

---

## Design and Implementation of the App

<!-- Video of the App -->
[![Video of Complete App Flow](https://i.ytimg.com/vi/deXm1yzqRmU/maxresdefault.jpg)](https://youtu.be/deXm1yzqRmU)

### The Welcome screen layout

The first screen displayed when the app is launched, is the welcome page screen as shown below. 

<!-- Image for Welcome page -->
<img src="https://user-images.githubusercontent.com/26028981/32066973-292b2430-ba9f-11e7-8650-096d1d818fb0.png" width="40%" />

This [layout](/app/src/main/res/layout/welcome_page.xml) is inflated by the [BookSearchActivity](/app/src/main/java/com/example/kaushiknsanji/bookslibrary/BookSearchActivity.java) and is included in the [activity_main.xml](/app/src/main/res/layout/activity_main.xml). This is visible only for the first time when the app is launched and loaded into memory. This screen basically tells the user on how to search for a book.

### Assisted Search

The SearchView in the action menu is implemented with Assisted Search. Hence this activity also becomes the Searchable activity with the searchable configuration as defined [here](/app/src/main/res/xml/searchable.xml). As seen in the Searchable configuration, a [Recent Search Suggestions Provider](/app/src/main/java/com/example/kaushiknsanji/bookslibrary/providers/RecentBookSearchProvider.java) is also implemented and is displayed when the user types in atleast 3 characters in the search to show the corresponding match, provided if there were any recent search with those 3 characters.

<!-- Image for Assisted Search -->
<img src="https://user-images.githubusercontent.com/26028981/32066984-36ffac5c-ba9f-11e7-9698-575374ee48ca.png" width="40%" />

When the SearchView is expanded, user can opt to search by Voice as well. But this will not allow the user to make any modifications as the transcribed text is directly fed to the search box and executed in one shot. Any search that is done, is updated to the title as shown below.

<!-- Image for Completed Search -->
<img src="https://user-images.githubusercontent.com/26028981/32066997-441ecb3e-ba9f-11e7-9fb8-33e0467eaa93.png" width="40%" />

One can clear the recent search history by simply going to the overflow menu at the top and select the menu option that says **"Clear Search History"**. On success of this action, a toast will be displayed.

<!-- Image for "Clear Search History" -->
<img src="https://user-images.githubusercontent.com/26028981/32067002-4a8bbdb0-ba9f-11e7-98f8-8423f994f0fa.png" width="40%" />

### Displaying Results

Once the search is entered by the user, the welcome screen if active is replaced with a `ViewPager` and its `Fragments` to display the results. The ViewPager will be set to show its first Tab by default, which is the LIST Tab. In this the results are displayed like in a vertical list with list of Books related to the search. The other Tab is the GRID Tab which allows the user to view the results in a Staggered Grid View (of 2 columns).

<!-- Images for the List and Grid Views -->
<img src="https://user-images.githubusercontent.com/26028981/32067013-51ad2cf0-ba9f-11e7-9d75-acec8788c4ed.png" width="40%" />     <img src="https://user-images.githubusercontent.com/26028981/32067015-52eeab34-ba9f-11e7-846f-08eb83a8a1bd.png" width="40%" />

Adapter used by ViewPager is an extension of [FragmentStatePagerAdapter](/app/src/main/java/com/example/kaushiknsanji/bookslibrary/adapters/DisplayPagerAdapter.java) whose Fragments for List & Grid view is given by the [RecyclerViewFragment](/app/src/main/java/com/example/kaushiknsanji/bookslibrary/adapterviews/RecyclerViewFragment.java), which uses `RecyclerView` instead of `ListView` or `GridView`. 
* For List View
  - The `RecyclerView` uses a `LinearLayoutManager` as its layout manager and an adapter [RecyclerListAdapter](/app/src/main/java/com/example/kaushiknsanji/bookslibrary/adapters/RecyclerListAdapter.java) to populate the List.
* For Grid View
  - The `RecyclerView` uses a `StaggeredGridLayoutManager` as its layout manager and an adapter [RecyclerGridAdapter](/app/src/main/java/com/example/kaushiknsanji/bookslibrary/adapters/RecyclerGridAdapter.java) to populate the Grid. 
	
For both of the layout managers used, an item decoration is added defined by the [BookShelfItemDecoration](https://github.com/kaushiknsanji/Books_Library_App/blob/0c15b06877ca29523a588b67f30431f4acfaed37/app/src/main/java/com/example/kaushiknsanji/bookslibrary/adapterviews/RecyclerViewFragment.java#L362-L445). This basically adds a book shelf decoration just below the item.

Both the above adapters registers an item click listener whose interface callbacks are defined by [OnAdapterItemClickListener](/app/src/main/java/com/example/kaushiknsanji/bookslibrary/observers/OnAdapterItemClickListener.java).

The `RecyclerViewFragment` registers an `OnScrollListener` to propagate the corresponding event to the `BookSearchActivity`. This is implemented by extending `RecyclerView.OnScrollListener` which is done by the class [BaseRecyclerViewScrollListener](/app/src/main/java/com/example/kaushiknsanji/bookslibrary/observers/BaseRecyclerViewScrollListener.java). This is an abstract class which provides an event callback when the scroll reaches the last 3 items in the list, to reveal the pagination buttons for the user to navigate to different pages in the current search, as shown below. The `RecyclerViewFragment` registers a concrete implementation of this class which is [RecyclerViewScrollListener](https://github.com/kaushiknsanji/Books_Library_App/blob/0c15b06877ca29523a588b67f30431f4acfaed37/app/src/main/java/com/example/kaushiknsanji/bookslibrary/adapterviews/RecyclerViewFragment.java#L447-L466) to propagate the event to the `BookSearchActivity`, which reveals the hidden pagination panel (`R.id.pagination_panel_id`).
	
<!-- GIF for Pagination -->
![pagination](https://user-images.githubusercontent.com/26028981/32455240-a2d8f5d6-c347-11e7-87cc-a0c2b2b5ecfd.gif)

### The Details Page

Clicking on an item in the List Tab View or the Grid Tab View, opens up the Details page [activity_book_detail.xml](/app/src/main/res/layout/activity_book_detail.xml) inflated by the activity [BookDetailActivity](/app/src/main/java/com/example/kaushiknsanji/bookslibrary/BookDetailActivity.java). This displays additional information such as 
* The book description
* Sample previews of the Book, that basically takes the user to a link via an Intent to the browser.
* Information link when no previews present and 
* A button that takes the user to a page for buying a book if the book is saleable in the user's region.

<!-- Image for Book Details including landscape -->
<img src="https://user-images.githubusercontent.com/26028981/32067039-616fb482-ba9f-11e7-9afc-598898c59640.png" width="40%" />   <img src="https://user-images.githubusercontent.com/26028981/32067041-62717ef6-ba9f-11e7-8e36-6b20f02ff66f.png" width="40%" />
<img src="https://user-images.githubusercontent.com/26028981/32067043-63b44906-ba9f-11e7-9107-78296ce868ff.png" width="70%" />

### The Book Image Page

If the Image of the Book is loaded in the Details page `activity_book_detail.xml`, then the user can click on the image to open a larger image of the same, which is displayed by the activity [BookImageActivity](/app/src/main/java/com/example/kaushiknsanji/bookslibrary/BookImageActivity.java)

<!-- Image for Book Image Activity -->
<img src="https://user-images.githubusercontent.com/26028981/32067050-68559410-ba9f-11e7-9250-c19f23cf3762.png" width="40%" />

### Controlling the Search Results

Search Results can be controlled by varying certain parameters embedded in the search query. This is defined by the [Google Books API](https://developers.google.com/books/docs/v1/using#WorkingVolumes) which this application is based on and is acting as a client that receives the data for the search executed. 

Most of these parameters can be altered by using the **"Search Settings"** menu item in the `BookSearchActivity`. The **"Search Settings"** menu item, opens up the preferences [preferences.xml](/app/src/main/res/xml/preferences.xml) loaded by the activity [SearchSettingsActivity](/app/src/main/java/com/example/kaushiknsanji/bookslibrary/settings/SearchSettingsActivity.java). These preferences provides options to override the search parameters used in the query, and the following are the ones that can changed -
* `filter` parameter which restricts the results returned to the kind of books we want like Partially viewable/Free ebooks/Paid ebooks and so on.
* `printType` parameter which restricts the results returned to specific print or publication type.
* `orderBy` parameter that defines the sorting order of the results.
* Pagination parameters like
  - `startIndex` parameter that tells the page index to be shown.
  - `maxResults` parameters that tells the max number of results to be shown per page.
  
<!-- Image for Settings -->
<img src="https://user-images.githubusercontent.com/26028981/32067053-6ddcc7aa-ba9f-11e7-9d56-454123d40f64.png" width="40%" />

The search settings implements the `android.support.v7.preference.Preference` Preferences. As such all the values are stored in the default `SharedPreferences`. For the Pagination parameters, custom [NumberPicker](/app/src/main/java/com/example/kaushiknsanji/bookslibrary/settings/NumberPickerPreference.java) Preference has been implemented by extending the `DialogPreference`.

There is also another preference setting provided, which resets all the preferences \(used in the search\) to their default values. This is implemented by [ConfirmationPreference](/app/src/main/java/com/example/kaushiknsanji/bookslibrary/settings/ConfirmationPreference.java)

When the user returns to the `BookSearchActivity` after making the required changes in the **"Search Settings"**, the search query will be rebuilt and the results will be loaded accordingly. For this to happen, `BookSearchActivity` implements the Listener `SharedPreferences.OnSharedPreferenceChangeListener`.

While searching, one can also filter the results by entering keyword prefixes into the search box, specifically to search only in that field of the output. Following are the parameters that cater to this category - 
* `intitle`  - Returns results where the text following this keyword is found in the title.
* `inauthor`  - Returns results where the text following this keyword is found in the author.
* `inpublisher` - Returns results where the text following this keyword is found in the publisher.
* `subject` - Returns results where the text following this keyword is listed in the category list of the volume.

The above is **NOT** provided by the **"Search Settings"** menu item, instead it can be accessed by another menu item that says **"Search Keyword Filters"**. This shows a dialog with a list of cards specific to each of the above mentioned prefixes, which is displayed by the `DialogFragment` [KeywordFiltersDialogFragment](/app/src/main/java/com/example/kaushiknsanji/bookslibrary/adapterviews/KeywordFiltersDialogFragment.java). Selecting any of these cards, will insert the prefix into the search box. Any word/phrase typed after this, will be searched in that particular field of the results.

<!-- GIF for Keywords -->
![keyword_filter](https://user-images.githubusercontent.com/26028981/32455259-b51a0a5a-c347-11e7-90ef-a3eda68006b8.gif)

### Loading of Data

Loading of Data for the Book Search done is carried out in a background thread using [BooksLoader](/app/src/main/java/com/example/kaushiknsanji/bookslibrary/workers/BooksLoader.java) that extends an `AsyncTaskLoader`. Request and response is carried out via a REST call to the Google Books API. The data format used for communication is the JSON format. Talking to the REST API and parsing the response is done by the utility code [BookClientUtility](/app/src/main/java/com/example/kaushiknsanji/bookslibrary/utils/BookClientUtility.java).

_As per the Rubric, no third party library is used for communicating with the REST API._

### Loading of Images

Loading of Images for each of the items in the list/grid views, in the Details page and the Book Image page is carried out in a background thread through a viewless Fragment [ImageDownloaderFragment](/app/src/main/java/com/example/kaushiknsanji/bookslibrary/workers/ImageDownloaderFragment.java). Functioning of this fragment is as follows -
* It first checks whether the image to be loaded is present in the Bitmap Cache, implemented by [BitmapImageCache](/app/src/main/java/com/example/kaushiknsanji/bookslibrary/cache/BitmapImageCache.java)
* If present in the cache, it updates the image to the corresponding `ImageView` passed.
* If not present in cache, then the image is downloaded in a background thread using [ImageDownloader](/app/src/main/java/com/example/kaushiknsanji/bookslibrary/workers/ImageDownloader.java) that extends an `AsyncTaskLoader`. Once successfully downloaded, it updates the image to the corresponding `ImageView` passed.

The identifier for the Fragment and its Loader is maintained to be unique to the item being displayed in the List/Grid View such that each item displays the correct image to be shown, without resulting in any duplication. If duplication still happens, the loader will take care of loading the correct one when the item is being displayed the second time in the screen.

_As per the Rubric, no third party library is used for loading images._

### Information in general, on the entire app

* After scrolling to some extent in the List Tab View or the Grid Tab View
  - If the user swipes across the tabs, then the Fragment being displayed will scroll to the item last shown in the previous tab.
  - If the user selects the active tab again, user will be taken to the top item \(0th item) in the Fragment.

* If the search entered by the user does not yield any result, then the `BookSearchActivity` will display the hidden embedded page [no_result_page.xml](/app/src/main/res/layout/no_result_page.xml).

<!-- Image for No Result page -->
<img src="https://user-images.githubusercontent.com/26028981/32067068-8176121c-ba9f-11e7-82b8-fe68ec9e3cfe.png" width="40%" />

* If during search, that is while initiating a request to the REST API, a network connectivity issue is encountered, then a Network Error dialog is displayed for the user to take action accordingly. The dialog is implemented by the `DialogFragment` [NetworkErrorDialogFragment](/app/src/main/java/com/example/kaushiknsanji/bookslibrary/dialogs/NetworkErrorDialogFragment.java) that inflates the layout [network_error_dialog.xml](/app/src/main/res/layout/network_error_dialog.xml).

<!-- Image for Network Error dialog -->
<img src="https://user-images.githubusercontent.com/26028981/32067075-850b5b94-ba9f-11e7-8ac0-278970937c24.png" width="40%" />

* If the response fails with any HTTP error, then this will be consumed silently and the search will not happen. Instead, the previous search results continues to be displayed.
* During the loading of books data, a custom [progress bar](/app/src/main/res/drawable/progress_bar_indeterminate.xml) will be displayed in the `BookSearchActivity`. This is implemented using the `animation-list / AnimationDrawable`.

### The About Page

This can be viewed by going into the menu item **"About"** in the overflow menu of the `BookSearchActivity`. This page describes in brief about the app, and has links to my bio and the course details hosted by Udacity. This is shown by the activity [AboutActivity](/app/src/main/java/com/example/kaushiknsanji/bookslibrary/AboutActivity.java) that inflates the layout [activity_about.xml](/app/src/main/res/layout/activity_about.xml).

<!-- Image for About page -->
<img src="https://user-images.githubusercontent.com/26028981/32067085-8f891480-ba9f-11e7-96af-49210c1fe280.png" width="40%" />

---

## Bug Fixes and other important changes

* Custom Preferences [NumberPickerPrefence](app/src/main/java/com/example/kaushiknsanji/bookslibrary/settings/NumberPickerPreference.java) and the [ConfirmationPreference](app/src/main/java/com/example/kaushiknsanji/bookslibrary/settings/ConfirmationPreference.java) have been modified to fix the [Settings Screen crash issue](https://github.com/kaushiknsanji/Books_Library_App/issues/3) noticed. 
* The [No Result](app/src/main/res/layout/no_result_page.xml) page and the [Welcome](app/src/main/res/layout/welcome_page.xml) page layouts had issues related ConstraintLayouts used, noticed in some devices running on Android 5. Screen elements were either getting clipped or at times used to disappear on rotation.
* The Title Text in the details screen was not displaying the Ellipse characters for content that exceeded its MaxLines set. This was happening due to the Spannable being used for resizing the Subtitle part of the Title when it was present. This was fixed by employing a `SpannableStringBuilder` to do the same and finalized the text on `TextView` with `BufferType` as `NORMAL`, that enables content modification, eg., ellipse can be applied later. Changes can be found in the [issue](https://github.com/kaushiknsanji/Books_Library_App/issues/6).
* The Title and Author Text in the details screen can at times be very large for some books, which in turn renders the scrollable content (following the Author Text) inaccessible. This has been fixed as part of the [issue](https://github.com/kaushiknsanji/Books_Library_App/issues/5). The TextViews were basically embedded in a `NestedScrollView` of a fixed height (managed dynamically in the [BookDetailActivity](app/src/main/java/com/example/kaushiknsanji/bookslibrary/BookDetailActivity.java) code). These were enabled for expand/collapse whenever the content in them exceeded their MaxLines set, yielding to ellipsis in the end. A `ViewTreeObserver` was employed to monitor for large content(that exceeded MaxLines) during the layout process that enables for expand/collapse of the TextViews. As an indicator, image anchors are placed against these TextViews when they can be expanded/collapsed. 

**Following are the images for a Book having a long Title Text**

<img src="https://user-images.githubusercontent.com/26028981/33951543-401e9a38-e055-11e7-8686-55324da2aaff.png" width="40%" />   <img src="https://user-images.githubusercontent.com/26028981/33951550-44200d06-e055-11e7-9108-b7ea293bddf6.png" width="40%" />

**The same in landscape**

<img src="https://user-images.githubusercontent.com/26028981/33951564-4bb56e1c-e055-11e7-8499-aaffe991f515.png" width="70%" />   <img src="https://user-images.githubusercontent.com/26028981/33951567-4d1fbadc-e055-11e7-8f38-8272613c1b79.png" width="70%" />

**Sample image for a Book having long Title and Author Text**

<img src="https://user-images.githubusercontent.com/26028981/33951592-65185766-e055-11e7-9ccb-2aaff1915925.png" width="40%" />

---

## Review from the Reviewer (Udacity)

![Review_Book_Listing_App](https://user-images.githubusercontent.com/26028981/66827627-b7c29580-ef6c-11e9-9ff5-462488f109fa.PNG)

---

## License

```
Copyright 2017 Kaushik N. Sanji

Licensed under the Apache License, Version 2.0 (the "License"); 
you may not use this file except in compliance with the License. 
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0
   
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
