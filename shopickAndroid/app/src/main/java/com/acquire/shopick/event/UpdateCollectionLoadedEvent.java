package com.acquire.shopick.event;

import com.acquire.shopick.io.model.Brand;
import com.acquire.shopick.io.model.Product;

import java.util.ArrayList;

/**
 * Created by gaurav on 10/19/15.
 */
public class UpdateCollectionLoadedEvent {
    ArrayList<Product> products;

    public UpdateCollectionLoadedEvent(ArrayList<Product> productsLoaded) {
        this.products = productsLoaded;
    }

    public ArrayList<Product> getCollection() {
        return products;
    }

}
