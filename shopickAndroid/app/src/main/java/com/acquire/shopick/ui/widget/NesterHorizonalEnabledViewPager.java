package com.acquire.shopick.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by gaurav on 9/25/15.
 */
public class NesterHorizonalEnabledViewPager extends ViewPager {

    private GestureDetector mGestureDetector;
    private boolean mIsLockOnHorizontalAxis = false;

    public NesterHorizonalEnabledViewPager(Context context) {
        super(context);
        mGestureDetector = new GestureDetector(context, new XScrollDetector());

    }
    public NesterHorizonalEnabledViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(context, new XScrollDetector());

    }

    @Override public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = super.onInterceptTouchEvent(ev);
        if (!mIsLockOnHorizontalAxis)
            mIsLockOnHorizontalAxis = mGestureDetector.onTouchEvent(ev);

        // release the lock when finger is up
        if (ev.getAction() == MotionEvent.ACTION_MOVE)
            mIsLockOnHorizontalAxis = false;

         if (mIsLockOnHorizontalAxis) {
             return true;
         } else {
             return intercepted;
         }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean intercepted = super.onTouchEvent(event);
        if (!mIsLockOnHorizontalAxis)
            mIsLockOnHorizontalAxis = mGestureDetector.onTouchEvent(event);

        // release the lock when finger is up
        if (event.getAction() == MotionEvent.ACTION_MOVE)
            mIsLockOnHorizontalAxis = false;
        getParent().requestDisallowInterceptTouchEvent(mIsLockOnHorizontalAxis);

        return intercepted;
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
