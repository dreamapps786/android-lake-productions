package com.bubbleshooter;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.model.BubbleShooter;

/**
 * Created with IntelliJ IDEA.
 * User: Lars Davidsen
 * Date: 12-03-13
 * Time: 21:48
 * To change this template use File | Settings | File Templates.
 */
public class Activity1 extends AndroidApplication {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActionResolverAndroid actionResolverAndroid = new ActionResolverAndroid(this);
        initialize(new BubbleShooter(actionResolverAndroid), false);
        startActivity(this.getIntent());
    }
}