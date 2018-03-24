package com.madventures.sawmalie.madventures;



        import java.util.ArrayList;
        import java.util.Currency;
        import java.util.List;
        import java.util.Set;
        import Util.MadventureMenuActivity;
        import android.annotation.SuppressLint;
        import android.app.AlertDialog;
        import android.content.Context;
        import android.content.SharedPreferences;
        import android.os.Bundle;
        import android.preference.PreferenceManager;
        import android.util.Log;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Spinner;

/**
 * Allows user to change settings for their application.
 */
@SuppressLint({ "NewApi" })
public class Settings extends MadventureMenuActivity {

    private Context context;
    private EditText usernm, passwd;
    private Spinner spinnerFrom;
    private Button btnSubmit;
    private Set<Currency> currencies = Currency.getAvailableCurrencies();
    private List<String> currenciesName = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        context = this;

        usernm = (EditText) findViewById(R.id.name_edit);

        passwd = (EditText) findViewById(R.id.passwd_edit);

        spinnerFrom = (Spinner) findViewById(R.id.curr_spinner);

        for (Currency c : currencies) {
            currenciesName.add(c.getDisplayName());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                currenciesName);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrom.setAdapter(dataAdapter);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int cur = prefs.getInt("CURR", -1);
        String us = prefs.getString("USERNAME", null);
        String pass = prefs.getString("PASSWORD", null);

        // If this is not the first time they enter settings, it means
        /// they already entered their data, and it will be loaded.
        if (us != null) {
            usernm.setText(us);
        }

        if (passwd != null) {
            passwd.setText(pass);
        }

        if (cur != -1) {
            spinnerFrom.setSelection(cur);
        }

        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Once it validates that the fields are not empty, we will save
                // them to shared preferences
                if (!usernm.getText().toString().trim().isEmpty() && !passwd.getText().toString().trim().isEmpty()) {
                    int from = currenciesName.indexOf(spinnerFrom.getSelectedItem());
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("CURR", from);
                    editor.putString("USERNAME", usernm.getText().toString());
                    editor.putString("PASSWORD", passwd.getText().toString());
                    editor.commit();
                    Log.i("CUR", from + "");
                    finish();
                }

                else {
                    invalidInput(v);
                }
            }

        });
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
