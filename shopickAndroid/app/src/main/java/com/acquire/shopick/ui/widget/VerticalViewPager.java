package com.acquire.shopick.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Uses a combination of a PageTransformer and swapping X & Y coordinates
 * of touch events to create the illusion of a vertically scrolling ViewPager.
 *
 * Requires API 11+
 *
 */
public class VerticalViewPager extends ViewPager {

    private GestureDetector mGestureDetector;
    private boolean mIsLockOnHorizontalAxis = false;

    public VerticalViewPager(Context context) {
        super(context);
        init();
        mGestureDetector = new GestureDetector(context, new XScrollDetector());

    }

    public VerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        mGestureDetector = new GestureDetector(context, new XScrollDetector());

    }

    private void init() {
        // The majority of the magic happens here
        setPageTransformer(true, new VerticalPageTransformer());
        // The easiest way to get rid of the overscroll drawing that happens on the left and right
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    private class VerticalPageTransformer implements ViewPager.PageTransformer {

        @Override
        public void transformPage(View view, float position) {

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                view.setAlpha(1);

                // Counteract the default slide transition
                view.setTranslationX(view.getWidth() * -position);

                //set Y position to swipe in from top
                float yPosition = position * view.getHeight();
                view.setTranslationY(yPosition);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

    /**
     * Swaps the X and Y coordinates of your touch event.
     */
    private MotionEvent swapXY(MotionEvent ev) {
        float width = getWidth();
        float height = getHeight();

        float newX = (ev.getY() / height) * width;
        float newY = (ev.getX() / width) * height;

        ev.setLocation(newX, newY);

        return ev;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = super.onInterceptTouchEvent(swapXY(ev));
        swapXY(ev); // return touch coordinates to original reference frame for any child views
        return intercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // decide if horizontal axis is locked already or we need to check the scrolling direction
        if (!mIsLockOnHorizontalAxis)
            mIsLockOnHorizontalAxis = mGestureDetector.onTouchEvent(event);

        // release the lock when finger is up
        if (event.getAction() == MotionEvent.ACTION_MOVE)
            mIsLockOnHorizontalAxis = false;
        if (mIsLockOnHorizontalAxis) {
            Toast t = new Toast(getContext());
            t.setText("ldkjlfkj");
            t.show();
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                this.requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                this.requestDisallowInterceptTouchEvent(false);
                break;
        }

        getParent().requestDisallowInterceptTouchEvent(mIsLockOnHorizontalAxis);
        if (mIsLockOnHorizontalAxis) {
            return false;
        }
        return super.onTouchEvent(swapXY(event));
    }


    private class XScrollDetector extends GestureDetector.SimpleOnGestureListener {

        // -----------------------------------------------------------------------
        //
        // Methods
        //
        // -----------------------------------------------------------------------
        /**
         * @return true - if we're scrolling in X direction, false - in Y direction.
         */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return Math.abs(distanceX) > Math.abs(distanceY);
        }

    }
}
