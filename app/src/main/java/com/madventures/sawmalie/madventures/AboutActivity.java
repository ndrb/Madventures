package com.madventures.sawmalie.madventures;

import Util.MadventureMenuActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

/**
 * About Activity that displays a simple Activity with click-able labels that
 * redirect to different web-sites by opening a web browser using Intents.
 */
public class AboutActivity extends MadventureMenuActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView tx1 = findViewById(R.id.desc_aboot);
        tx1.setMovementMethod(new ScrollingMovementMethod());
    }
}
