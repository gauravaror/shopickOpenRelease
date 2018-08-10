package com.acquire.shopick.ui;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.widget.TextView;

import com.acquire.shopick.R;
import com.acquire.shopick.dao.User;
import com.acquire.shopick.util.AccountUtils;
import com.acquire.shopick.util.AnalyticsManager;
import com.acquire.shopick.util.FeedUtils;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.DecoDrawEffect;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;
import com.squareup.otto.Bus;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gaurav on 7/12/16.
 */

public class IntroScreenThirdFragment extends Fragment {

    @Bind(R.id.dynamicArcView)
    public DecoView mDecoView;


    @Bind(R.id.textPercentage)
    TextView textPercentage;

    @Bind(R.id.textRemaining)
    TextView textToGo;

    private int mBackIndex;
    private int mBackIndex2;
    private int mSeries1Index;
    private int mSeries2Index;

    private Bus mBus;

    private Context mContext;

    private final float mSeriesMax = 1000f;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRoot = inflater.inflate(R.layout.intro_screen_third_fragment, container, false);
        ButterKnife.bind(this, mRoot);
        AnalyticsManager.sendEvent("INTRO_SCREEN","OPENED","THIRD");
        textPercentage.setText("10 Picks");
        textToGo.setText(" 10 Monthly Picks");

        createBackSeries(mSeriesMax);
        createBackSeries2(mSeriesMax);
        createDataSeries1(mSeriesMax);
        createDataSeries2(mSeriesMax);
        createEvents();
        return mRoot;
    }

    private void createBackSeries(float max) {
        SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFE2E2E2"))
                .setRange(0, max, 0)
                .setInitialVisibility(true)
                .build();

        mBackIndex = mDecoView.addSeries(seriesItem);
    }

    private void createBackSeries2(float max) {
        SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFE2E2E2"))
                .setRange(0, max, 0)
                .setLineWidth(32f)
                .setInset(new PointF(32f, 32f))
                .setInitialVisibility(true)
                .build();

        mBackIndex2 = mDecoView.addSeries(seriesItem);
    }

    private void createDataSeries1(float max) {
        final SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFFF8800"))
                .setRange(0, max, 0)
                .setInitialVisibility(false)
                .build();

        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                float percentFilled = ((currentPosition - seriesItem.getMinValue()) / (seriesItem.getMaxValue() - seriesItem.getMinValue()));
                float picks = seriesItem.getInitialValue();
                textPercentage.setText(((int) currentPosition)+" Picks" );
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });




        mSeries1Index = mDecoView.addSeries(seriesItem);
    }

    private void createDataSeries2(float max) {
        final SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFFF4444"))
                .setRange(0, max, 0)
                .setLineWidth(32f)
                .setInset(new PointF(32f, 32f))
                .setInitialVisibility(false)
                .build();

        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {

                textToGo.setText(((int) currentPosition)+" Monthly Picks");

            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });

        mSeries2Index = mDecoView.addSeries(seriesItem);
    }


    private void createEvents() {
        mDecoView.executeReset();

        mDecoView.addEvent(new DecoEvent.Builder(mSeriesMax)
                .setIndex(mBackIndex)
                .setDuration(3000)
                .setDelay(80)
                .build());
        mDecoView.addEvent(new DecoEvent.Builder(mSeriesMax)
                .setIndex(mBackIndex2)
                .setDuration(3000)
                .setDelay(100)
                .build());


        mDecoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(mSeries1Index)
                .setDuration(2000)
                .setDelay(1250)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(400L)
                .setIndex(mSeries1Index)
                .setDelay(3250)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(mSeries2Index)
                .setDuration(1000)
                .setEffectRotations(1)
                .setDelay(7000)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(300L)
                .setIndex(mSeries2Index)
                .setDelay(8500)
                .build());


        mDecoView.addEvent(new DecoEvent.Builder(0).setIndex(mSeries2Index).setDelay(18000).build());

        mDecoView.addEvent(new DecoEvent.Builder(0)
                .setIndex(mSeries1Index)
                .setDelay(20000)
                .setDuration(1000)
                .setInterpolator(new AnticipateInterpolator())
                .setListener(new DecoEvent.ExecuteEventListener() {
                    @Override
                    public void onEventStart(DecoEvent decoEvent) {

                    }

                    @Override
                    public void onEventEnd(DecoEvent decoEvent) {

                    }
                })
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_EXPLODE)
                .setIndex(mSeries1Index)
                .setDelay(21000)
                .setDuration(3000)
                .setDisplayText("Picks!")
                .setListener(new DecoEvent.ExecuteEventListener() {
                    @Override
                    public void onEventStart(DecoEvent decoEvent) {

                    }

                    @Override
                    public void onEventEnd(DecoEvent decoEvent) {
                        createEvents();
                    }
                })
                .build());

    }

}
