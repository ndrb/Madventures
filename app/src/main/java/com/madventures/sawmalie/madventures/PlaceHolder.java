package com.madventures.sawmalie.madventures;


import Util.MadventureMenuActivity;
import android.os.Bundle;

/**
 * A placeholder Activity that informs the user that the certain activity they
 * clicked on is still under development.
 */
public class PlaceHolder extends MadventureMenuActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_holder_view);
    }
}
