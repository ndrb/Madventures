package com.madventures.sawmalie.madventures;


import Util.MadventureMenuActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Activity that lets the user convert from imperial to metric units and
 * vice-versa. Converting weight, height and volume are all supported. And
 * multiple units of measurment from imperial to metric are supported.
 */
public class MIConverter extends MadventureMenuActivity {

    private EditText distanceOne, distanceTwo, volumeOne, volumeTwo, weightOne, weightTwo;

    private Spinner dOne, dTwo, vOne, vTwo, wOne, wTwo;

    private final int CM = 0;
    private final int M = 1;
    private final int KM = 2;
    private final int INCHES = 0;
    private final int MILES = 1;

    private final int ML = 0;
    private final int LITRES = 1;

    private final int PINTS = 0;
    private final int GALLONS = 1;

    private final int KG = 0;
    private final int POUND = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_converter);

        distanceOne = findViewById(R.id.first_distance_edit);
        distanceTwo = findViewById(R.id.second_distance_edit);

        volumeOne = findViewById(R.id.first_volume_edit);
        volumeTwo = findViewById(R.id.second_volume_edit);

        weightOne = findViewById(R.id.first_weight_edit);
        weightTwo = findViewById(R.id.second_weight_edit);

        dOne = findViewById(R.id.distance_imperial);
        dTwo = findViewById(R.id.distance_metric);

        vOne = findViewById(R.id.volume_imperial);
        vTwo = findViewById(R.id.volume_metric);

        wOne = findViewById(R.id.weight_imperial);
        wTwo = findViewById(R.id.weight_metric);
    }

    /**
     * Convert whatever is in the first distance EditText widget into it's
     * chosen metric equivalent and displays it in the second EditText.
     *
     * @param view
     *            View from where it originated
     */
    public void convertDistanceITM(View view) {
        int di = dOne.getSelectedItemPosition();
        int dm = dTwo.getSelectedItemPosition();
        double d = 0.0;

        switch (di) {
            case CM:
                switch (dm) {
                    case INCHES:
                        // Convert cm to inches
                        if (!distanceOne.getText().toString().trim().isEmpty())
                            d = Double.valueOf(distanceOne.getText().toString()) * 0.39370;
                        break;
                    case MILES:
                        // Convert cm to miles
                        if (!distanceOne.getText().toString().trim().isEmpty())
                            d = Double.valueOf(distanceOne.getText().toString()) * 0.00000621371;
                        break;
                }
                break;
            case M:
                switch (dm) {
                    case INCHES:
                        // Convert m to inches
                        if (!distanceOne.getText().toString().trim().isEmpty())
                            d = Double.valueOf(distanceOne.getText().toString()) * 39.3701;
                        break;
                    case MILES:
                        // Convert m to miles
                        if (!distanceOne.getText().toString().trim().isEmpty())
                            d = Double.valueOf(distanceOne.getText().toString()) * 0.000621371;
                        break;
                }
                break;
            case KM:
                switch (dm) {
                    case INCHES:
                        // Convert km to inches
                        if (!distanceOne.getText().toString().trim().isEmpty())
                            d = Double.valueOf(distanceOne.getText().toString()) * 39370.1;
                        break;
                    case MILES:
                        // Convert km to miles
                        if (!distanceOne.getText().toString().trim().isEmpty())
                            d = Double.valueOf(distanceOne.getText().toString()) * 0.621371;
                        break;
                }
                break;
        }

        distanceTwo.setText(String.valueOf(d));
    }

    /**
     * Convert whatever is in the second distance EditText widget into it's
     * chosen imperial equivalent and displays it in the first EditText.
     *
     * @param view
     *            View from where it originated
     */
    public void convertDistanceMTI(View view) {
        int di = dOne.getSelectedItemPosition();
        int dm = dTwo.getSelectedItemPosition();

        double d = 0.0;

        switch (dm) {
            case INCHES:
                switch (di) {
                    case CM:
                        // Inches to cm
                        if (!distanceTwo.getText().toString().trim().isEmpty())
                            d = Double.valueOf(distanceTwo.getText().toString()) * 2.54;
                        break;
                    case M:
                        // Inches to m
                        if (!distanceTwo.getText().toString().trim().isEmpty())
                            d = Double.valueOf(distanceTwo.getText().toString()) * 0.0254;
                        break;
                    case KM:
                        // Inches to km
                        if (!distanceTwo.getText().toString().trim().isEmpty())
                            d = Double.valueOf(distanceTwo.getText().toString()) * 0.0000254;
                        break;
                }

                break;

            case MILES:
                switch (di) {
                    case CM:
                        // Miles to cm
                        if (!distanceTwo.getText().toString().trim().isEmpty())
                            d = Double.valueOf(distanceTwo.getText().toString()) * 160934;
                        break;
                    case M:
                        // Miles to m
                        if (!distanceTwo.getText().toString().trim().isEmpty())
                            d = Double.valueOf(distanceTwo.getText().toString()) * 1609.34;
                        break;
                    case KM:
                        // Miles to km
                        if (!distanceTwo.getText().toString().trim().isEmpty())
                            d = Double.valueOf(distanceTwo.getText().toString()) * 1.60934;
                        break;
                }

                break;
        }

        distanceOne.setText(String.valueOf(d));
    }

    /**
     * Convert whatever is in the first volume EditText widget into it's chosen
     * metric equivalent and displays it in the second EditText.
     *
     * @param view
     *            View from where it originated
     */
    public void convertVolumeITM(View view) {
        int vi = vOne.getSelectedItemPosition();
        int vm = vTwo.getSelectedItemPosition();

        double d = 0.0;

        switch (vi) {
            case ML:
                switch (vm) {
                    case PINTS:
                        // ml to Pints
                        if (!volumeOne.getText().toString().trim().isEmpty())
                            d = Double.valueOf(volumeOne.getText().toString()) * 0.00211338;
                        break;
                    case GALLONS:
                        // ml to Gallons
                        if (!volumeOne.getText().toString().trim().isEmpty())
                            d = Double.valueOf(volumeOne.getText().toString()) * 0.000264172;
                        break;
                }

                break;
            case LITRES:
                switch (vm) {
                    case PINTS:
                        // litres to Pints
                        if (!volumeOne.getText().toString().trim().isEmpty())
                            d = Double.valueOf(volumeOne.getText().toString()) * 2.11338;
                        break;

                    case GALLONS:
                        // litres to Gallons
                        if (!volumeOne.getText().toString().trim().isEmpty())
                            d = Double.valueOf(volumeOne.getText().toString()) * 0.264172;
                        break;
                }

                break;
        }

        volumeTwo.setText(String.valueOf(d));
    }

    /**
     * Convert whatever is in the second volume EditText widget into it's chosen
     * imperial equivalent and displays it in the first EditText.
     *
     * @param view
     *            View from where it originated
     */
    public void convertVolumeMTI(View view) {
        int vi = vOne.getSelectedItemPosition();
        int vm = vTwo.getSelectedItemPosition();

        double d = 0.0;

        switch (vm) {
            case PINTS:
                switch (vi) {
                    case ML:
                        // pints to ml
                        if (!volumeTwo.getText().toString().trim().isEmpty())
                            d = Double.valueOf(volumeTwo.getText().toString()) * 473.176;
                        break;
                    case LITRES:
                        // pints to litres
                        if (!volumeTwo.getText().toString().trim().isEmpty())
                            d = Double.valueOf(volumeTwo.getText().toString()) * 0.473176;
                        break;
                }

                break;
            case GALLONS:
                switch (vi) {
                    case ML:
                        // gallons to ml
                        if (!volumeTwo.getText().toString().trim().isEmpty())
                            d = Double.valueOf(volumeTwo.getText().toString()) * 3785.41;
                        break;

                    case LITRES:
                        // gallons to litres
                        if (!volumeTwo.getText().toString().trim().isEmpty())
                            d = Double.valueOf(volumeTwo.getText().toString()) * 3.78541;
                        break;
                }

                break;
        }

        volumeOne.setText(String.valueOf(d));
    }

    /**
     * Convert whatever is in the first weight EditText widget into it's chosen
     * metric equivalent and displays it in the second EditText.
     *
     * @param view
     *            View from where it originated
     */
    public void convertWeightITM(View view) {

        int wi = wOne.getSelectedItemPosition();
        int wm = wTwo.getSelectedItemPosition();
        double d = 0.0;

        switch (wi) {
            case KG:
                switch (wm) {
                    case POUND:
                        // kg to pound
                        if (!weightOne.getText().toString().trim().isEmpty())
                            d = Double.valueOf(weightOne.getText().toString()) * 2.20462;
                        break;
                }
                break;
        }

        weightTwo.setText(String.valueOf(d));
    }

    /**
     * Convert whatever is in the second weight EditText widget into it's chosen
     * imperial equivalent and displays it in the first EditText.
     *
     * @param view
     *            View from where it originated
     */
    public void convertWeightMTI(View view) {
        int wi = wOne.getSelectedItemPosition();
        int wm = wTwo.getSelectedItemPosition();
        double d = 0.0;

        switch (wm) {
            case POUND:
                switch (wi) {
                    case KG:
                        // pound to kg
                        if (!weightTwo.getText().toString().trim().isEmpty())
                            d = Double.valueOf(weightTwo.getText().toString()) * 0.453592;
                        break;
                }
                break;
        }
        weightOne.setText(String.valueOf(d));
    }

}

