package com.acquire.shopick.event;

import com.acquire.shopick.dao.Categories;

import java.util.ArrayList;

/**
 * Created by gaurav on 1/22/16.
 */
public class CategoriesLoadedEvent {
    ArrayList<Categories> presentations;

    public CategoriesLoadedEvent(ArrayList<Categories> brandsLoaded) {
        this.presentations = brandsLoaded;
    }

    public ArrayList<Categories> getCategories() {
        return presentations;
    }

}
