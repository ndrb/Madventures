package com.madventures.sawmalie.madventures;

import Util.MadventureMenuActivity;
import android.os.Bundle;
import android.view.View;

/**
 * About Activity that displays a simple Activity with click-able labels that
 * redirect to different web-sites by opening a web browser using Intents.
 */
public class AboutActivity extends MadventureMenuActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
}
