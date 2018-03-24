package com.madventures.sawmalie.madventures;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import Util.MadventureMenuActivity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity that does some GEO-location and retrieves the weather information
 * associated with the latitude and longitude found.
 */
public class Weather extends MadventureMenuActivity {
    double longitude;
    double latitude;

    private final String API_KEY = "&appid=2de143494c0b295cca9337e1e96b00e0&units=metric";
    private final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?";

    private TextView errorField;
    private static final int MAXBYTES = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather);

        errorField = (TextView) findViewById(R.id.error_field);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Location location = null;
        try{
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        catch(SecurityException e){
            Toast.makeText(this, "Please enable location access :(",
                    Toast.LENGTH_LONG).show();
        }

        if (location != null) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            Log.i("URL", BASE_URL + "lat=" + latitude + "&lon=" + longitude + API_KEY);
            new DownloadWebpageText().execute(BASE_URL + "lat=" + latitude + "&lon=" + longitude + API_KEY);
        }

        else {
            errorField.setText(getString(R.string.weather_error));
        }
    }

    /**
     * Uses AsyncTask to create a task away from the main UI thread. This task
     * takes a URL string and uses it to create an HttpUrlConnection. Once the
     * connection has been established, the AsyncTask downloads the contents of
     * the webpage via an an InputStream. The InputStream is converted into a
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
            String[] results = result.split(",");

            if (results.length == 4) {
                TextView locationField = (TextView) findViewById(R.id.location_res);
                TextView tempField = (TextView) findViewById(R.id.weather_res);
                TextView descField = (TextView) findViewById(R.id.description_res);
                TextView humidityField = (TextView) findViewById(R.id.humidity_res);
                locationField.setText(results[2]);
                tempField.setText(results[0] + getString(R.string.celcius));
                descField.setText(results[3]);
                humidityField.setText(results[1] + "%");
            }

            else {
                errorField.setText(getString(R.string.weather_error));
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
                Log.i("URLS", urls[0]);
                return downloadUrl(urls[0]);
            }

            catch (IOException e) {
                return getString(R.string.weather_url_error);
            }
        }
    }

    /**
     * Given a URL, establishes an HttpUrlConnection and retrieves the web page
     * content as a InputStream, which it returns as a string.
     *
     * @param myurl URL to down-load
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
                return getString(R.string.server_return) + response + getString(R.string.aborting_read);

            is = conn.getInputStream();
            String contentAsString = readIt(is, len);
            return contentAsString;
        }

        catch (IOException e) {

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
        return null;
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
        String results = "";

        try {
            results = processJSONResponse(json);
        }

        catch (IllegalStateException | NoSuchAlgorithmException | JSONException e) {
            e.printStackTrace();
        }

        return results;
    }

    /**
     * processJSONResponse() Parameter is a JSON stream. Parsed using JSONArray
     * [] JSONObject {} Populate a struct array with the data.
     *
     * @param resp The contents of the down-loaded URL as a string
     * @return The desired data that we filtered out of the JSON response
     */
    public String processJSONResponse(String resp)
            throws IllegalStateException, IOException, JSONException, NoSuchAlgorithmException {
        JSONObject jobj = new JSONObject(resp);
        JSONObject JSONholder;
        JSONArray JSONarray;
        String results = "";

        Log.i("pJSONr", jobj.toString());

        if (jobj.has("main")) {
            JSONholder = new JSONObject(jobj.getString("main"));

            if (JSONholder.has("temp")) {
                results += JSONholder.getString("temp") + ",";
            }
            if (JSONholder.has("humidity")) {
                results += JSONholder.getString("humidity") + ",";
            }
        }

        if (jobj.has("name")) {
            results += jobj.getString("name") + ",";
        }

        if (jobj.has("weather")) {
            JSONarray = new JSONArray(jobj.getString("weather"));
            int length = JSONarray.length();
            for (int i = 0; i < length; i++) {
                // unnamed objects in array: [ {}, {}, {} ... ]
                JSONholder = JSONarray.getJSONObject(i);
                // named items in array
                if (JSONholder.has("description")) {
                    results += JSONholder.getString("description");
                }
            }
        }
        Log.i("ttt", results);
        return results;
    }
}

