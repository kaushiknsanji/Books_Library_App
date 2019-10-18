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

package com.example.kaushiknsanji.bookslibrary.workers;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.widget.ImageView;

import com.example.kaushiknsanji.bookslibrary.R;
import com.example.kaushiknsanji.bookslibrary.cache.BitmapImageCache;

/**
 * {@link Fragment} class that manages the downloading of the Book Images
 * required by the list/grid, detail and book image views.
 * <p>
 * <p>Images are downloaded only when not present in the {@link BitmapImageCache}</p>
 * <p>Images retrieved are updated to the corresponding ImageView reference passed</p>
 *
 * @author Kaushik N Sanji
 */
public class ImageDownloaderFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Bitmap> {

    //Constant used for logs and Fragment Tag
    private static final String LOG_TAG = ImageDownloaderFragment.class.getSimpleName();

    //Stores the ImageView component that needs to be updated when the Image is downloaded
    private ImageView mImageView;

    //Stores the Image URL whose Image is to be downloaded
    private String mImageURLStr;

    /**
     * Static Constructor of the Fragment
     *
     * @param fragmentManager FragmentManager that manages the Fragments
     * @param tagId           The integer position that identifies the view being inflated in the Adapter
     * @return Instance of the Fragment
     */
    public static ImageDownloaderFragment newInstance(FragmentManager fragmentManager, int tagId) {
        //Creating the Fragment Tag string using the tagId
        String fragmentTagStr = LOG_TAG + "_" + tagId;

        //Retrieving the Fragment from the FragmentManager if existing
        ImageDownloaderFragment imageDownloaderFragment
                = (ImageDownloaderFragment) fragmentManager.findFragmentByTag(fragmentTagStr);
        if (imageDownloaderFragment == null) {
            //When the Fragment is being added for the first time

            //Instantiating the Fragment
            imageDownloaderFragment = new ImageDownloaderFragment();
            //Adding the Fragment to Transaction
            fragmentManager.beginTransaction().add(imageDownloaderFragment, fragmentTagStr).commit();
        }

        //Returning the Fragment instance
        return imageDownloaderFragment;
    }

    /**
     * Method that loads the Image from Memory Cache or downloads the Image from the URL passed
     * if necessary
     *
     * @param imageView   The ImageView Component on which the Image needs to be updated
     * @param imageURLStr String containing the Image URL whose Image needs to be downloaded and updated
     * @param loaderId    Integer identifier used while creating this Fragment
     */
    public void executeAndUpdate(ImageView imageView, String imageURLStr, int loaderId) {
        //Saving the parameters passed
        mImageView = imageView;
        mImageURLStr = imageURLStr;

        //Looking up for the Image in Memory Cache for the given URL
        Bitmap bitmap = BitmapImageCache.getBitmapFromCache(mImageURLStr);
        if (bitmap != null) {
            //When Bitmap image was present in Memory Cache, update the ImageView
            mImageView.setImageBitmap(bitmap);
        } else {
            //Resetting the ImageView to the default Book Image for lazy loading
            mImageView.setImageResource(R.drawable.ic_book);

            //Starting the download when Bitmap image is not available from Memory Cache: START
            LoaderManager loaderManager = ((FragmentActivity) mImageView.getContext()).getSupportLoaderManager();
            boolean isNewImageURLStr = false; //Boolean to check if we need to restart the loader
            Loader<Bitmap> loader = loaderManager.getLoader(loaderId); //Getting the loader at the loaderId
            if (loader instanceof ImageDownloader) {
                //Validating the loader and casting to ImageDownloader
                ImageDownloader imageDownloader = (ImageDownloader) loader;
                //Checking for inequality of the Image URL with the one from the loader
                isNewImageURLStr = !mImageURLStr.equals(imageDownloader.getImageURLStr());
            }

            if (isNewImageURLStr) {
                //Restarting the Loader when the ImageURL is new
                loaderManager.restartLoader(loaderId, null, this);
            } else {
                //Invoking the Loader AS-IS if the ImageURL is the same
                //or if the Loader is not yet registered with the loaderId passed
                loaderManager.initLoader(loaderId, null, this);
            }
            //Starting the download when Bitmap image is not available from Memory Cache: END
        }
    }

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @NonNull
    @Override
    public Loader<Bitmap> onCreateLoader(int id, Bundle args) {
        //Returning an Instance of ImageDownloader to start the Image download
        return new ImageDownloader(mImageView.getContext(), mImageURLStr);
    }

    /**
     * Called when a previously created loader has finished its load.
     * This is where we display the Bitmap image generated by the loader
     *
     * @param loader      The Loader that has finished.
     * @param bitmapImage The Bitmap Image generated by the Loader.
     */
    @Override
    public void onLoadFinished(@NonNull Loader<Bitmap> loader, Bitmap bitmapImage) {
        if (bitmapImage != null && mImageView != null) {
            //Updating the ImageView when the Bitmap is downloaded successfully
            mImageView.setImageBitmap(bitmapImage);
        } else if (mImageView != null) {
            //Resetting the ImageView to the default Book Image when the Bitmap failed to download
            mImageView.setImageResource(R.drawable.ic_book);
        }
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(@NonNull Loader<Bitmap> loader) {
        //Resetting the ImageView to the default Book Image
        mImageView.setImageResource(R.drawable.ic_book);
    }

}