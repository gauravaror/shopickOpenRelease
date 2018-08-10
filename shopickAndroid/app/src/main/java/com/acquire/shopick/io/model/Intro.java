package com.acquire.shopick.io.model;

/**
 * Created by gaurav on 10/31/15.
 */
public class Intro {
    public int id;

    public Intro(int id, String title, String desc, int drawable) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.drawable = drawable;

    }

    public String title;
    public String desc;
    public int drawable;
    public int imageUrl;
}
