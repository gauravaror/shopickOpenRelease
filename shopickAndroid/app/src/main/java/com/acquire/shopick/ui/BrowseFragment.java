package com.acquire.shopick.ui;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.acquire.shopick.R;
import com.acquire.shopick.model.BrandCollectionItem;

public  class BrowseFragment extends Fragment {

    private String text;
    /**
     * Create a new instance of CountingFragment, providing "num"
     * as an argument.
     */
    static BrowseFragment newInstance(int num, BrandCollectionItem item) {
        BrowseFragment f = new BrowseFragment();
        f.text = item.getName();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        args.putString("title", item.getName());
        args.putString("description",item.getTag_line());
        args.putString("photoUrl",item.getLogo_url());
        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_browse, container, false);
        TextView tView = (TextView)rootView.findViewById(R.id.browse_text);
        tView.setText(text);
        return rootView;
    }
}