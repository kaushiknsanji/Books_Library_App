package com.example.kaushiknsanji.bookslibrary.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Utility class that deals with the Network related stuff
 *
 * @author Kaushik N Sanji
 */
public class NetworkUtility {

    //Constant used for Logs
    private static final String LOG_TAG = NetworkUtility.class.getSimpleName();

    /**
     * Method that evaluates the state of Internet Connectivity
     *
     * @param context is the Context of the Application
     * @return a Boolean representing the state of Internet Connectivity
     * <br/><b>TRUE</b> if the Internet Connectivity is established
     * <br/><b>FALSE</b> otherwise
     */
    public static boolean isNetworkConnected(Context context) {
        //Retrieving the Connectivity Manager from the Context
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //Retrieving the current active default data network
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        //Checking the connectivity status and returning its state
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
