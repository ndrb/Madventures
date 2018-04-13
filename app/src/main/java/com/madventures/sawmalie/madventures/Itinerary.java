package com.madventures.sawmalie.madventures;

import Util.MadventureMenuActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Main Class for Itinerary Activity that displays the Itineraries of the last
 * viewed trip or the first one in the DB.
 */
public class Itinerary extends MadventureMenuActivity {
    private SimpleCursorAdapter sca;
    private ListView lv;
    private Context context;
    private DBHelper dbh;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.itinerary);
        lv = findViewById(R.id.itListView);
        dbh = DBHelper.getDBHelper(this);
        context = this;
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int tripId = prefs.getInt("TRIPS_ID", -1);
        Log.i("SP", tripId + "");

        if (tripId != -1) {
            cursor = dbh.findItinerariesForTrip(tripId);
        }

        else
        {
            cursor = dbh.findFirstTripInDB();
            cursor = dbh.findItinerariesForTrip(Integer.parseInt(cursor.getString(0)));
        }

        String[] from = { DBHelper.BUDGETED_EXP_DESCRIPTION, DBHelper.BUDGETED_EXP_CATEGORY,
                DBHelper.BUDGETED_EXP_AMOUNT };
        int[] to = { R.id.itDesc, R.id.itCat, R.id.itAmount };


        sca = new SimpleCursorAdapter(this, R.layout.itinerary_view, cursor, from, to, 0);
        lv.setAdapter(sca);

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itineraryDetails((int) id);
            }

        });

        lv.setOnItemLongClickListener(new OnItemLongClickListener() {

            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, final long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setMessage(R.string.dialog_message).setTitle(R.string.dialog_title);

                // Add the OK button
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int pos) {
                        dbh.deleteItinerary((int) id);
                        int tripId = (int) id;
                        refreshView(tripId);
                        Log.i("Delete Succesful: ", "" + getResources().getString(R.string.delete_ok));
                    }
                });

                // Add the cancel button
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int pos) {
                        // User cancelled the dialog
                        dialog.dismiss();
                        Log.i("Delete Cancel: ", "" + getResources().getString(R.string.delete_cancel));
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }
        });
    }

    /**
     * Refreshes the ListView by re-queried the DB.
     *
     * @param tripId
     *            The id of the trip to be queried
     */
    private void refreshView(int tripId) {
        // renew the cursor
        cursor = dbh.findItinerariesForTrip(tripId);
        // have the adapter use the new cursor, changeCursor closes old cursor
        // too
        sca.changeCursor(cursor);
        // have the adapter tell the observers
        sca.notifyDataSetChanged();
    }

    /**
     * Goes into another Activity to display the Itineraries for a trip
     *
     * @param tripId
     *            Id of the trip whose Itineraries to display
     */
    public void launchItineraryActivity(int tripId) {
        Intent i = new Intent(this, ItineraryForTripActivity.class);
        i.putExtra("TRIP_ID", tripId);
        startActivity(i);
    }

    /**
     * Launches a new Activity that lets the user create new itineraries.
     *
     * @param view
     */
    public void createItineraryClicked(View view) {
        Log.i("Buttons", "Create Itinerary button clicked");
        Intent i = new Intent(this, CreateNewItinerary.class);
        startActivity(i);
    }

    /**
     * Launches a new Activity that displays the details of the selected
     * Itinerary.
     *
     * @param id
     *            Id of the itinerary whose details to display
     */
    public void itineraryDetails(int id) {
        Intent i = new Intent(this, ItineraryDetails.class);
        i.putExtra("IT_ID", id);
        startActivity(i);
    }
}
