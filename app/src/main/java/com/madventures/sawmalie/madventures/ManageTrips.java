package com.madventures.sawmalie.madventures;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import Util.MadventureMenuActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

/**
 * Display a list of trips.
 */

public class ManageTrips extends MadventureMenuActivity {
    private SimpleCursorAdapter sca;
    private ListView lv;
    private DBHelper dbh;
    private Button bu;
    private TextView errorLabel;
    private Cursor cursor;

    public String apiurl = "http://madventures-ndrbaydoun.rhcloud.com/webapi/user_trips";
    public String name;
    public String passwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_trips);

        errorLabel = findViewById(R.id.error_label);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String us = prefs.getString("USERNAME", null);
        String passwd = prefs.getString("PASSWORD", null);

        if (us == null || passwd == null) {
            Intent s = new Intent(this, Settings.class);
            startActivity(s);
        }

        lv = findViewById(R.id.mtListView);
        bu = findViewById(R.id.bu);

        bu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                synch();
            }
        });

        DBHelper dbh = DBHelper.getDBHelper(this);
        cursor = dbh.findAllTrips();
        Log.i("CURSOR", "" + cursor);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String[] from = { DBHelper.TRIPS_NAME, DBHelper.TRIPS_DESCRIPTION };
        int[] to = { R.id.tripName, R.id.tripDesc };
        dbh = DBHelper.getDBHelper(this);
        Cursor c = dbh.findAllTrips();
        sca = new SimpleCursorAdapter(this, R.layout.trip_view, c, from, to, 0);
        lv.setAdapter(sca);

        lv.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = sca.getCursor();
                c.moveToPosition(position);
                int idOfTrip = c.getInt(c.getColumnIndex(DBHelper.TRIPS_PK_ID));
                Log.i("SELECTION", "Trip id: " + idOfTrip);
                launchItineraryActivity(idOfTrip);
            }

        });
        Log.i("CURSOR", "" + c);
    }

    /**
     * Launch activity to display itinerary for a specific trip
     *
     * @param tripId
     *            id of trip to view itineraries
     */
    public void launchItineraryActivity(int tripId) {
        Intent i = new Intent(this, ItineraryForTripActivity.class);
        i.putExtra("TRIP_ID", tripId);
        startActivity(i);
    }

    /**
     * Calls the AynchTask class to download data from a RESTful API and
     * save the info locally.
     */
    public void synch()
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        name = prefs.getString("USERNAME", null);
        passwd = prefs.getString("PASSWORD", null);

        if (name == null || passwd == null) {
            Intent s = new Intent(this, Settings.class);
            startActivity(s);
        }

        else {
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                new DownloadData().execute(apiurl);
            }

            else
            {
                errorLabel.setText(getString(R.string.network_conn_error));
            }
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
    private class DownloadData extends AsyncTask<String, Void, String> {

        /**
         * Method that displays the final result.
         *
         * @param result
         *            String containing the final result
         */
        @Override
        protected void onPostExecute(String result) {
            errorLabel.setText(result);
            refreshView();
        }

        /**
         * Method that performs actions in the background thread.
         *
         * @return String that is returned from the downloadUrl method.
         */
        @Override
        protected String doInBackground(String... params) {

            String jsonData = "email=" + name + "&password=" + passwd;

            try {
                return downloadData(params[0], jsonData);
            }

            catch (IOException e) {
                return getString(R.string.weather_url_error) + e.getMessage();
            }
        }

        /**
         * Given a URL, establishes an HttpUrlConnection and retrieves the web page
         * content as a InputStream, which it returns as a string.
         *
         * @return String result returned from the readIt method
         */
        private String downloadData(String... params) throws IOException {
            InputStream is = null;
            OutputStream out;
            String contentAsString = "";
            int response;
            URL url;
            HttpURLConnection conn = null;

            byte[] bytes = params[1].getBytes("UTF-8");

            try {
                url = new URL(params[0]);
            }

            catch (MalformedURLException e) {
                return getString(R.string.weather_url_error) + e.getMessage();
            }

            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);

                Log.i("G", conn.toString());

                out = new BufferedOutputStream(conn.getOutputStream());

                out.write(bytes);
                out.flush();
                out.close();

                response = conn.getResponseCode();

                if (response != HttpURLConnection.HTTP_OK)
                {
                    if(response == 401)
                    {
                        return getString(R.string.invalid_auth);
                    }
                    return getString(R.string.server_return) + response + getString(R.string.aborting_read);
                }

                is = conn.getInputStream();
                contentAsString = readIt(is);
                return contentAsString;

            }

            finally {
                if (is != null)
                    try {
                        is.close();
                    } catch (IOException ignore) {
                    }

                if (conn != null)
                    try {
                        conn.disconnect();
                    } catch (IllegalStateException ignore) {
                    }

            }
        }

        /**
         * Reads stream from HTTP connection and converts it to a String.
         *
         * @param stream
         *            InputStream for reading out the response from the URL that was
         *            down-loaded
         * @return String result from processJSONResponse method
         */
        public String readIt(InputStream stream) throws IOException {

            String buffer = "";
            BufferedReader reader = null;

            reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));

            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer += line + "\n";
            }

            try {
                return JSONParse(buffer);
            } catch (JSONException e) {
                e.printStackTrace();
                return getString(R.string.json_error);
            }
        }

        /**
         * processes the JSON data and retrieves the desired info,
         * also handles concurrency with the data retrieved and synch
         *  the local DB with what was retrieved from the web
         *
         * @param read The contents of the down-loaded URL as a string
         * @return String that is an error or succes message
         * @throws JSONException If encounters errors with JSON processing
         */
        private String JSONParse(String read) throws JSONException {
            Cursor trip;
            int tripId;

            JSONObject jobj = new JSONObject(read);

            JSONArray jsonTrips = new JSONArray();
            JSONArray jsonLocations = new JSONArray();

            HashMap<String, String> locations = new HashMap<>();

            if (jobj.has("locations")) {
                jsonLocations = jobj.getJSONArray("locations");
                Log.i("API", "has locations");
                int ident;
                String nm;
                String desc;
                String city;
                String cc;

                for (int i = 0; i < jsonLocations.length(); i++) {
                    JSONObject row = jsonLocations.getJSONObject(i);
                    ident = row.getInt("id");
                    nm = row.getString("name");

                    if (dbh.getLocationByName(nm).moveToLast() == false) {
                        desc = row.getString("description");
                        city = row.getString("city");
                        cc = row.getString("country_code");
                        String localLocationId = dbh.insertNewLocation(nm, desc, city, cc) + "";
                        locations.put(ident + "", localLocationId);
                    } else {
                        Cursor c = dbh.getLocationByName(nm);
                        c.moveToFirst();

                        locations.put(ident + "", c.getString(0));
                    }
                }
            }

            if (jobj.has("trips")) {
                Log.i("API", "has trips");
                jsonTrips = jobj.getJSONArray("trips");

                int id;
                String name;
                String description;
                String creation;
                String update;
                String close;

                String category;
                String budgetedDesc;
                String supplierName;
                String address;
                int budgetedAmount;
                String dateArrived;
                String dateDeparture;
                int locationId;

                int amt;

                JSONArray budgeted = new JSONArray();

                for (int i = 0; i < jsonTrips.length(); i++) {
                    JSONObject row = jsonTrips.getJSONObject(i);
                    id = row.getInt("id");
                    name = row.getString("name");
                    description = row.getString("description");
                    creation = row.getString("creation_date_stamp");
                    if (creation.equals("-0001-11-30 00:00:00"))
                        creation = "2015/11/11";
                    else
                        creation = (creation.replace('-', '/')).substring(0, creation.indexOf(" "));
                    update = row.getString("update_date_stamp");
                    if (update.equals("-0001-11-30 00:00:00"))
                        update = "2015/11/11";
                    else
                        update = (update.replace('-', '/')).substring(0, update.indexOf(" "));
                    close = row.getString("close_date_stamp");
                    if (close.equals("-0001-11-30 00:00:00"))
                        close = "2015/11/11";
                    else
                        close = (close.replace('-', '/')).substring(0, close.indexOf(" "));
                    trip = dbh.getTripByWebId(id);

                    if (trip.moveToLast() == false) {
                        // create
                        tripId = (int) dbh.insertNewTrip(id, creation, update, close, name, description);
                    } else {
                        // update
                        trip.moveToFirst();
                        tripId = Integer.parseInt(trip.getString(0));
                        dbh.updateTrip(tripId, id, creation, update, close, name, description);
                        dbh.deleteTripItineraries(tripId);
                    }

                    if (row.has("budgeted_expenses")) {
                        Log.i("API", "has budgeted");
                        budgeted = row.getJSONArray("budgeted_expenses");

                        for (int j = 0; j < budgeted.length(); j++) {
                            JSONObject be = budgeted.getJSONObject(j);
                            locationId = be.getInt("location_id");
                            category = be.getString("category");
                            budgetedDesc = be.getString("description");
                            supplierName = be.getString("name_of_supplier");
                            address = be.getString("address");
                            budgetedAmount = be.getInt("amount");
                            dateArrived = be.getString("date_arr_planned");
                            if (dateArrived.equals("-0001-11-30 00:00:00"))
                                dateArrived = "2015/11/11";
                            else
                                dateArrived = (dateArrived.replace('-', '/')).substring(0, dateArrived.indexOf(" "));
                            dateDeparture = be.getString("date_dep_planned");
                            if (dateDeparture.equals("-0001-11-30 00:00:00"))
                                dateDeparture = "2015/11/11";
                            else
                                dateDeparture = (dateDeparture.replace('-', '/')).substring(0,
                                        dateDeparture.indexOf(" "));

                            long budgetID = dbh.insertNewBudgeted(tripId + "", locations.get(locationId + ""), dateArrived,
                                    dateDeparture, budgetedAmount + "", budgetedDesc, category, supplierName, address);

                            if(be.has("actual_expense"))
                            {
                                try{
                                    JSONObject ac = be.getJSONObject("actual_expense");
                                    amt = ac.getInt("amount");
                                    dbh.insertNewActual(dateArrived, dateDeparture, amt + "", budgetedDesc, category, supplierName, address, budgetID);
                                }catch(JSONException jse){
                                    //throws exception if JSON object is null but null represents no expense in our api
                                    Log.i("json", "no actual expense");
                                }
                            }
                        }
                    }

                }
            }

            return getString(R.string.succes_synch);
        }
    }

    /**
     * Refreshes the ListView by re-queried the DB.
     *
     */
    private void refreshView()
    {
        // renew the cursor
        Cursor cursor = dbh.findAllTrips();
        // have the adapter use the new cursor, changeCursor closes old cursor
        // too
        sca.changeCursor(cursor);
        // have the adapter tell the observers
        sca.notifyDataSetChanged();
    }
}