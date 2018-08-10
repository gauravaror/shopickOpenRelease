package com.acquire.shopick.io.model;

import android.text.TextUtils;

/**
 * Created by gaurav on 3/8/16.
 */
public class SearchResult {
    public String _index;
    public String _type;
    public String _id;
    public Source _source;

    @Override
    public String toString() {
        if (!TextUtils.isEmpty(_source.name)) {
        return _source.name;
        } else {
            return _source.title;
        }
    }
}