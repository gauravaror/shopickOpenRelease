package com.acquire.shopick.model;

import android.graphics.drawable.Drawable;

/**
 * Created by gaurav on 9/15/15.
 */

public class ExploreSpinnerItem {
    public boolean isHeader() {
        return isHeader;
    }

    public void setIsHeader(boolean isHeader) {
        this.isHeader = isHeader;
    }

    boolean isHeader;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    String tag;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    int drawable;

    public int getSelectedDrawable() {
        return drawable;
    }

    public void setSelectedDrawable(int drawable) {
        this.drawable = drawable;
    }

    int drawable_unselected;

    public int getUnselectedDrawable() {
        return drawable_unselected;
    }

    public void setUnselectedDrawable(int drawable) {
        this.drawable_unselected = drawable_unselected;
    }


    String title;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isIndented() {
        return indented;
    }

    public void setIndented(boolean indented) {
        this.indented = indented;
    }

    int color;
    boolean indented;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    String category;

    public ExploreSpinnerItem(boolean isHeader,int id ,String tag, String title, boolean indented, int color, int drawable, int drawable_unselected, String Category) {
        this.isHeader = isHeader;
        this.drawable = drawable;
        this.drawable_unselected = drawable_unselected;
        this.tag = tag;
        this.id = id;
        this.title = title;
        this.indented = indented;
        this.color = color;
        this.category = Category;
    }
}