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

package com.example.kaushiknsanji.bookslibrary;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaushiknsanji.bookslibrary.models.BookInfo;
import com.example.kaushiknsanji.bookslibrary.utils.TextAppearanceUtility;
import com.example.kaushiknsanji.bookslibrary.workers.ImageDownloaderFragment;

import java.text.ParseException;

/**
 * Activity class that displays the {@link BookInfo} Item data selected in the List/Grid View.
 * The Item data is received via an Intent and the corresponding view components in the
 * layout 'R.layout.activity_book_detail' are updated accordingly
 *
 * @author Kaushik N Sanji
 */
public class BookDetailActivity extends AppCompatActivity
        implements View.OnClickListener, ViewTreeObserver.OnGlobalLayoutListener {

    //Bundle Key used for grabbing the Intent's data
    public static final String BOOK_INFO_ITEM_STR_KEY = "BookInfo.Item.Data";
    //Constant used for logs
    private static final String LOG_TAG = BookDetailActivity.class.getSimpleName();
    //Storing references to the View components that are to be updated with the data
    private TextView mTitleTextView;
    private TextView mAuthorTextView;
    private RatingBar mBookRatingBar;
    private TextView mRatingCountTextView;
    private ImageView mBookImageView;
    private TextView mPagesTextView;
    private TextView mPublisherTextView;
    private TextView mCategoryTextView;
    private TextView mDescriptionTextView;
    private ImageButton mEpubImageButton;
    private ImageButton mPdfImageButton;
    private Button mWebPreviewButton;
    private Button mInfoButton;
    private Button mBuyButton;

    //Storing references to the TextViews with constant Text that are to be hidden based on the data
    private TextView mPublisherSubtitleTextView;
    private TextView mCategorySubtitleTextView;
    private TextView mSamplePreviewsSubtitleTextView;
    private TextView mNotForSaleTextView;

    //Storing references to the Image Borders that are to be hidden based on the data
    private ImageView mPublisherBorderImageView;
    private ImageView mCategoryBorderImageView;
    private ImageView mSamplesBorderImageView;
    private ImageView mInfoBorderImageView;

    //Storing references to the ImageViews for TextView Expand/Collapse
    private ImageView mTitleTextExpandImageView;
    private ImageView mAuthorTextExpandImageView;

    //Storing references to the NestedScrollViews that wraps the Title/Author TextViews
    private NestedScrollView mTitleTextScrollView;
    private NestedScrollView mAuthorTextScrollView;

    //Storing the Links pointed to by the respective buttons, retrieved from the BookInfo data
    private String mEpubLink;
    private String mPdfLink;
    private String mPreviewLink;
    private String mInfoLink;
    private String mBuyLink;

    //ViewTreeObserver to watch the Title TextView and Author TextView to enable Expand/Collapse
    private ViewTreeObserver mViewTreeObserver;

    //Rotate Animators for TextView Expand/Collapse Image anchor
    private Animator rotateTo180Anim;
    private Animator rotateTo0Anim;

    //Intent for the Larger Book Image to be shown in the Book Image Activity
    private Intent mBookImageIntent;

    //Method invoked by the system to create and setup the layout 'R.layout.activity_book_detail'
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        //Displaying the Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //Retrieving the View Components: START
        mTitleTextView = findViewById(R.id.detail_title_text_id);
        mTitleTextScrollView = findViewById(R.id.detail_title_text_scroll_id);
        mTitleTextExpandImageView = findViewById(R.id.detail_title_text_expand_img_id);
        mAuthorTextView = findViewById(R.id.detail_author_text_id);
        mAuthorTextScrollView = findViewById(R.id.detail_author_text_scroll_id);
        mAuthorTextExpandImageView = findViewById(R.id.detail_author_text_expand_img_id);
        mBookRatingBar = findViewById(R.id.detail_ratingbar_id);
        mRatingCountTextView = findViewById(R.id.detail_rating_count_text_id);
        mBookImageView = findViewById(R.id.detail_book_image_id);
        mPagesTextView = findViewById(R.id.detail_pages_text_id);
        mPublisherSubtitleTextView = findViewById(R.id.detail_publisher_section_text_id);
        mPublisherTextView = findViewById(R.id.detail_publisher_text_id);
        mPublisherBorderImageView = findViewById(R.id.detail_publisher_border_id);
        mCategorySubtitleTextView = findViewById(R.id.detail_category_section_text_id);
        mCategoryTextView = findViewById(R.id.detail_categories_text_id);
        mCategoryBorderImageView = findViewById(R.id.detail_categories_border_id);
        mDescriptionTextView = findViewById(R.id.detail_description_text_id);
        mSamplePreviewsSubtitleTextView = findViewById(R.id.detail_samples_section_text_id);
        mEpubImageButton = findViewById(R.id.detail_epub_button_id);
        mPdfImageButton = findViewById(R.id.detail_pdf_button_id);
        mWebPreviewButton = findViewById(R.id.detail_web_button_id);
        mSamplesBorderImageView = findViewById(R.id.detail_samples_border_id);
        mInfoButton = findViewById(R.id.detail_info_button_id);
        mInfoBorderImageView = findViewById(R.id.detail_info_border_id);
        mBuyButton = findViewById(R.id.detail_buy_button_id);
        mNotForSaleTextView = findViewById(R.id.detail_not_for_sale_text_id);
        //Retrieving the View Components: END

        //Retrieving the Parent View's TreeObserver for the Title TextView and the Author TextView
        mViewTreeObserver = findViewById(R.id.detail_title_author_card_id).getViewTreeObserver();

        //Defaulting the Visibility of Expand/Collapse ImageView anchors to "GONE"
        mTitleTextExpandImageView.setVisibility(View.GONE);
        mAuthorTextExpandImageView.setVisibility(View.GONE);

        //Loading the Rotation Animators for TextView Expand/Collapse Image anchors
        rotateTo0Anim = AnimatorInflater.loadAnimator(this, R.animator.rotate_180_0);
        rotateTo180Anim = AnimatorInflater.loadAnimator(this, R.animator.rotate_0_180);

        //Setting the Click Listeners on the Buttons
        mEpubImageButton.setOnClickListener(this);
        mPdfImageButton.setOnClickListener(this);
        mWebPreviewButton.setOnClickListener(this);
        mInfoButton.setOnClickListener(this);
        mBuyButton.setOnClickListener(this);

        //Setting Click Listener onto the ImageView
        mBookImageView.setOnClickListener(this);

        //Handling the Intent data
        handleIntent(getIntent());
    }

    /**
     * Method that handles the Intent data being passed by the
     * {@link com.example.kaushiknsanji.bookslibrary.adapterviews.RecyclerViewFragment}
     *
     * @param intent is the Intent that contains the Item Data of the Item clicked in the
     *               {@link com.example.kaushiknsanji.bookslibrary.adapterviews.RecyclerViewFragment}
     */
    private void handleIntent(Intent intent) {
        //Retrieving the Item Data passed in the intent
        Parcelable parcelableExtra = intent.getParcelableExtra(BOOK_INFO_ITEM_STR_KEY);
        if (parcelableExtra instanceof BookInfo) {
            //Validating and casting to BookInfo accordingly
            BookInfo itemBookInfo = (BookInfo) parcelableExtra;
            //Updating the Layout based on the BookInfo data
            updateLayout(itemBookInfo);
        }
    }

    /**
     * Updates the layout elements with the data found in the {@link BookInfo} object
     *
     * @param itemBookInfo is the {@link BookInfo} object for the Item view clicked in the
     *                     {@link com.example.kaushiknsanji.bookslibrary.adapterviews.RecyclerViewFragment}
     */
    private void updateLayout(BookInfo itemBookInfo) {
        //Updating the Title Text
        updateTitle(itemBookInfo.getTitle(), itemBookInfo.getSubTitle());
        //Updating the Author Text
        updateAuthor(itemBookInfo.getAuthors(getString(R.string.no_authors_found_default_text)));
        //Updating the Book Rating
        updateBookRating(itemBookInfo.getBookRatings(), itemBookInfo.getBookRatingCount());
        //Updating the Book Image
        updateBookImage(itemBookInfo.getImageLinkForDetailInfo());
        //Creating an Intent for the Book Image
        generateBookImageIntent(itemBookInfo.getImageLinkForBookImageInfo());
        //Updating the Pages Information
        updatePagesInfo(itemBookInfo.getPageCount(), itemBookInfo.getBookType());

        //Updating the Publisher Text: START
        String publishedDateStr = "";
        try {
            publishedDateStr = itemBookInfo.getPublishedDateInLongFormat(getString(R.string.no_published_date_default_text));
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Error occurred while parsing and formatting the Published date", e);
        }
        updatePublisher(itemBookInfo.getPublisher(getString(R.string.no_publisher_found_default_text)), publishedDateStr);
        //Updating the Publisher Text: END

        //Updating the Category Text
        updateCategory(itemBookInfo.getCategories(getString(R.string.no_categories_found_default_text)));
        //Updating the Description Text
        updateDescription(itemBookInfo.getDescription(getString(R.string.no_description_found_default_text)));
        //Updating the Previews
        updatePreviews(itemBookInfo.isSampleAvailable(), itemBookInfo.getEpubLink(), itemBookInfo.getPdfLink(), itemBookInfo.getPreviewLink());
        //Updating the Saleability information
        updateSaleability(itemBookInfo.isForSale(), itemBookInfo.isDiscounted(), itemBookInfo.getListPrice(), itemBookInfo.getRetailPrice(), itemBookInfo.getBuyLink());
    }

    /**
     * Method that updates the Saleability information of the Book based on the details
     * retrieved from the Item's {@link BookInfo} Object
     *
     * @param isForSale    is a Boolean which tells whether the Book is for Sale or Not in the user's region
     * @param isDiscounted is a Boolean which tells whether the Book is Discounted or not
     * @param listPrice    is the List Price of the Book in User's locale currency format
     * @param retailPrice  is the Retail Price of the Book in User's locale currency format
     * @param buyLink      is the Buy Link for purchasing the Book if the Book is for Sale
     */
    private void updateSaleability(boolean isForSale, boolean isDiscounted, String listPrice, String retailPrice, String buyLink) {
        if (isForSale) {
            //Displaying only the Buy Button with the Price details as the Book is saleable
            //in the user's region
            mBuyLink = buyLink; //Updating the Buy Link on the Button

            //Setting the Visibility to the Buy Button
            mBuyButton.setVisibility(View.VISIBLE);
            mNotForSaleTextView.setVisibility(View.GONE);

            //Setting the Text on the Button
            if (isDiscounted) {
                //When the Book is having a Discount, make the List Price appear with a Strikethrough Text
                //on the Button
                String textToSet = listPrice + " " + retailPrice + " Buy";
                TextAppearanceUtility.setStrikethroughText(mBuyButton, textToSet, listPrice);
                TextAppearanceUtility.modifyTextColor(mBuyButton, listPrice, ContextCompat.getColor(this, R.color.detailDiscountedTextColor));
            } else {
                //When there is no Discount on the Price, show only the Retail Price
                //on the Button
                String textToSet = retailPrice + " Buy";
                mBuyButton.setText(textToSet);
            }

        } else {
            //Hiding the Buy Button and displaying only the 'Not For Sale' Text as the Book is NOT
            //saleable in the user's region
            mBuyButton.setVisibility(View.GONE);
            mNotForSaleTextView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Method that updates the Previews section with the links retrieved from the Item's {@link BookInfo} Object
     *
     * @param isSampleAvailable is a Boolean which tells whether the Sample Previews are available or not
     * @param epubLink          is the Epub Sample Preview Link if available; else empty
     * @param pdfLink           is the Pdf Sample Preview Link if available; else empty
     * @param previewLink       is the Web Preview Link if available; else an Info link
     */
    private void updatePreviews(boolean isSampleAvailable, String epubLink, String pdfLink, String previewLink) {
        if (isSampleAvailable) {
            //Updating the Buttons with their Previews when available

            if (TextUtils.isEmpty(epubLink)) {
                //Hiding the Epub Button when Epub Preview is not available
                mEpubImageButton.setVisibility(View.GONE);
            } else {
                //Updating the Epub Button with its Preview link when available
                mEpubLink = epubLink;
            }

            if (TextUtils.isEmpty(pdfLink)) {
                //Hiding the Pdf Button when Pdf Preview is not available
                mPdfImageButton.setVisibility(View.GONE);
            } else {
                //Updating the Pdf Button with its Preview link when available
                mPdfLink = pdfLink;
            }

            //Updating the Web Preview Button with its Preview Link
            mPreviewLink = previewLink;

            if (!TextUtils.isEmpty(epubLink) && !TextUtils.isEmpty(pdfLink)) {
                //Changing the placement of Web Preview Button when Epub + Pdf Previews are available

                //Moving the Web Preview Button to the bottom of the two buttons
                ConstraintLayout.LayoutParams layoutParamsWeb = (ConstraintLayout.LayoutParams) mWebPreviewButton.getLayoutParams();
                layoutParamsWeb.topToBottom = mPdfImageButton.getId();
                layoutParamsWeb.leftToLeft = R.id.detail_book_info_card_content_id;
                layoutParamsWeb.startToStart = R.id.detail_book_info_card_content_id;
                layoutParamsWeb.rightToRight = R.id.detail_book_info_card_content_id;
                layoutParamsWeb.endToEnd = R.id.detail_book_info_card_content_id;
                layoutParamsWeb.leftToRight = -1;
                layoutParamsWeb.startToEnd = -1;
                layoutParamsWeb.topMargin = getResources().getDimensionPixelSize(R.dimen.detail_content_margin_top);
                layoutParamsWeb.leftMargin = 0;
                layoutParamsWeb.rightMargin = 0;

                //Changing the constraints of the Pdf Button, to be attached to the right of the parent
                ConstraintLayout.LayoutParams layoutParamsPdf = (ConstraintLayout.LayoutParams) mPdfImageButton.getLayoutParams();
                layoutParamsPdf.rightToRight = R.id.detail_book_info_card_content_id;
                layoutParamsPdf.endToEnd = R.id.detail_book_info_card_content_id;
                layoutParamsPdf.rightToLeft = -1;
                layoutParamsPdf.endToStart = -1;
            }

            //Hiding the Info Button and its related components as Web Preview is available
            mInfoButton.setVisibility(View.GONE);
            mInfoBorderImageView.setVisibility(View.GONE);

        } else {
            //Updating the Info Button with the Info Link when no previews are available
            //and hiding the Button components related to Sample previews as they are NOT available

            mInfoLink = previewLink;

            //Hiding the components related to Sample previews
            mEpubImageButton.setVisibility(View.GONE);
            mPdfImageButton.setVisibility(View.GONE);
            mWebPreviewButton.setVisibility(View.GONE);
            mSamplesBorderImageView.setVisibility(View.GONE);
            mSamplePreviewsSubtitleTextView.setVisibility(View.GONE);

        }
    }

    /**
     * Method that updates the Description Text for the Book
     *
     * @param description is the Description of the Book retrieved from the Item's {@link BookInfo} Object
     */
    private void updateDescription(String description) {
        //Setting the Description of the Book
        mDescriptionTextView.setText(description);
        //Setting the Font for the Description
        mDescriptionTextView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/quintessential_regular.ttf"));
    }

    /**
     * Method that updates the Category Text
     *
     * @param categories is the Book Categories retrieved from the Item's {@link BookInfo} Object
     */
    private void updateCategory(String categories) {
        if (categories.equals(getString(R.string.no_categories_found_default_text))) {
            //Hiding the respective view components when Category info is not available
            mCategorySubtitleTextView.setVisibility(View.GONE);
            mCategoryTextView.setVisibility(View.GONE);
            mCategoryBorderImageView.setVisibility(View.GONE);
        } else {
            //Updating the Category info when it is present
            mCategoryTextView.setText(categories);
        }
    }

    /**
     * Method that updates the Publisher Text and its Published Date
     *
     * @param publisher        is the Publisher retrieved from the Item's {@link BookInfo} Object
     * @param publishedDateStr is the Book's Published Date retrieved from the Item's {@link BookInfo} Object
     */
    private void updatePublisher(String publisher, String publishedDateStr) {
        if (publisher.equals(getString(R.string.no_publisher_found_default_text))
                && publishedDateStr.equals(getString(R.string.no_published_date_default_text))) {
            //Hiding the respective view components when both the Publisher
            //and the Date info is not available
            mPublisherSubtitleTextView.setVisibility(View.GONE);
            mPublisherTextView.setVisibility(View.GONE);
            mPublisherBorderImageView.setVisibility(View.GONE);
        } else {
            //Updating the Publisher info when either of the information is present
            mPublisherTextView.setText(getString(R.string.detail_publisher_section_format, publisher, publishedDateStr));
        }
    }

    /**
     * Method that updates the Number of Pages in the Book along with its Type,
     * using the information retrieved from the Item's {@link BookInfo} Object
     *
     * @param pageCount is the Number of Pages in the Book
     * @param bookType  is the Type that tells if it is a Magazine or a Book
     */
    private void updatePagesInfo(int pageCount, String bookType) {
        //Setting the Pages information
        mPagesTextView.setText(getString(R.string.pages_book_type, pageCount, bookType));
    }

    /**
     * Method that updates the Book Image
     *
     * @param imageLinkForDetailInfo is the Link to the Book's Image
     *                               retrieved from the Item's {@link BookInfo} Object
     */
    private void updateBookImage(String imageLinkForDetailInfo) {
        if (!TextUtils.isEmpty(imageLinkForDetailInfo)) {
            //Loading the Image when link is present
            ImageDownloaderFragment
                    .newInstance(this.getSupportFragmentManager(), mBookImageView.getId())
                    .executeAndUpdate(mBookImageView,
                            imageLinkForDetailInfo,
                            mBookImageView.getId());
        } else {
            //Resetting to the default Book image when link is absent
            mBookImageView.setImageResource(R.drawable.ic_book);
        }
    }

    /**
     * Method that generates an Intent for the Larger Book Image to be shown
     * when the Book ImageView is clicked
     *
     * @param imageLinkForBookImageInfo is the Link to the Book's Image
     *                                  retrieved from the Item's {@link BookInfo} Object
     */
    private void generateBookImageIntent(String imageLinkForBookImageInfo) {
        if (!TextUtils.isEmpty(imageLinkForBookImageInfo)) {
            //Generating the Intent for the Book Image to be shown,
            //when the link to Book Image is present

            //Initializing the Book Image Intent
            mBookImageIntent = new Intent(this, BookImageActivity.class);
            //Passing the Image Link to the Book Image Activity
            mBookImageIntent.putExtra(BookImageActivity.BOOK_INFO_ITEM_IMAGE_STR_KEY, imageLinkForBookImageInfo);

        } else {
            //Setting the Intent for the Book Image to NULL,
            //when the link to Book Image is NOT present
            mBookImageIntent = null;
        }
    }

    /**
     * Method that updates the Book's Rating fields
     *
     * @param bookRatings     is the Book's Star Rating retrieved from the Item's {@link BookInfo} Object
     * @param bookRatingCount is the Count of Ratings on the Book, retrieved from the Item's {@link BookInfo} Object
     */
    private void updateBookRating(float bookRatings, String bookRatingCount) {
        //Updating the Rating on the RatingBar
        mBookRatingBar.setRating(bookRatings);
        //Setting the Rating Count
        mRatingCountTextView.setText(bookRatingCount);
    }

    /**
     * Method that updates the Author Text
     *
     * @param authors is the Authors String retrieved from the Item's {@link BookInfo} Object
     */
    private void updateAuthor(String authors) {
        //Setting the Author Text
        mAuthorTextView.setText(getString(R.string.by_author_text, authors));
        //Setting the Font for the Author
        mAuthorTextView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/quintessential_regular.ttf"));
    }

    /**
     * Method that updates the Title Text
     *
     * @param title    is the Book Title retrieved from the Item's {@link BookInfo} Object
     * @param subTitle is the Book SubTitle retrieved from the Item's {@link BookInfo} Object
     */
    private void updateTitle(String title, String subTitle) {
        if (!TextUtils.isEmpty(subTitle)) {
            //When SubTitle is present

            //Separating the title and its subtitle by a new line character: START
            int indexOfSubtitleStart = TextUtils.indexOf(title, subTitle);
            int indexOfColonForSubtitle = TextUtils.lastIndexOf(title, ':', indexOfSubtitleStart);
            if (indexOfColonForSubtitle == -1) {
                //If the title originally had a subtitle but the separator was a '-' instead of ':'
                indexOfColonForSubtitle = TextUtils.lastIndexOf(title, '-', indexOfSubtitleStart);
            }
            title = title.substring(0, indexOfColonForSubtitle) + "\n" + subTitle;
            //Separating the title and its subtitle by a new line character: END

            //Setting the Title Text with resized text for SubTitle
            TextAppearanceUtility.modifyTextSize(mTitleTextView, title, subTitle, 0.75f);
        } else {
            //Setting the Title Text only when SubTitle is NOT present
            mTitleTextView.setText(title);
        }

        //Setting the Font for the Title
        mTitleTextView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/coprgtb.ttf"));
    }

    /**
     * Method that opens a webpage for the URL passed
     *
     * @param urlString is the String containing the URL of the webpage to be launched
     */
    private void openLink(String urlString) {
        //Parsing the URL
        Uri webPageUri = Uri.parse(urlString);
        //Creating an ACTION_VIEW Intent with the URI
        Intent webIntent = new Intent(Intent.ACTION_VIEW, webPageUri);
        //Checking if there is an Activity that accepts the Intent
        if (webIntent.resolveActivity(getPackageManager()) != null) {
            //Launching the corresponding Activity and passing it the Intent
            startActivity(webIntent);
        }
    }

    /**
     * This hook is called whenever an item in the options menu is selected.
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Executing based on MenuItem's Id
        switch (item.getItemId()) {
            case android.R.id.home:
                //Handling the action bar's home/up button
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Called when a view has been clicked.
     * Mainly used for the Buttons in this Details Page
     *
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {
        //Executing action based on the View's id
        switch (view.getId()) {
            case R.id.detail_epub_button_id:
                //For the EPUB Preview Button
                openLink(mEpubLink);
                break;
            case R.id.detail_pdf_button_id:
                //For the PDF Preview Button
                openLink(mPdfLink);
                break;
            case R.id.detail_web_button_id:
                //For the WEB Preview Button
                openLink(mPreviewLink);
                break;
            case R.id.detail_info_button_id:
                //For the Info Button
                openLink(mInfoLink);
                break;
            case R.id.detail_buy_button_id:
                //For the Buy Button
                openLink(mBuyLink);
                break;
            case R.id.detail_book_image_id:
                //For the Book Image shown
                if (mBookImageIntent != null) {
                    //When the Image is present and the Intent is created
                    //Passing the Intent to the Book Image Activity
                    //to display a bigger picture of the Book
                    startActivity(mBookImageIntent);
                } else {
                    //When the Image is not present for the Book
                    //Displaying a toast to indicate the user that there is no image for the Book
                    Toast.makeText(this, R.string.no_book_image_msg, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.detail_title_text_id:
                //For the Title TextView
                //Expand/Collapse the Title TextView when clicked
                toggleTextViewExpansion(mTitleTextView, getResources().getInteger(R.integer.detail_title_text_max_lines),
                        mTitleTextExpandImageView, mTitleTextScrollView);
                break;
            case R.id.detail_author_text_id:
                //For the Author TextView
                //Expand/Collapse the Author TextView when clicked
                toggleTextViewExpansion(mAuthorTextView, getResources().getInteger(R.integer.detail_author_text_max_lines),
                        mAuthorTextExpandImageView, mAuthorTextScrollView);
                break;
        }
    }

    /**
     * Callback method to be invoked when the global layout state or the visibility of views
     * within the view tree changes.
     * This is applied for the View that contains both the Title Text and the Author Text
     */
    @Override
    public void onGlobalLayout() {
        //Enabling the Title TextView for Expansion if it exceeds Maxlines
        addExpandableStateForEllipsizedTextViews(mTitleTextView);
        //Enabling the Author TextView for Expansion if it exceeds Maxlines
        addExpandableStateForEllipsizedTextViews(mAuthorTextView);

        //Retrieving the Parent View's TreeObserver
        mViewTreeObserver = findViewById(R.id.detail_title_author_card_id).getViewTreeObserver();

        if (mViewTreeObserver.isAlive()) {
            //UnRegistering the OnGlobalLayoutListener when done
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mViewTreeObserver.removeOnGlobalLayoutListener(this);
            } else {
                mViewTreeObserver.removeGlobalOnLayoutListener(this);
            }
        }
    }

    /**
     * Method that adds click listeners to TextViews with Text exceeding
     * the MaxLines setting, which enables them to be expanded on click
     *
     * @param textView is the TextView of either the Title Text or the Author Text which
     *                 is to be enabled for expansion
     */
    private void addExpandableStateForEllipsizedTextViews(TextView textView) {
        //Retrieving the total number of Text Lines in the TexView
        int totalLineCount = textView.getLineCount();

        if (totalLineCount > 0) {
            //When Layout is attached to the TextView and the line count is more than 0

            //Retrieving the Ellipse count for the entire Text
            int totalEllipseCount = getEllipseCountFromTextView(textView, totalLineCount);

            if (totalEllipseCount > 0) {
                //When the Text in the TextView is Ellipsized (exceeds MaxLines set),
                //enable the Click Listener
                switch (textView.getId()) {
                    case R.id.detail_title_text_id:
                        //For the Title TextView
                        if (mTitleTextExpandImageView.getVisibility() == View.GONE) {
                            //Displaying the expand image anchor for the lengthy Title Text
                            mTitleTextExpandImageView.setVisibility(View.VISIBLE);

                            //Registering the click listener on the Title TextView
                            mTitleTextView.setOnClickListener(this);
                        }
                        break;
                    case R.id.detail_author_text_id:
                        if (mAuthorTextExpandImageView.getVisibility() == View.GONE) {
                            //Displaying the expand image anchor for the lengthy Author Text
                            mAuthorTextExpandImageView.setVisibility(View.VISIBLE);

                            //Registering the click listener on the Author TextView
                            mAuthorTextView.setOnClickListener(this);
                        }
                        break;
                }
            }
        }
    }

    /**
     * Method that searches for the Ellipsis in the Text of a TextView and returns its total count
     *
     * @param textView       is the TextView containing a Text which needs to be scanned for Ellipsis
     * @param totalLineCount is the Total Line count of the Text in Integer
     * @return Integer containing the total number of Ellipsis found in the Text of a TextView
     */
    private int getEllipseCountFromTextView(TextView textView, int totalLineCount) {
        int totalEllipseCount = 0; //Defaulting the Total Ellipse count to 0, initially

        //Retrieving the layout attached to TextView
        Layout textViewLayout = textView.getLayout();

        //Iterating over the Lines of the Text and counting the Ellipsis found
        for (int index = 0; index < totalLineCount; index++) {
            if (textViewLayout.getEllipsisCount(index) > 0) {
                totalEllipseCount++;
            }
        }

        //Returning the total Ellipsis found
        return totalEllipseCount;
    }

    /**
     * Method that toggles the TextView Expand/Collapse
     * for the Title Text(R.id.detail_title_text_id) and the Author Text (R.id.detail_author_text_id)
     *
     * @param textView                  is the TextView of either the Title Text or the Author Text
     * @param originalLineCount         is the Original MaxLine Count set for the TextViews
     * @param textExpandAnchorImageView is the Expand/Collapse ImageView anchor for the TextView passed
     * @param textScrollView            is the {@link NestedScrollView} which is the parent of the TextView passed
     */
    private void toggleTextViewExpansion(TextView textView, int originalLineCount, ImageView textExpandAnchorImageView, NestedScrollView textScrollView) {
        //Retrieving the Current Line count of the Text in the TextView
        int totalLineCount = textView.getLineCount();

        //Retrieving the Ellipse count for the entire Text
        int totalEllipseCount = getEllipseCountFromTextView(textView, totalLineCount);

        //Retrieving the basic Layout Params of the NestedScrollView
        ViewGroup.LayoutParams layoutParams = textScrollView.getLayoutParams();

        if (totalEllipseCount == 0) {
            //Resetting to original state when it was previously expanded to show all the lines
            textView.setMaxLines(originalLineCount);

            //Resetting the parent NestedScrollView height to WRAP_CONTENT
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;

            //Rotating the Image Anchor from 180 to 0
            rotateTo0Anim.setTarget(textExpandAnchorImageView);
            rotateTo0Anim.start();

        } else {
            //Setting to the expanded state to show all the lines
            textView.setMaxLines(Integer.MAX_VALUE);

            //Retrieving the Max Height set for the parent NestedScrollView
            int scrollContentMaxHeight = getResources().getDimensionPixelSize(R.dimen.detail_title_author_content_max_height);

            //Limiting the Height of the parent NestedScrollView when the content Height is more than the fixed Height
            if (textView.getMeasuredHeight() > scrollContentMaxHeight) {
                layoutParams.height = scrollContentMaxHeight; //Limiting to the fixed Height set
            }

            //Rotating the Image Anchor from 0 to 180
            rotateTo180Anim.setTarget(textExpandAnchorImageView);
            rotateTo180Anim.start();
        }
    }

    //Called by the Activity when it is prepared to be shown
    @Override
    protected void onResume() {
        super.onResume();

        if (mViewTreeObserver != null && mViewTreeObserver.isAlive()) {
            //Registering the OnGlobalLayoutListener when the ViewTreeObserver is alive
            mViewTreeObserver.addOnGlobalLayoutListener(this);
        }

    }

}