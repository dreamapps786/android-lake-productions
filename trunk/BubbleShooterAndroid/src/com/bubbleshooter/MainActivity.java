package com.bubbleshooter;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.gui.BubbleShooter;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class MainActivity extends AndroidApplication {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initialize(new BubbleShooter(), false);
    }
}