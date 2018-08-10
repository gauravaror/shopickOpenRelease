package com.acquire.shopick.ui;


import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.acquire.shopick.Config;
import com.acquire.shopick.R;
import com.acquire.shopick.io.model.SearchResult;
import com.acquire.shopick.io.model.Source;
import com.acquire.shopick.io.network.ShopickApi;
import com.acquire.shopick.util.AccountUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

import static com.acquire.shopick.util.LogUtils.LOGE;

/**
 * Created by akshay on 1/2/15.
 */
public class SearchResultAdapter extends ArrayAdapter<SearchResult> implements Filterable {
    Context context;
    int resource, textViewResourceId;
    List<SearchResult> items;
    ShopickApi.SearchService service;
    private ArrayList<SearchResult> mData;
    int type;

    public SearchResultAdapter(Context context, int resource, int textViewResourceId, int type) {
        super(context, resource, textViewResourceId);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        mData = new ArrayList<SearchResult>();
        this.type = type;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public SearchResult getItem(int index) {
        return mData.get(index);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }
        SearchResult people = mData.get(position);
        if (people != null) {
            TextView lblName = (TextView) view.findViewById(android.R.id.text1);
            lblName.setTextColor(context.getResources().getColor(R.color.body_text_1));
            if (lblName != null)
                if (!TextUtils.isEmpty(people._source.name)) {
                    lblName.setText(people._source.name);
                } else {
                    lblName.setText(people._source.title);
                }
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if(constraint != null) {
                // A class that queries a web API, parses the data and returns an ArrayList<Style>
                Retrofit retrofit = new Retrofit.Builder()
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .baseUrl(Config.PROD_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                service = retrofit.create(ShopickApi.SearchService.class);
                try {
                    Call<ArrayList<SearchResult>> call_search;
                    if (type == 0 ) {
                        call_search = service.searchShopickLocation(constraint.toString(), AccountUtils.getRequestMap(context));
                    } else if (type == 1) {
                        call_search = service.searchShopickType(constraint.toString(), AccountUtils.getRequestMap(context));
                    } else {
                        call_search = service.searchShopickPostCollection(constraint.toString(), AccountUtils.getRequestMap(context));
                    }
                    mData = call_search.execute().body();
                }
                catch(Exception e) {
                    LOGE("myException", e.getMessage());
                }
                // Now assign the values and count to the FilterResults object
                filterResults.values = mData;
                filterResults.count = mData.size();
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence contraint, FilterResults results) {
            if(results != null && results.count > 0) {
                notifyDataSetChanged();
            }
            else {
                notifyDataSetInvalidated();
            }
        }
    };

}