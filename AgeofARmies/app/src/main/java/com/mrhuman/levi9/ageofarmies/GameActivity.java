package com.mrhuman.levi9.ageofarmies;

import android.annotation.SuppressLint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class GameActivity extends AppCompatActivity {

    public static final String WIDTH_ARG = "WIDTH_ARG";
    public static final String HEIGHT_ARG = "HEIGHT_ARG";
    public static final String SCALE_ARG = "SCALE_ARG";

    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler hideHandler = new Handler();
    private View mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        mContentView = findViewById(R.id.fullscreen_content);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        hideHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null) {
                    actionBar.hide();
                }

                hideHandler.postDelayed(new Runnable() {
                    @SuppressLint("InlinedApi")
                    @Override
                    public void run() {
                        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                    }
                }, UI_ANIMATION_DELAY);
            }
        }, 100);
    }

}
