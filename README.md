# Books Library App - v1.0

![GitHub](https://img.shields.io/github/license/kaushiknsanji/Books_Library_App) ![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/kaushiknsanji/Books_Library_App) ![GitHub repo size](https://img.shields.io/github/repo-size/kaushiknsanji/Books_Library_App) ![GitHub Releases](https://img.shields.io/github/downloads/kaushiknsanji/Books_Library_App/v1.0/total)

This is the Release version 1.0 of the **Books Library** App.

## Changes done in this Release

* Used `ConstrainedWidth` to enforce `WRAP_CONTENT` constraints for Item Views shown by `BookSearchActivity` ([commit](https://github.com/kaushiknsanji/Books_Library_App/commit/da3ef414f2604b57147413936466c00d6715f68b)) and certain other views shown by `BookDetailActivity`([commit](https://github.com/kaushiknsanji/Books_Library_App/commit/afb88281cd21723ca2d0c9475d8d7dd2352354d2)).
* Removed unnecessary NestedScrollViews for Expandable TextViews shown by `BookDetailActivity`. Also, used NestedScrollView instead of ScrollView for the scrollable content following the Book Title and Author in the Portrait mode - ([commit](https://github.com/kaushiknsanji/Books_Library_App/commit/afb88281cd21723ca2d0c9475d8d7dd2352354d2)).
* Added Click Listeners to Expand/Collapse Image Anchors shown by `BookDetailActivity` - ([commit](https://github.com/kaushiknsanji/Books_Library_App/commit/0cdcd0c81aef463c23f431908eced4c15b60d1be)).
* Moved registration of Adapter Item Click listener to the `BookSearchActivity` (from `RecyclerViewFragment`) so that we can save the position of the Item clicked, when launching the `BookDetailActivity`, which prevents the scroll to top when navigating back from the BookDetailActivity - ([commit](https://github.com/kaushiknsanji/Books_Library_App/commit/4ca2979c08e1c64b84541d4a9b4799b93f8b7875)).
* Modified the dispatch order of Diff updates to the Adapter by dispatching the updates to the Adapter first and then clearing the Adapter's old data to load the new list - ([commit](https://github.com/kaushiknsanji/Books_Library_App/commit/46ef6abe92ec7ae23c6a49ddecedd29c5bfb5d48)).
* "Handling of Null Image links", and "Reading and Writing Bitmaps to Memory cache" is now taken care by the `ImageDownloader` AsyncTaskLoader - ([commit](https://github.com/kaushiknsanji/Books_Library_App/commit/30f8dcc3ad655240d13da8b2b3faa54e39f83224)).
* Modified DiffUtil to issue Payload only for the required changes, and the Adapters to bind the data changes from the Payload - ([commit](https://github.com/kaushiknsanji/Books_Library_App/commit/d5adfe55ec41b79c27fc8e34d58ede1c3e3a379a)).
* Bitmap Memory Cache is cleared on App Exit as well - ([commit](https://github.com/kaushiknsanji/Books_Library_App/commit/a01331b418d28c27fb3e9cf5025264e6b5a948ab)).
* Enabled logging for debuggable build types only, through the use of custom [Logger](https://github.com/kaushiknsanji/Books_Library_App/blob/release_v1.0/app/src/main/java/com/example/kaushiknsanji/bookslibrary/utils/Logger.java) which is a wrapper to the `android.util.Log` - ([commit](https://github.com/kaushiknsanji/Books_Library_App/commit/3489e5a479e08064e155f29f2659b3cf59038ba1)).
* Fixed the layout clipping of [Page NumberPicker Dialog](https://github.com/kaushiknsanji/Books_Library_App/commit/5cc33d1c4b6c630f64f6d4714672045ccb1d3399) and [NetworkError Dialog](https://github.com/kaushiknsanji/Books_Library_App/commit/5377b870230982cffdd12497b3ce330a5b9d5e9f).


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
