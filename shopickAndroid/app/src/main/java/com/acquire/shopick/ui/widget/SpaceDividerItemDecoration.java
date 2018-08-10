package com.acquire.shopick.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.acquire.shopick.R;
/**
 * Created by gaurav on 6/5/16.
 */

public class SpaceDividerItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;
    private int space;
    private boolean sides;

    public SpaceDividerItemDecoration(Context context, int space, boolean sides) {
        mDivider = context.getResources().getDrawable(R.drawable.line_divider);
        this.space = space;
        this.sides = sides;

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = space;
        if (sides) {
            outRect.left = space;
            outRect.right = space;
        }
        // Add top margin only for the first item to avoid double space between items
        if(parent.getChildPosition(view) == 0)
            outRect.top = space;
    }
}
