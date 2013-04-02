package com.bubbleshooter;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.model.BubbleShooter;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import com.scoreloop.client.android.core.model.Client;
import com.scoreloop.client.android.ui.ScoreloopManagerSingleton;

public class MainActivity extends Application {
    /** Called when the activity is first created. */

    private static Client client;
    private static Context _android_game_context;

    static void init(final Context android_game_context) {
        if (client == null) {
            client = new Client(android_game_context,
                    "bpROao+wDpxM9Xy1b3aKk2Krr9WcEiM6nxSgQqvs7jrDX638FnPiIw==",null);
            _android_game_context = android_game_context;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init(this);
        ScoreloopManagerSingleton.init(_android_game_context,
                "bpROao+wDpxM9Xy1b3aKk2Krr9WcEiM6nxSgQqvs7jrDX638FnPiIw==");
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //initialize(new BubbleShooter(actionResolverAndroid), false);
        Intent i = new Intent(this, Activity1.class);
        boolean fixMe = false;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ScoreloopManagerSingleton.destroy();
    }
}