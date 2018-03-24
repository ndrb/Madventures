package com.madventures.sawmalie.madventures;


import java.util.Date;
import Util.MadventureMenuActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.DatePicker;

/**
 * Displays the itineraries associated with a specific date.
 *
 * @author Nader Baydoun
 * @author Jesse Tremblay
 * @author Joey Campanelli
 */
@SuppressWarnings("deprecation")
public class TodayActivity extends MadventureMenuActivity {
    ListView lv;
    SimpleCursorAdapter sca;
    DBHelper db;
    DatePicker dp;
    Button bu;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.today);
        context = this;
        lv = (ListView) findViewById(R.id.todayListView);
        db = DBHelper.getDBHelper(this);
        dp = (DatePicker) findViewById(R.id.dp);
        bu = (Button) findViewById(R.id.bu);

        bu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String day = dp.getYear() + "/" + dp.getMonth() + "/" + dp.getDayOfMonth();

                Log.i("H", day);
                loadLv(day);
            }
        });

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itineraryDetails((int) id);
            }
        });

        lv.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, final long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setMessage(R.string.dialog_message).setTitle(R.string.dialog_title);

                // Add the OK button
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int pos) {
                        db.deleteItinerary((int) id);
                        Date day = new Date();
                        String dateStr = (day.getYear() + 1900) + "/" + day.getMonth() + "/" + day.getDate();
                        refreshView(dateStr);
                        Log.i("Delete Succesful: ", "" + getResources().getString(R.string.delete_ok));
                    }
                });

                // Add the cancel button
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int pos) {
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

    @Override
    protected void onResume() {
        super.onResume();
        Date day = new Date();
        String dateStr = (day.getYear() + 1900) + "/" + day.getMonth() + "/" + day.getDate();
        Log.i("C", dateStr);
        loadLv(dateStr);
    }

    /**
     * Load the list view with itineraries for today
     *
     * @param day
     *            current day to load itineraries of
     */
    private void loadLv(String day) {
        String[] from = { DBHelper.BUDGETED_EXP_DESCRIPTION, DBHelper.BUDGETED_EXP_CATEGORY,
                DBHelper.BUDGETED_EXP_AMOUNT };
        int[] to = { R.id.itDesc, R.id.itCat, R.id.itAmount };
        DBHelper dbh = DBHelper.getDBHelper(this);
        Cursor c = dbh.findItinerariesForDate(day);
        sca = new SimpleCursorAdapter(this, R.layout.itinerary_view, c, from, to, 0);
        lv.setAdapter(sca);
        Log.i("CURSOR1", "" + c);
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

    /**
     * Refreshes the ListView by re-queried the DB.
     *
     */
    private void refreshView(String date) {
        // renew the cursor
        Cursor cursor = db.findItinerariesForDate(date);
        // have the adapter use the new cursor, changeCursor closes old cursor
        // too
        sca.changeCursor(cursor);
        // have the adapter tell the observers
        sca.notifyDataSetChanged();
    }
}
