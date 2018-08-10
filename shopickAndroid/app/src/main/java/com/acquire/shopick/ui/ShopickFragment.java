/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.acquire.shopick.ui;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acquire.shopick.R;

import static com.acquire.shopick.util.LogUtils.LOGD;
import static com.acquire.shopick.util.LogUtils.makeLogTag;

public class ShopickFragment extends ListFragment {
    private static final String TAG = makeLogTag(ShopickFragment.class);

    private String mContentDescription = null;
    private View mRoot = null;


    public interface Listener {
        public void onFragmentViewCreated(Fragment fragment);
        public void onFragmentAttached(Fragment fragment);
        public void onFragmentDetached(Fragment fragment);
        public void onLoadedDetached(Fragment fragment);

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_shopick, container, false);
        if (mContentDescription != null) {
            mRoot.setContentDescription(mContentDescription);
        }
        return mRoot;
    }


    public void setContentDescription(String desc) {
        mContentDescription = desc;
        if (mRoot != null) {
            mRoot.setContentDescription(mContentDescription);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() instanceof Listener) {
            ((Listener) getActivity()).onFragmentViewCreated(this);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (getActivity() instanceof Listener) {
            ((Listener) getActivity()).onFragmentAttached(this);
        }

    }
    public  void setContentTopClearance(int clearance) {
        getView().setPadding(getView().getPaddingLeft(),clearance,getView().getPaddingRight(),getView().getPaddingBottom());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (getActivity() instanceof Listener) {
            ((Listener) getActivity()).onFragmentDetached(this);
        }
    }

}
