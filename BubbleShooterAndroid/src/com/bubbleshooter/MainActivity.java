package com.bubbleshooter;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.model.BubbleShooter;

import android.os.Bundle;

public class MainActivity extends AndroidApplication {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize(new BubbleShooter(), false);
    }
}