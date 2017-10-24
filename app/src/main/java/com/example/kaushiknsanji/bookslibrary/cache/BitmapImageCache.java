package com.example.kaushiknsanji.bookslibrary.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Application level class that saves the Bitmaps downloaded in a Memory Cache
 *
 * @author Kaushik N Sanji
 */
public class BitmapImageCache {

    //Memory Cache to save the Bitmaps downloaded
    private static LruCache<String, Bitmap> mMemoryCache;

    static {
        //Static constructor invoked only on the first time when loaded into VM

        //Retrieving the current Max Memory available for the VM
        final int maxMemory = (int) Runtime.getRuntime().maxMemory() / 1024;

        //Setting the cache size to be 1/8th of the Max Memory
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            /**
             * Returns the size of the entry for {@code key} and {@code value} in
             * terms of kilobytes rather than the number of entries
             */
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                //Size of the cache now returned will be the size of the entries
                //measured in kilobytes rather than the number of entries
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    /**
     * Method that retrieves the Bitmap Image from Memory Cache for the given Image URL
     *
     * @param imageURLStr String containing the Image URL whose Bitmap needs to be retrieved from Memory Cache
     * @return Bitmap of the Image for the Image URL mentioned
     */
    public static Bitmap getBitmapFromCache(String imageURLStr) {
        return mMemoryCache.get(imageURLStr);
    }

    /**
     * Method that adds the Bitmap Image to Memory Cache with the Image URL String as the Key
     *
     * @param imageURLStr String containing the Image URL used as the Key to store in Memory Cache
     * @param bitmap      Bitmap Image downloaded from the Image URL passed
     */
    public static void addBitmapToCache(String imageURLStr, Bitmap bitmap) {
        if (getBitmapFromCache(imageURLStr) == null
                && bitmap != null) {
            mMemoryCache.put(imageURLStr, bitmap);
        }
    }

    /**
     * Method that clears the entire Memory Cache
     */
    public static void clearCache() {
        mMemoryCache.evictAll();
    }

}
