package com.madventures.sawmalie.madventures;


import Util.MadventureMenuActivity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * Activity that lets users calculate tip amounts by giving the full price of
 * their bill, choosing the tip percentage (and optionally the number of people
 * to split the bill by for Going Dutch).
 */
public class TipCalcActivity extends MadventureMenuActivity {
    private RadioGroup radioGroup;
    private Button calcTip;
    private EditText billTotalEdit, tipEdit, numOfPpl;
    private TextView finalTip, finalTotal, perPersonTotal;
    private RadioButton radioButton;
    private double tipPercentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_calc);

        billTotalEdit = (EditText) findViewById(R.id.bill_edit);

        tipEdit = (EditText) findViewById(R.id.tip_edit);

        numOfPpl = (EditText) findViewById(R.id.numbers_for_humans_edit);

        calcTip = (Button) findViewById(R.id.calc_tip);

        finalTip = (TextView) findViewById(R.id.final_tip_result);

        finalTotal = (TextView) findViewById(R.id.total_label_result);

        perPersonTotal = (TextView) findViewById(R.id.total_per_person_result);

        radioGroup = (RadioGroup) findViewById(R.id.radio_group);

        if (savedInstanceState != null)
        {
            CharSequence savedText = savedInstanceState.getCharSequence("VAL1");
            finalTip.setText(savedText);

            savedText = savedInstanceState.getCharSequence("VAL2");
            finalTotal.setText(savedText);

            savedText = savedInstanceState.getCharSequence("VAL3");
            perPersonTotal.setText(savedText);
        }

        addTipCalc();
    }

    @Override
    protected void onSaveInstanceState (Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putCharSequence("VAL1", finalTip.getText());
        outState.putCharSequence("VAL2", finalTotal.getText());
        outState.putCharSequence("VAL3", perPersonTotal.getText());
    }

    /**
     * Retrieves the data from the EditText widgets and performs the necessary
     * error validation and displays the results of the calculations.
     */
    private void addTipCalc() {

        calcTip.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputBill = billTotalEdit.getText().toString();
                double t = 0;

                // Checks if the input is empty to prevent an empty String from
                // being converted into a do
                if (inputBill.trim().equals("")) {
                    invalidInput(v);
                }

                else {
                    // Stores the value entered as a double
                    t = Double.valueOf(billTotalEdit.getText().toString());

                    if (tipPercentage < 1) {
                        if (tipEdit.getText().toString().trim().equals("")) {
                            // Creates a pop-up to notify the user of invalid
                            // input
                            invalidInput(v);
                        }

                        else {
                            // Does the necessary tip calculations and displays
                            // them
                            tipPercentage = Double.valueOf(tipEdit.getText().toString());
                            double f = ((tipPercentage * t) / 100);
                            finalTip.setText("" + f);
                            finalTotal.setText("" + (f + t));

                            String numPpl = numOfPpl.getText().toString();

                            if (numPpl.trim().equals("")) {
                                numOfPpl.setText("1");
                                numPpl = "1";
                            }
                            int ppl = Integer.valueOf(numPpl);
                            perPersonTotal.setText("" + Math.round((t + f) / ppl * 100) * 1.0 / 100);
                            tipPercentage = 0;
                        }
                    }

                    // If the user has entered an optional 'number of people'
                    // value
                    // for splitting the bill, the necessary logic is also ran
                    else {
                        double f = ((tipPercentage * t) / 100);
                        finalTip.setText("" + f);
                        finalTotal.setText("" + (f + t));

                        String numPpl = numOfPpl.getText().toString();

                        if (numPpl.trim().equals("")) {
                            numOfPpl.setText("1");
                            numPpl = "1";
                        }

                        int ppl = Integer.valueOf(numPpl);
                        perPersonTotal.setText("" + Math.round((t + f) / ppl * 100) * 1.0 / 100);
                    }
                }
            }
        });
    }

    /**
     * Whenever a new Radio Button option is selected, we will update a class
     * variable with the tip percentage.
     *
     * @param view
     *            View from where it originated
     */
    public void onRadioButtonClicked(View view) {
        // This is needed here in order to retrieve the checked radio button
        // every time
        // the user clicks on a new radio button.
        radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());

        // Retrieves the text of the radio button selected
        String ident = radioButton.getText().toString();

        // If the user wants to manually enter the tip amount
        // we will unlock the EditText widget and let them enter a value
        if (ident.equals("Let me decide!")) {
            tipPercentage = 0;
            unlockPercentField();
        }

        // Or if the user chooses a hard coded tip percentage
        // we will set the appropriate percentage and lock the editText widget
        // for manual tip percentage entry
        else if (ident.equals("10%")) {
            tipPercentage = 10;
            lockPercentField();
        }

        else if (ident.equals("15%")) {
            tipPercentage = 15;
            lockPercentField();
        }

        else if (ident.equals("20%")) {
            tipPercentage = 20;
            lockPercentField();
        }
    }

    /**
     * Locks the EditText widget for manual tip percentage entry If the user
     * decides to go with a hard coded value.
     */
    private void lockPercentField() {
        tipEdit.clearComposingText();
        tipEdit.setEnabled(false);
    }

    /**
     * If the user wants to enter their own manual tip percentage this will make
     * sure the EditText is not locked.
     */
    private void unlockPercentField() {
        tipEdit.setEnabled(true);
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