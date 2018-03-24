package com.madventures.sawmalie.madventures;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;
import Util.MadventureMenuActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

/**
 * CurrencyConverter activity that gives the user the choice to choose the FROM
 * and TO currencies and will find the conversion rate of those two currencies
 * and multiply that rate by whatever number the user also supplies.
 */
@SuppressLint("NewApi")
public class CurrencyConverter extends MadventureMenuActivity {
    // The two spinners: convert FROM a currency TO another currency
    private Spinner spinnerFrom, spinnerTo;
    private Button btnSubmit;
    private TextView textView;
    private EditText amount;
    private int cur = -1;

    // A data-set of unique Currency objects of almost every currency in
    // real-world use
    // (not all these currencies are supported for conversion)
    private Set<Currency> currencies = Currency.getAvailableCurrencies();

    // An ArrayList that contains all the display names of every currency
    private List<String> currenciesName = new ArrayList<String>();

    // An ArrayList that contains all the ISO codes of every currency
    private List<String> currencyISO = new ArrayList<String>();

    // Target currency (the one to convert; it's ISO code is stored here)
    private String TO;

    private static final int MAXBYTES = 500;

    // To store the conversion rate once found
    double CONV_RATE = -1.0;

    // The unfinished URL of the API that will be called to retrieve the
    // exchange rate
    private final String BASE_URL = "http://api.fixer.io/latest?base=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currency_converter);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        cur = prefs.getInt("CURR", -1);

        if (cur == -1) {
            Intent s = new Intent(this, Settings.class);
            startActivity(s);
        }

        textView = (TextView) findViewById(R.id.tv);

        if (savedInstanceState != null)
        {
            CharSequence savedText = savedInstanceState.getCharSequence("VAL");
            textView.setText(savedText);
        }

        amount = (EditText) findViewById(R.id.amount);

        // Traverse the Set of Currencies and retrieve their display names and
        // ISO code
        // and save them to two separate ArrayLists
        for (Currency c : currencies) {
            currenciesName.add(c.getDisplayName());
            currencyISO.add(c.getCurrencyCode());
        }

        addItemsOnSpinners();
        addListenerOnButton();
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence("VAL", textView.getText());
    }

    /**
     * Add items into the two spinners dynamically using an ArrayAdapter.
     */
    public void addItemsOnSpinners() {
        spinnerFrom = (Spinner) findViewById(R.id.spinnerFrom);
        spinnerTo = (Spinner) findViewById(R.id.spinnerTo);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                currenciesName);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrom.setAdapter(dataAdapter);
        spinnerTo.setAdapter(dataAdapter);

        if (cur != -1) {
            spinnerFrom.setSelection(cur);
        }
    }

    /**
     * Get the selected drop-down list value. Set an OnClickListener and
     * retrieve the index of the spinner item selected. The index will be used
     * to retrieve the ISO code of that currency from an ArrayList of ISO codes.
     */
    public void addListenerOnButton() {
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                int from = currenciesName.indexOf(spinnerFrom.getSelectedItem());
                int to = currenciesName.indexOf(spinnerTo.getSelectedItem());

                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    Log.i("URL", BASE_URL + currencyISO.get(from) + ";symbols=" + currencyISO.get(to));

                    TO = currencyISO.get(to);

                    new DownloadWebpageText().execute(BASE_URL + currencyISO.get(from) + ";symbols=" + TO);
                }

                else {
                    Toast.makeText(CurrencyConverter.this, getString(R.string.network_conn_error), Toast.LENGTH_SHORT)
                            .show();
                }

            }

        });
    }

    /**
     * Uses AsyncTask to create a task away from the main UI thread. This task
     * takes a URL string and uses it to create an HttpUrlConnection. Once the
     * connection has been established, the AsyncTask downloads the contents of
     * the web-page via an an InputStream. The InputStream is converted into a
     * string, which is displayed in the UI by the AsyncTask's onPostExecute
     * method.
     */
    private class DownloadWebpageText extends AsyncTask<String, Void, String> {
        /**
         * Method that displays the final result.
         *
         * @param result
         *            String containing the final result
         */
        protected void onPostExecute(String result) {
            if (result.contains("422")) {
                textView.setText(getString(R.string.curr_support));
            }

            else {
                CONV_RATE = Double.valueOf(result);
                results();
            }
        }

        /**
         * Method that performs actions in the background thread.
         *
         * @param urls
         *            Variable length parameter of the URLs to be down-loaded
         * @return String that is returned from the downloadUrl method.
         */
        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadUrl(urls[0]);
            }

            catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
    }

    /**
     * Given a URL, establishes an HttpUrlConnection and retrieves the web page
     * content as a InputStream, which it returns as a string.
     *
     * @param myurl
     *            URL to down-load
     * @return String result returned from the readIt method
     */
    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        int len = MAXBYTES;
        HttpURLConnection conn = null;
        URL url = new URL(myurl);
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            int response = conn.getResponseCode();

            if (response != HttpURLConnection.HTTP_OK)
                return "Server returned: " + response + " aborting read.";

            is = conn.getInputStream();

            String contentAsString = readIt(is, len);
            return contentAsString;
        }

        catch (IOException e) {
            Log.e("EXCEPTION", "exception" + Log.getStackTraceString(e));
            throw e;
        }

        finally {
            if (is != null) {
                try {
                    is.close();
                }

                catch (IOException ignore) {
                }

                if (conn != null) {
                    try {
                        conn.disconnect();
                    }

                    catch (IllegalStateException ignore) {
                    }
                }
            }
        }
    }

    /**
     * Reads stream from HTTP connection and converts it to a String.
     *
     * @param stream
     *            InputStream for reading out the response from the URL that was
     *            down-loaded
     * @param len
     *            the maximum byte length to read from the stream
     * @return String result from processJSONResponse method
     */
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"), len);
        StringBuilder sb = new StringBuilder();
        String line = null;

        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }

        stream.close();
        String json = sb.toString();
        String conversionRate = "";

        try {
            conversionRate = processJSONResponse(json);
        }

        catch (IllegalStateException | NoSuchAlgorithmException | JSONException e) {
            textView.setText(getString(R.string.unexpected_parsing_error));
            e.printStackTrace();
        }

        catch (NumberFormatException e) {
            textView.setText(getString(R.string.curr_support));
            e.printStackTrace();
        }

        return conversionRate;
    }

    /**
     * processJSONResponse() Parameter is a JSON stream. Parsed using JSONArray
     * [] JSONObject {} Populate a struct array with the data.
     *
     * @param resp
     *            The contents of the down-loaded URL as a string
     * @return The desired data that we filtered out of the JSON response
     */
    public String processJSONResponse(String resp)
            throws IllegalStateException, IOException, JSONException, NoSuchAlgorithmException {
        JSONObject jobjHolder = new JSONObject(resp);
        JSONObject jobj = new JSONObject(resp);
        String result = null;

        if (jobjHolder.has("rates")) {
            jobj = new JSONObject(jobjHolder.getString("rates"));

            if (jobj.has(TO)) {
                result = jobj.getString(TO);
                Log.i("TEST", result);
            }

        }

        return result;
    }

    /**
     * Checks to see if the EditText is empty, if not will convert the inputed
     * number amount by multiplying by the exchange rate and displaying it.
     */
    public void results() {
        String holderAmt = amount.getText().toString();

        if (holderAmt.trim().equals("")) {
            Toast.makeText(CurrencyConverter.this, getString(R.string.field_empty), Toast.LENGTH_SHORT).show();
        }

        else {
            double amt = Double.valueOf(holderAmt);
            textView.setText("" + amt * CONV_RATE);
        }
    }

}
