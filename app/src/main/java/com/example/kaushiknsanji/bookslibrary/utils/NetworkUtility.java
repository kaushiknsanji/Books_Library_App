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
        //Flag to save the connectivity status
        boolean isNetworkConnected = false;

        if (connectivityManager != null) {
            //Retrieving current active default data network
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            //Retrieving the connectivity status
            isNetworkConnected = (activeNetworkInfo != null && activeNetworkInfo.isConnected());
        }

        //Returning the state of Internet Connectivity
        return isNetworkConnected;
    }

}
