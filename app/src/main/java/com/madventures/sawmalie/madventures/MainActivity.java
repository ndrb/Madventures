package com.madventures.sawmalie.madventures;


import Util.MadventureMenuActivity;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.pm.PackageManager;

import com.madventures.sawmalie.madventures.GSuite.Picker;

/**
 * Main Activity that displays a GridView and sets onItemClick for every
 * activity to send the user to the appropriate activity.
 */
public class MainActivity extends MadventureMenuActivity {

    // Sets constants for every activity clicked
    private static final int ITINERARY_VAL = 0;
    private static final int TODAY_VAL = 1;
    private static final int TIP_CALC_VAL = 2;
    private static final int UNIT_CONV_VAL = 3;

    private static final int WEATHER_VAL = 5;
    private static final int CUR_CONV_VAL = 6;

    private static final int MAPS_VAL = 9;
    private static final int MANAGE_TRIPS_VAL = 10;

    private TextView helloTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String name = prefs.getString("name", "");

        helloTextView = findViewById(R.id.helloTextView);
        helloTextView.setText(getString(R.string.helloUser) + name);

        GridView gridView = findViewById(R.id.gridview);
        gridView.setAdapter(new ImageAdapter(this));
        // Attach the event for selecting an image
        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("Images", "Item at position " + position + " selected");
                swapActivity(position);
            }

        });

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, "Denying Location can limit app functionality :(",Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    /**
     * Will launch the appropriate Activity that was clicked form the GridView.
     *
     * @param itemSelected
     *            The position in the GridView which was clicked
     */
    private void swapActivity(int itemSelected) {
        Intent i;

        switch (itemSelected) {
            case TIP_CALC_VAL:
                i = new Intent(this, TipCalcActivity.class);
                break;
            case UNIT_CONV_VAL:
                i = new Intent(this, MIConverter.class);
                break;
            case CUR_CONV_VAL:
                i = new Intent(this, CurrencyConverter.class);
                break;
            case WEATHER_VAL:
                i = new Intent(this, Weather.class);
                break;
            case TODAY_VAL:
                i = new Intent(this, TodayActivity.class);
                break;
            case MANAGE_TRIPS_VAL:
                i = new Intent(this, ManageTrips.class);
                break;
            case ITINERARY_VAL:
                i = new Intent(this, Itinerary.class);
                break;
            case MAPS_VAL:
                i = new Intent(this, MapsActivity.class);
                break;
            default:
                i = new Intent(this, PlaceHolder.class);
                break;
        }

        startActivity(i);
    }

}