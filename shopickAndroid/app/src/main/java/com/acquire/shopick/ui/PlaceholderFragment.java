package com.acquire.shopick.ui;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.acquire.shopick.R;

public  class PlaceholderFragment extends Fragment {

    private String text;
    AnimationDrawable rocketAnimation;

    public PlaceholderFragment() {
        text = "default constructor";
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     View rootView = inflater.inflate(R.layout.fragment_shopick, container, false);
        /*   final ImageView tView = (ImageView)rootView.findViewById(R.id.reebokPump);
        ImageButton tButton = (ImageButton)rootView.findViewById(R.id.pump_button);
        tButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                tView.setBackgroundResource(R.drawable.animation_reebok_pump);
                rocketAnimation  = (AnimationDrawable)tView.getBackground();
                rocketAnimation.stop();
                rocketAnimation.start();
            }

        });

        ImageButton zButton = (ImageButton)rootView.findViewById(R.id.zrated_button);
        zButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                tView.setBackgroundResource(R.drawable.animation_reebok_zrated);
                rocketAnimation  = (AnimationDrawable)tView.getBackground();
                rocketAnimation.stop();
                rocketAnimation.start();
            }

        });*/
        return rootView;
    }
}