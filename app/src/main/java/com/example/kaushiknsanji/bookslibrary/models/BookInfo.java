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

package com.example.kaushiknsanji.bookslibrary.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.example.kaushiknsanji.bookslibrary.utils.BooksDiffUtility;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Parcelable Model Class for holding the parsed data of the Books
 * retrieved in the search response
 *
 * @author Kaushik N Sanji
 */
public class BookInfo implements Parcelable {

    /**
     * Implementation of {@link android.os.Parcelable.Creator} interface
     * to generate instances of this Parcelable class {@link BookInfo} from a {@link Parcel}
     */
    public static final Creator<BookInfo> CREATOR = new Creator<BookInfo>() {

        /**
         * Creates an instance of this Parcelable class {@link BookInfo} from
         * a given Parcel whose data had been previously written by #writeToParcel() method
         *
         * @param in The Parcel to read the object's data from.
         * @return Returns a new instance of this Parcelable class {@link BookInfo}
         */
        @Override
        public BookInfo createFromParcel(Parcel in) {
            return new BookInfo(in);
        }

        /**
         * Creates a new Array of this Parcelable class {@link BookInfo}
         * @param size  Size of the array.
         * @return Returns an array of this Parcelable class {@link BookInfo}, with every entry
         * initialized to null.
         */
        @Override
        public BookInfo[] newArray(int size) {
            return new BookInfo[size];
        }
    };
    //Stores the id of the Book (for comparison purpose)
    private String mBookId;
    //Stores the Title of the Book
    private String mTitle;
    //Stores the SubTitle of the Book if any
    private String mSubTitle;
    //Stores the List of Authors for the Book
    private String[] mAuthors;
    //Stores the Publisher of the Book
    private String mPublisher;
    //Stores the Published Date of the Book
    private String mPublishedDateStr;
    //Stores the Number of Pages in the Book
    private int mPageCount;
    //Stores whether the Book is a Magazine or a Book
    private String mBookType;
    //Stores the list of Categories that the Book belongs to
    private String[] mCategories;
    //Stores the Book ratings
    private float mBookRatings;
    //Stores the Count of Ratings the Book has received
    private int mBookRatingCount;
    //Stores whether the book is saleable or not for the user
    private String mSaleability;
    //Stores the List price of the book
    private double mListPrice;
    //Stores the Retail price of the book
    private double mRetailPrice;
    //Stores the Description of the book
    private String mDescription;
    //Stores the link to Small Image of the Book
    private String mImageLinkSmall;
    //Stores the link to Large Image of the Book
    private String mImageLinkLarge;
    //Stores the link to Extra Large Image of the Book
    private String mImageLinkExtraLarge;
    //Stores the accessibility status details of the book
    private String mAccessViewStatus;
    //Stores the link to Sample epub download of the Book
    private String mEpubLink;
    //Stores the link to Sample pdf download of the Book
    private String mPdfLink;
    //Stores the link to web preview if available or the info page
    private String mPreviewLink;
    //Stores the link to buying page of the book
    private String mBuyLink;

    /**
     * Constructor of {@link BookInfo}
     *
     * @param bookId is the Id of the Book Volume
     */
    public BookInfo(String bookId) {
        mBookId = bookId;
    }

    /**
     * Parcelable constructor that de-serializes the data from a Parcel Class passed
     *
     * @param in is the Instance of the Parcel Class containing the serialized data
     */
    protected BookInfo(Parcel in) {
        mBookId = in.readString();
        mTitle = in.readString();
        mSubTitle = in.readString();
        mAuthors = in.createStringArray();
        mPublisher = in.readString();
        mPublishedDateStr = in.readString();
        mPageCount = in.readInt();
        mBookType = in.readString();
        mCategories = in.createStringArray();
        mBookRatings = in.readFloat();
        mBookRatingCount = in.readInt();
        mSaleability = in.readString();
        mListPrice = in.readDouble();
        mRetailPrice = in.readDouble();
        mDescription = in.readString();
        mImageLinkSmall = in.readString();
        mImageLinkLarge = in.readString();
        mImageLinkExtraLarge = in.readString();
        mAccessViewStatus = in.readString();
        mEpubLink = in.readString();
        mPdfLink = in.readString();
        mPreviewLink = in.readString();
        mBuyLink = in.readString();
    }

    /**
     * Flattens/Serializes this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mBookId);
        dest.writeString(mTitle);
        dest.writeString(mSubTitle);
        dest.writeStringArray(mAuthors);
        dest.writeString(mPublisher);
        dest.writeString(mPublishedDateStr);
        dest.writeInt(mPageCount);
        dest.writeString(mBookType);
        dest.writeStringArray(mCategories);
        dest.writeFloat(mBookRatings);
        dest.writeInt(mBookRatingCount);
        dest.writeString(mSaleability);
        dest.writeDouble(mListPrice);
        dest.writeDouble(mRetailPrice);
        dest.writeString(mDescription);
        dest.writeString(mImageLinkSmall);
        dest.writeString(mImageLinkLarge);
        dest.writeString(mImageLinkExtraLarge);
        dest.writeString(mAccessViewStatus);
        dest.writeString(mEpubLink);
        dest.writeString(mPdfLink);
        dest.writeString(mPreviewLink);
        dest.writeString(mBuyLink);
    }

    /**
     * Describes the kinds of special objects contained in this Parcelable
     * instance's marshaled representation.
     *
     * @return a bitmask indicating the set of special object types marshaled
     * by this Parcelable object instance.
     */
    @Override
    public int describeContents() {
        return 0; //Indicating with no mask
    }

    /**
     * Setter method for the List of Authors
     *
     * @param authors The List of Authors
     */
    public void setAuthors(String[] authors) {
        this.mAuthors = authors;
    }

    /**
     * Setter method for the Publisher of the Book
     *
     * @param publisher The Publisher of the Book
     */
    public void setPublisher(String publisher) {
        this.mPublisher = publisher;
    }

    /**
     * Setter method for the Published Date of the Book
     *
     * @param publishedDateStr The Published Date of the Book
     */
    public void setPublishedDateStr(String publishedDateStr) {
        this.mPublishedDateStr = publishedDateStr;
    }

    /**
     * Setter method for the list of Categories the Book belongs to
     *
     * @param categories The list of Categories the Book belongs to
     */
    public void setCategories(String[] categories) {
        this.mCategories = categories;
    }

    /**
     * Setter method for the saleablility of the book in the User's region/IP
     *
     * @param saleability The saleablility of the book in the User's region/IP
     */
    public void setSaleability(String saleability) {
        this.mSaleability = saleability;
    }

    /**
     * Setter method for the Description of the book
     *
     * @param description The Description of the book
     */
    public void setDescription(String description) {
        this.mDescription = description;
    }

    /**
     * Setter method for the link to Small Image of the Book
     *
     * @param imageLinkSmall The link to Small Image of the Book
     */
    public void setImageLinkSmall(String imageLinkSmall) {
        this.mImageLinkSmall = imageLinkSmall;
    }

    /**
     * Setter method for the link to Large Image of the Book
     *
     * @param imageLinkLarge The link to Large Image of the Book
     */
    public void setImageLinkLarge(String imageLinkLarge) {
        this.mImageLinkLarge = imageLinkLarge;
    }

    /**
     * Setter method for the link to Extra Large Image of the Book
     *
     * @param imageLinkExtraLarge The link to Extra Large Image of the Book
     */
    public void setImageLinkExtraLarge(String imageLinkExtraLarge) {
        this.mImageLinkExtraLarge = imageLinkExtraLarge;
    }

    /**
     * Setter method for the accessibility status details of the book
     *
     * @param accessViewStatus The accessibility status details of the book
     */
    public void setAccessViewStatus(String accessViewStatus) {
        this.mAccessViewStatus = accessViewStatus;
    }

    /**
     * Method that prepares and returns the Complete title of the Book
     *
     * @return String containing the Title and the SubTitle if any
     */
    public String getTitle() {

        if (!TextUtils.isEmpty(mSubTitle)
                && !mTitle.contains(mSubTitle)) {
            //Combining SubTitle when not empty and the Title does not contain the same
            return (mTitle + ": " + mSubTitle);
        }

        //Returning the Title only when Subtitle is not present
        return mTitle;
    }

    /**
     * Setter method for Title of the Book
     *
     * @param title The Title of the Book
     */
    public void setTitle(String title) {
        this.mTitle = title;
    }

    /**
     * Method that returns the SubTitle of the Book if any
     *
     * @return String containing the SubTitle of the Book
     */
    public String getSubTitle() {
        return mSubTitle;
    }

    /**
     * Setter method for SubTitle of the Book
     *
     * @param subTitle The SubTitle of the Book
     */
    public void setSubTitle(String subTitle) {
        this.mSubTitle = subTitle;
    }

    /**
     * Method that prepares and returns the Authors of the Book
     *
     * @param fallback String containing the default message to be returned when no Authors present
     * @return String containing the Authors of the Book
     */
    public String getAuthors(String fallback) {
        int countOfAuthors = (null == mAuthors ? 0 : mAuthors.length);

        if (countOfAuthors == 1) {
            //Returning the first Author if only one is present
            return TextUtils.isEmpty(mAuthors[0]) ? fallback : mAuthors[0];
        } else if (countOfAuthors > 1) {
            //Concatenating all the Authors and returning the same
            return TextUtils.join(", ", mAuthors);
        }

        //Returning the default fallback string when no Authors are present
        return fallback;
    }

    /**
     * Method that returns the Publisher of the Book
     *
     * @param fallback String containing the default message to be returned when no Publisher is present
     * @return String containing the Publisher information
     */
    public String getPublisher(String fallback) {
        return TextUtils.isEmpty(mPublisher) ? fallback : mPublisher;
    }

    /**
     * Method that prepares and returns the Published date of the Book (in Medium format)
     *
     * @param fallback is the default fallback string that will be returned when no date is available
     *                 throws {@link ParseException}
     * @return String containing the Published date in the format
     * <br/> 'MMM dd, yyyy' when complete date information is available
     * <br/> 'MMM, yyyy' when only the month & year information is available
     * <br/> 'yyyy' when only the year information is available
     */
    public String getPublishedDateInMediumFormat(String fallback) throws ParseException {
        if (!TextUtils.isEmpty(mPublishedDateStr)) {
            //Splitting the date to check for the existence of Year, Month and Date
            String[] dateSplits = mPublishedDateStr.split("-");

            int noOfDateParts = dateSplits.length;

            //Returning the formatted date based on what is present in the date received
            switch (noOfDateParts) {
                case 1: //When only the Year part is present
                    return mPublishedDateStr; //Returning the Year AS-IS
                case 2: //When only the Year + Month part is present
                    Date yearMonthDate = (new SimpleDateFormat("yyyy-MM", Locale.getDefault())).parse(mPublishedDateStr);
                    //Returning the published date in the format like 'Jan, 2017'
                    return (new SimpleDateFormat("MMM, yyyy", Locale.getDefault())).format(yearMonthDate);
                case 3: //When Complete date is present
                    Date parsedDate = (new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())).parse(mPublishedDateStr);
                    //Returning the published date in the format like 'Jan 01, 2017'
                    return DateFormat.getDateInstance(DateFormat.MEDIUM).format(parsedDate);
                default:
                    //Returning default fallback string when no date is available
                    return fallback;
            }
        }

        //Returning default fallback string when no date is available
        return fallback;
    }

    /**
     * Method that prepares and returns the Published date of the Book (in Long format)
     *
     * @param fallback is the default fallback string that will be returned when no date is available
     *                 throws {@link ParseException}
     * @return String containing the Published date in the format
     * <br/> 'MMMM dd, yyyy' when complete date information is available
     * <br/> 'MMMM, yyyy' when only the month & year information is available
     * <br/> 'yyyy' when only the year information is available
     */
    public String getPublishedDateInLongFormat(String fallback) throws ParseException {
        if (!TextUtils.isEmpty(mPublishedDateStr)) {
            //Splitting the date to check for the existence of Year, Month and Date
            String[] dateSplits = mPublishedDateStr.split("-");

            int noOfDateParts = dateSplits.length;

            //Returning the formatted date based on what is present in the date received
            switch (noOfDateParts) {
                case 1: //When only the Year part is present
                    return mPublishedDateStr; //Returning the Year AS-IS
                case 2: //When only the Year + Month part is present
                    Date yearMonthDate = (new SimpleDateFormat("yyyy-MM", Locale.getDefault())).parse(mPublishedDateStr);
                    //Returning the published date in the format like 'January, 2017'
                    return (new SimpleDateFormat("MMMM, yyyy", Locale.getDefault())).format(yearMonthDate);
                case 3: //When Complete date is present
                    Date parsedDate = (new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())).parse(mPublishedDateStr);
                    //Returning the published date in the format like 'January 01, 2017'
                    return DateFormat.getDateInstance(DateFormat.LONG).format(parsedDate);
                default:
                    //Returning default fallback string when no date is available
                    return fallback;
            }
        }

        //Returning default fallback string when no date is available
        return fallback;
    }

    /**
     * Method that returns the Number of Pages in the Book
     *
     * @return Integer containing the Number of Pages in the Book
     */
    public int getPageCount() {
        return mPageCount;
    }

    /**
     * Setter method for the Number of Pages in the Book
     *
     * @param pageCount The Number of Pages in the Book
     */
    public void setPageCount(int pageCount) {
        this.mPageCount = pageCount;
    }

    /**
     * Method that returns whether the Book is a Magazine or a Book
     *
     * @return String containing the info of whether the Book is a Magazine or a Book
     */
    public String getBookType() {
        //Capitalizing only the first letter and returning the modified string
        return mBookType.substring(0, 1) + mBookType.substring(1).toLowerCase();
    }

    /**
     * Setter method to store whether the Book is a Magazine or a Book
     *
     * @param bookType The Type of the Book like Magazine or a Book
     */
    public void setBookType(String bookType) {
        this.mBookType = bookType;
    }

    /**
     * Method that prepares and returns the Categories the Book belongs to
     *
     * @param fallback is the default fallback String that will be returned when no categories are found
     * @return String containing the Categories the Book belongs to
     */
    public String getCategories(String fallback) {
        int countOfCategories = (null == mCategories ? 0 : mCategories.length);

        if (countOfCategories == 1) {
            //Returning the first category if only one is present
            return TextUtils.isEmpty(mCategories[0]) ? fallback : mCategories[0];
        } else if (countOfCategories > 1) {
            //Concatenating all the categories and returning the same
            return TextUtils.join(" / ", mCategories);
        }

        //Returning the default fallback string when no categories are present
        return fallback;
    }

    /**
     * Method that returns the Book ratings
     *
     * @return Float value of the Book's ratings
     */
    public float getBookRatings() {
        return mBookRatings;
    }

    /**
     * Setter method for the Book ratings
     *
     * @param bookRatings The Book's ratings
     */
    public void setBookRatings(float bookRatings) {
        this.mBookRatings = bookRatings;
    }

    /**
     * Method that returns the Count of Ratings the Book has received
     *
     * @return String value of the Count of Ratings the Book has received
     */
    public String getBookRatingCount() {
        return String.valueOf(mBookRatingCount);
    }

    /**
     * Setter method for the Count of Ratings the Book has received
     *
     * @param bookRatingCount The count of Ratings the Book has received
     */
    public void setBookRatingCount(int bookRatingCount) {
        this.mBookRatingCount = bookRatingCount;
    }

    /**
     * Method that checks and returns whether the Book is For Sale or not in the User's Locale
     *
     * @return Boolean that returns whether the Book is For Sale or not in the User's Locale
     * <br/>Returns <b>TRUE</b> if the Book is For Sale; <b>FALSE</b> otherwise
     */
    public boolean isForSale() {
        boolean forSale = false;

        if (mSaleability.equals("FOR_SALE")) {
            forSale = true;
        } else if (mSaleability.equals("NOT_FOR_SALE")) {
            forSale = false;
        }

        return forSale;
    }

    /**
     * Method that prepares and returns the List Price of the Book in user's locale
     *
     * @return String containing the List Price of the Book in user's locale
     */
    public String getListPrice() {
        return NumberFormat.getCurrencyInstance().format(mListPrice);
    }

    /**
     * Setter method for the List Price value of the Book
     *
     * @param listPrice The List Price value of the Book
     */
    public void setListPrice(double listPrice) {
        this.mListPrice = listPrice;
    }

    /**
     * Method that prepares and returns the Retail Price of the Book in user's locale
     *
     * @return String containing the Retail Price of the Book in user's locale
     */
    public String getRetailPrice() {
        return NumberFormat.getCurrencyInstance().format(mRetailPrice);
    }

    /**
     * Setter method for the Retail Price value of the Book
     *
     * @param retailPrice The Retail Price value of the Book
     */
    public void setRetailPrice(double retailPrice) {
        this.mRetailPrice = retailPrice;
    }

    /**
     * Method that evaluates the List price & Retail Price to tell
     * whether the book is discounted or not
     *
     * @return <b>TRUE</b> when the book is discounted; <b>FALSE</b> otherwise
     */
    public boolean isDiscounted() {
        //Returning True when the Retail Price is less, indicating that it is discounted
        return mRetailPrice < mListPrice;
    }

    /**
     * Method that returns the Description of the Book
     *
     * @param fallback String containing the default message to be returned when no Description is present
     * @return String containing the Description of the Book
     */
    public String getDescription(String fallback) {
        return TextUtils.isEmpty(mDescription) ? fallback : mDescription;
    }

    /**
     * Method that returns the Link to the Image of the Book to be used in List/Grid Views
     *
     * @return String containing the Link to the Image of the Book to be used in List/Grid Views
     */
    public String getImageLinkForItemInfo() {
        return mImageLinkSmall;
    }

    /**
     * Method that returns the Link to the Image of the Book to be used in Detail view
     *
     * @return String containing the Link to the Image of the Book to be used in Detail view
     */
    public String getImageLinkForDetailInfo() {
        return mImageLinkLarge;
    }

    /**
     * Method that returns the Link to the Image of the Book to be used in the Book Image View
     *
     * @return String containing the Link to the Image of the Book to be used in the Book Image View
     */
    public String getImageLinkForBookImageInfo() {
        return mImageLinkExtraLarge;
    }

    /**
     * Method that returns the Link to the EPUB Sample of the Book that can be downloaded
     *
     * @return String containing the Link to the EPUB Sample of the Book that can be downloaded
     */
    public String getEpubLink() {
        return mEpubLink;
    }

    /**
     * Setter method for the link to Sample epub download of the Book
     *
     * @param epubLink The link to Sample epub download of the Book
     */
    public void setEpubLink(String epubLink) {
        this.mEpubLink = epubLink;
    }

    /**
     * Method that returns the Link to the PDF Sample of the Book that can be downloaded
     *
     * @return String containing the Link to the PDF Sample of the Book that can be downloaded
     */
    public String getPdfLink() {
        return mPdfLink;
    }

    /**
     * Setter method for the link to Sample pdf download of the Book
     *
     * @param pdfLink The link to Sample pdf download of the Book
     */
    public void setPdfLink(String pdfLink) {
        this.mPdfLink = pdfLink;
    }

    /**
     * Method that returns the Link to the Sample Web Preview of the Book if available or the info page
     *
     * @return String containing the Link to the Sample Web Preview of the Book
     * if available or the info page
     */
    public String getPreviewLink() {
        return mPreviewLink;
    }

    /**
     * Setter method for the link to web preview or info page
     *
     * @param previewLink The link to web preview or info page
     */
    public void setPreviewLink(String previewLink) {
        this.mPreviewLink = previewLink;
    }

    /**
     * Method that returns the Id of the Book Volume which is used
     * for comparison purpose
     *
     * @return String containing the Id of the Book Volume
     * @see BooksDiffUtility
     */
    public String getBookId() {
        return mBookId;
    }

    /**
     * Method that checks and returns whether the Book Sample is available or not
     *
     * @return Boolean that tells whether the Book Sample is available or not
     * <br/>Returns <b>TRUE</b> if available; <b>FALSE</b> otherwise
     */
    public boolean isSampleAvailable() {
        boolean isSamplePresent = false;

        if (mAccessViewStatus.equals("SAMPLE")) {
            //Sample is present when Access View Status says "SAMPLE"
            isSamplePresent = true;
        } else if (mAccessViewStatus.equals("NONE")) {
            //Sample is NOT present when Access View Status says "NONE"
            isSamplePresent = false;
        }

        //Returning the Sample availability status evaluated
        return isSamplePresent;
    }

    /**
     * Method that returns the link to Buying page of the Book
     *
     * @return String containing the link to Buying page of the Book
     */
    public String getBuyLink() {
        return mBuyLink;
    }

    /**
     * Setter method for the link to buying page
     *
     * @param buyLink The link to buying page
     */
    public void setBuyLink(String buyLink) {
        this.mBuyLink = buyLink;
    }

}
