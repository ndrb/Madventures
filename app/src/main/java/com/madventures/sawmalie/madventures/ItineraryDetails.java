package com.madventures.sawmalie.madventures;


import Util.MadventureMenuActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

/**
 * Class that displays the details of a selected Itinerary and also permits the
 * user to directly edit the data of the itinerary.
 */
public class ItineraryDetails extends MadventureMenuActivity {

    private DatePicker dpArrived, dpDepart;
    private EditText nameArea, locDesc, city, cc, budgetAmt, desc, categ, supName, addr, actualAmt;
    private Button button;
    private DBHelper db;
    private int itId;
    private String locId;
    private CheckBox actualCB, cb;
    private boolean hasActual;
    private Spinner spin;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_itinerary);
        db = DBHelper.getDBHelper(this);
        itId = (int) getIntent().getExtras().get("IT_ID");

        dpArrived = findViewById(R.id.dpArrive);
        dpDepart = findViewById(R.id.dpDepart);

        nameArea = findViewById(R.id.loc_edit_one);

        locDesc = findViewById(R.id.loc_edit_two);
        city = findViewById(R.id.loc_edit_three);
        cc = findViewById(R.id.loc_edit_four);
        budgetAmt = findViewById(R.id.iti_edit_three);
        desc = findViewById(R.id.iti_edit_four);
        categ = findViewById(R.id.iti_edit_five);

        supName = findViewById(R.id.iti_edit_six);
        addr = findViewById(R.id.iti_edit_seven);

        actualCB = findViewById(R.id.checkBox1);
        actualCB.setEnabled(false);
        actualAmt = findViewById(R.id.iti_actual_input);

        actualAmt.setEnabled(false);

        cb = findViewById(R.id.checkBox);

        spin = findViewById(R.id.spin);
        Cursor curs = db.findAllLocations();
        SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, curs, new String[]{DBHelper.LOCATIONS_NAME}, new int[]{android.R.id.text1}, 0);
        spin.setAdapter(mAdapter);

        cb.setEnabled(false);
        spin.setEnabled(false);


        button = findViewById(R.id.button_new_itinerary);

        button.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (!nameArea.getText().toString().trim().isEmpty() && !locDesc.getText().toString().trim().isEmpty()
                        && !city.getText().toString().trim().isEmpty() && !cc.getText().toString().trim().isEmpty()
                        && !budgetAmt.getText().toString().trim().isEmpty() && !desc.getText().toString().trim().isEmpty()
                        && !categ.getText().toString().trim().isEmpty()
                        && !supName.getText().toString().trim().isEmpty()
                        && !addr.getText().toString().trim().isEmpty()) {

                    String arrived = dpArrived.getYear() + "/" + dpArrived.getMonth() + "/" + dpArrived.getDayOfMonth();
                    String depart = dpDepart.getYear() + "/" + dpDepart.getMonth() + "/" + dpDepart.getDayOfMonth();

                    if (hasActual)
                    {
                        if (!actualAmt.getText().toString().trim().isEmpty())
                        {
                            db.updateItineraryWithActual(itId, Integer.parseInt(locId), nameArea.getText().toString(),
                                    locDesc.getText().toString(), city.getText().toString(), cc.getText().toString(), arrived,
                                    depart, budgetAmt.getText().toString(), desc.getText().toString(), categ.getText().toString(),
                                    supName.getText().toString(), addr.getText().toString(), actualAmt.getText().toString());
                            finish();
                        }

                        else
                        {
                            invalidInput(v);
                        }
                    }

                    else
                    {
                        db.updateItinerary(itId, Integer.parseInt(locId), nameArea.getText().toString(),
                                locDesc.getText().toString(), city.getText().toString(), cc.getText().toString(), arrived,
                                depart, budgetAmt.getText().toString(), desc.getText().toString(), categ.getText().toString(),
                                supName.getText().toString(), addr.getText().toString());
                        finish();
                    }
                }

                else {
                    invalidInput(v);
                }
            }
        });

        // Populate Fields
        Cursor cursor = db.getItinerary(itId);

        if (cursor.moveToFirst()) {
            Log.i("C", cursor.toString());

            String dtArr = cursor.getString(cursor.getColumnIndex(DBHelper.BUDGETED_EXP_DATE_ARRIVE));
            String dtDept = cursor.getString(cursor.getColumnIndex(DBHelper.BUDGETED_EXP_DATE_DEPART));

            String[] dtArrArray = dtArr.split("/");
            int yearArr = Integer.valueOf(dtArrArray[0]);
            int monthArr = Integer.valueOf(dtArrArray[1]);
            int dayArr = Integer.valueOf(dtArrArray[2]);

            String[] dtDeptArray = dtDept.split("/");
            int yearDept = Integer.valueOf(dtDeptArray[0]);
            int monthDept = Integer.valueOf(dtDeptArray[1]);
            int dayDept = Integer.valueOf(dtDeptArray[2]);

            dpArrived.updateDate(yearArr, monthArr, dayArr);
            dpDepart.updateDate(yearDept, monthDept, dayDept);

            nameArea.setText(cursor.getString(cursor.getColumnIndex(DBHelper.LOCATIONS_NAME)));
            locDesc.setText(cursor.getString(1));
            city.setText(cursor.getString(cursor.getColumnIndex(DBHelper.LOCATIONS_CITY)));
            cc.setText(cursor.getString(cursor.getColumnIndex(DBHelper.LOCATIONS_COUNTRY_CODE)));
            budgetAmt.setText(cursor.getString(cursor.getColumnIndex(DBHelper.BUDGETED_EXP_AMOUNT)));
            desc.setText(cursor.getString(cursor.getColumnIndex(DBHelper.BUDGETED_EXP_DESCRIPTION)));
            categ.setText(cursor.getString(cursor.getColumnIndex(DBHelper.BUDGETED_EXP_CATEGORY)));
            supName.setText(cursor.getString(cursor.getColumnIndex(DBHelper.BUDGETED_EXP_NAME_OF_SUPPLIER)));
            addr.setText(cursor.getString(cursor.getColumnIndex(DBHelper.BUDGETED_EXP_ADDRESS)));

            locId = cursor.getString(cursor.getColumnIndex(DBHelper.LOCATIONS_PK_ID));

            if (db.getItineraryActual(itId) != null) {
                actualCB.setEnabled(false);
                Cursor c = db.getItineraryActual(itId);
                if (c.moveToFirst())
                    actualAmt.setText(c.getString(0));
                hasActual = true;
            }
        }

        cursor.close();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getString(R.string.title_disc));
        builder.setMessage(getString(R.string.msg_disc));
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        };
        builder.setPositiveButton(getString(R.string.yes_disc), dialogClickListener);
        builder.setNegativeButton(R.string.no_disc, null);
        builder.show();
    }

    /**
     * Pops up an alert to warn the user of invalid input.
     *
     * @param v
     *            View from where it originated
     */
    private void invalidInput(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

        builder.setTitle(getString(R.string.invalid_input_title));
        builder.setMessage(getString(R.string.invalid_input_mess));
        builder.setPositiveButton(getString(R.string.invalid_input_pos), null);
        builder.show();
    }
}
