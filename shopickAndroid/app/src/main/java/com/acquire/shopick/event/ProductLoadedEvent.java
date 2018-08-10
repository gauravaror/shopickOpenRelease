package com.acquire.shopick.event;


import com.acquire.shopick.io.model.Product;

/**
 * Created by gaurav on 10/27/15.
 */
public class ProductLoadedEvent {
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Product product;

    public ProductLoadedEvent(Product product) {
        this.product = product;
    }
}
