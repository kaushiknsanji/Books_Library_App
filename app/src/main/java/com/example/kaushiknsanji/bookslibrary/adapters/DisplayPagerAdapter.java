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

package com.example.kaushiknsanji.bookslibrary.adapters;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kaushiknsanji.bookslibrary.R;
import com.example.kaushiknsanji.bookslibrary.adapterviews.RecyclerViewFragment;

import java.util.Set;

/**
 * Provides the appropriate Fragment for the ViewPager
 *
 * @author Kaushik N Sanji
 */
public class DisplayPagerAdapter extends FragmentStatePagerAdapter {

    //Constant for the number of views available
    private static final int TOTAL_VIEW_COUNT = 2;

    //Saves the reference to Application Context
    private Context mContext;

    //Sparse Array to keep track of the registered fragments in memory
    private SparseArray<Fragment> mRegisteredFragments = new SparseArray<>();

    //Stores a reference to the FragmentManager used
    private FragmentManager mFragmentManager;

    /**
     * Constructor for the PagerAdapter
     *
     * @param fm      is the FragmentManager to be used for managing the Fragments
     * @param context is the Application Context
     */
    public DisplayPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mFragmentManager = fm;
        this.mContext = context;
    }

    /**
     * Returns the Fragment associated with a specified position.
     *
     * @param position is the position of tab/fragment in the Pager
     */
    @Override
    public Fragment getItem(int position) {
        //Selecting the Fragment based on position
        switch (position) {
            case 0: //First view will be the List View
                return RecyclerViewFragment.newInstance(RecyclerViewFragment.LIST_MODE);
            case 1: //Second view will be the Grid View
                return RecyclerViewFragment.newInstance(RecyclerViewFragment.GRID_MODE);
            default:
                return null;
        }
    }

    //Registers the Fragment when the item is instantiated (for the first time) using #getItem
    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        mRegisteredFragments.put(position, fragment);
        return fragment;
    }

    //Unregisters the Fragment when the item is inactive
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        mRegisteredFragments.delete(position);
        super.destroyItem(container, position, object);
    }

    /**
     * Returns the registered fragment at the position
     *
     * @param position is the index of the Fragment shown in the ViewPager
     * @return Instance of the Active Fragment at the position if present; else Null
     */
    @Nullable
    public Fragment getRegisteredFragment(int position) {
        return mRegisteredFragments.get(position);
    }

    /**
     * Overriding to restore the state of Registered Fragments array
     *
     * @param state  is the Parcelable state
     * @param loader is the ClassLoader required for restoring the state
     */
    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        super.restoreState(state, loader);
        if (state != null) {
            //When the state is present
            Bundle bundle = (Bundle) state;
            //Setting the ClassLoader passed, onto the Bundle
            bundle.setClassLoader(loader);

            //Retrieving the keys used in Bundle
            Set<String> keyStringSet = bundle.keySet();
            //Iterating over the Keys to find the Fragments
            for (String keyString : keyStringSet) {
                if (keyString.startsWith("f")) {
                    //Fragment keys starts with 'f' followed by its position index
                    int position = Integer.parseInt(keyString.substring(1));
                    //Getting the Fragment from the Bundle using the Key through the FragmentManager
                    Fragment fragment = mFragmentManager.getFragment(bundle, keyString);
                    if (fragment != null) {
                        //If Fragment is valid, then update the Sparse Array of Registered Fragments
                        mRegisteredFragments.put(position, fragment);
                    }
                }
            }
        }
    }

    /**
     * Returns the number of views available.
     */
    @Override
    public int getCount() {
        return TOTAL_VIEW_COUNT;
    }

    /**
     * Method that inflates the template layout ('R.layout.icon_tab_layout_template') for the Tabs
     * and prepares the layout with the correct tab icon for the position requested
     *
     * @param position is the position of tab/fragment in the Pager
     * @return Prepared custom layout to be used for the tab at position
     */
    @NonNull
    public View getTabView(int position) {
        //Inflating the template Icon Tab Layout ('R.layout.icon_tab_layout_template') at position
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.icon_tab_layout_template, null);

        //Finding the Icon ImageView to set its icon
        ImageView iconImageView = rootView.findViewById(R.id.tab_image_icon_id);

        //Finding the TextView to set its Title
        TextView tabTextView = rootView.findViewById(R.id.tab_text_id);

        //Setting the Icon and the Text based on the current position
        switch (position) {
            case 0: //For List View
                iconImageView.setImageResource(R.drawable.list_tab_icon_state_list);
                tabTextView.setText(mContext.getString(R.string.list_tab_title));
                break;
            case 1: //For Grid View
                iconImageView.setImageResource(R.drawable.grid_tab_icon_state_list);
                tabTextView.setText(mContext.getString(R.string.grid_tab_title));
                break;
        }

        //Returning the prepared Icon Tab Layout
        return rootView;
    }

}
