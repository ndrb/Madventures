package com.madventures.sawmalie.madventures;


import Util.MadventureMenuActivity;
import Util.logger.Log;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

/**
 * Activity that lets users create new Itinerary items.
 */
public class CreateNewItinerary extends MadventureMenuActivity {

    private DatePicker dpArrived, dpDepart;
    private EditText nameArea, locDesc, city, cc, budgetAmt, desc, categ, supName, addr, actualAmt;
    private Button button;
    private DBHelper db;
    private int tripId;
    private CheckBox actualCB, cb;
    private Spinner spin;
    private View vw;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_itinerary);

        db = DBHelper.getDBHelper(this);

        dpArrived = (DatePicker) findViewById(R.id.dpArrive);
        dpDepart = (DatePicker) findViewById(R.id.dpDepart);

        nameArea = (EditText) findViewById(R.id.loc_edit_one);

        locDesc = (EditText) findViewById(R.id.loc_edit_two);
        city = (EditText) findViewById(R.id.loc_edit_three);
        cc = (EditText) findViewById(R.id.loc_edit_four);
        budgetAmt = (EditText) findViewById(R.id.iti_edit_three);
        desc = (EditText) findViewById(R.id.iti_edit_four);
        categ = (EditText) findViewById(R.id.iti_edit_five);

        supName = (EditText) findViewById(R.id.iti_edit_six);
        addr = (EditText) findViewById(R.id.iti_edit_seven);

        actualCB = (CheckBox) findViewById(R.id.checkBox1);
        actualAmt = (EditText) findViewById(R.id.iti_actual_input);

        cb = (CheckBox) findViewById(R.id.checkBox);
        spin = (Spinner) findViewById(R.id.spin);
        Cursor curs = db.findAllLocations();
        SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, curs, new String[]{DBHelper.LOCATIONS_NAME}, new int[]{android.R.id.text1}, 0);
        spin.setAdapter(mAdapter);

        button = (Button) findViewById(R.id.button_new_itinerary);

        button.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                vw = v;

                if(cb.isChecked())
                {

                    if (!budgetAmt.getText().toString().trim().isEmpty()
                            && !desc.getText().toString().trim().isEmpty()
                            && !categ.getText().toString().trim().isEmpty()
                            && !supName.getText().toString().trim().isEmpty()
                            && !addr.getText().toString().trim().isEmpty())
                    {
                        complete();
                    }

                    else
                    {
                        if(budgetAmt.getText().toString().trim().isEmpty())
                            budgetAmt.setText("0");
                        if(desc.getText().toString().trim().isEmpty())
                            desc.setText(desc.getHint());
                        if(categ.getText().toString().trim().isEmpty())
                            categ.setText(categ.getHint());
                        if(supName.getText().toString().trim().isEmpty())
                            supName.setText(supName.getHint());
                        if(addr.getText().toString().trim().isEmpty())
                            addr.setText(addr.getHint());

                        complete();
                    }
                }

                else
                {

                    if (!nameArea.getText().toString().trim().isEmpty()
                            && !locDesc.getText().toString().trim().isEmpty()
                            && !city.getText().toString().trim().isEmpty()
                            && !cc.getText().toString().trim().isEmpty()
                            && !budgetAmt.getText().toString().trim().isEmpty()
                            && !desc.getText().toString().trim().isEmpty()
                            && !categ.getText().toString().trim().isEmpty()
                            && !supName.getText().toString().trim().isEmpty()
                            && !addr.getText().toString().trim().isEmpty())
                    {
                        complete();
                    }


                    else
                    {
                        if(nameArea.getText().toString().trim().isEmpty())
                            invalidInput(v);
                        else
                        {
                            if(locDesc.getText().toString().trim().isEmpty())
                                locDesc.setText(locDesc.getHint());
                            if(city.getText().toString().trim().isEmpty())
                                city.setText(city.getHint());
                            if(cc.getText().toString().trim().isEmpty())
                                cc.setText(cc.getHint());
                            if(budgetAmt.getText().toString().trim().isEmpty())
                                budgetAmt.setText("0");
                            if(desc.getText().toString().trim().isEmpty())
                                desc.setText(desc.getHint());
                            if(categ.getText().toString().trim().isEmpty())
                                categ.setText(categ.getHint());
                            if(supName.getText().toString().trim().isEmpty())
                                supName.setText(supName.getHint());
                            if(addr.getText().toString().trim().isEmpty())
                                addr.setText(addr.getHint());
                            complete();
                        }
                    }
                }
            }
        });

        if (getIntent().getExtras() != null)
        {
            tripId = getIntent().getExtras().getInt("TRIP_ID");
        }

        else
        {
            tripId = 1;
        }

    }

    private void complete()
    {
        String arrived = dpArrived.getYear() + "/" + dpArrived.getMonth() + "/" + dpArrived.getDayOfMonth();
        String depart = dpDepart.getYear() + "/" + dpDepart.getMonth() + "/" + dpDepart.getDayOfMonth();


        if (actualCB.isChecked())
        {
            if (!actualAmt.getText().toString().trim().isEmpty())
            {
                if(!cb.isChecked())
                {
                    db.insertNewItineraryWithActual(tripId, nameArea.getText().toString(), locDesc.getText().toString(),
                            city.getText().toString(), cc.getText().toString(), arrived, depart,
                            budgetAmt.getText().toString(), desc.getText().toString(), categ.getText().toString(),
                            supName.getText().toString(), addr.getText().toString(), actualAmt.getText().toString());
                    finish();
                }

                else
                {
                    Cursor cur = db.findAllLocations();

                    int position = spin.getSelectedItemPosition();
                    cur.moveToPosition(position);
                    int id = cur.getInt(cur.getColumnIndex(DBHelper.LOCATIONS_PK_ID));

                    db.insertNewItineraryWithActualPresetLocation(tripId, id, arrived, depart,
                            budgetAmt.getText().toString(), desc.getText().toString(), categ.getText().toString(),
                            supName.getText().toString(), addr.getText().toString(), actualAmt.getText().toString());
                    finish();
                }
            }

            else
            {
                invalidInput(vw);
            }
        }

        else
        {
            if(!cb.isChecked())
            {

                db.insertNewItinerary(tripId, nameArea.getText().toString(), locDesc.getText().toString(),
                        city.getText().toString(), cc.getText().toString(), arrived, depart,
                        budgetAmt.getText().toString(), desc.getText().toString(), categ.getText().toString(),
                        supName.getText().toString(), addr.getText().toString());
                finish();
            }

            else
            {
                Cursor cur = db.findAllLocations();

                int position = spin.getSelectedItemPosition();
                cur.moveToPosition(position);
                int id = cur.getInt(cur.getColumnIndex(DBHelper.LOCATIONS_PK_ID));



                db.insertNewItineraryPresetLocation(tripId, id, arrived, depart,
                        budgetAmt.getText().toString(), desc.getText().toString(), categ.getText().toString(),
                        supName.getText().toString(), addr.getText().toString());
                finish();
            }
        }
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

    public void enableActualField(View v)
    {
        actualAmt.setEnabled(!actualAmt.isEnabled());
    }
}