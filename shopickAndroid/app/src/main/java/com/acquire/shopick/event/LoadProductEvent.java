package com.acquire.shopick.event;

/**
 * Created by gaurav on 10/27/15.
 */
public class LoadProductEvent {
    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }


    public LoadProductEvent(String product_id) {
        this.product_id = product_id;
    }

    String product_id;

}
