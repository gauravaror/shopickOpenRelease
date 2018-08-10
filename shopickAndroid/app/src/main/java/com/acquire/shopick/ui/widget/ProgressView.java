package com.acquire.shopick.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;

import com.acquire.shopick.R;

/**
 * Created by gaurav on 10/9/15.
 */
public class ProgressView extends ImageView {

    private  Animation anim;
    public ProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setAnimation(attrs);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAnimation(attrs);
    }

    public ProgressView(Context context) {
        super(context);
    }

    private void setAnimation(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ProgressView);
        int frameCount = a.getInt(R.styleable.ProgressView_frameCount, 12);
        int duration = a.getInt(R.styleable.ProgressView_duration, 1000);
        a.recycle();

        setAnimation(frameCount, duration);
    }

    public void setAnimation(final int frameCount, final int duration) {
        anim = AnimationUtils.loadAnimation(getContext(), R.anim.prog_anim);
        anim.setDuration(duration);
        anim.setInterpolator(new Interpolator() {

            @Override
            public float getInterpolation(float input) {
                return (float) Math.floor(input * frameCount) / frameCount;
            }
        });
        startAnimation(anim);
    }

    public void stopAnimation() {
        anim.cancel();
        anim.reset();
        setVisibility(View.GONE);
        setImageDrawable(null);
    }

    public void resetAnimation() {
        anim.cancel();
        anim.reset();
        anim.start();
    }
}
