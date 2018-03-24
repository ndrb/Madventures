package com.madventures.sawmalie.madventures;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * DBHelper class that inherits from SQLiteOpenHelper.
 */

public class DBHelper extends SQLiteOpenHelper {

    // Database table name
    public static final String TABLE_NAME_TRIPS = "trips";
    public static final String TABLE_NAME_LOCATIONS = "locations";
    public static final String TABLE_NAME_ACTUAL_EXP = "actual_expense";
    public static final String TABLE_NAME_BUDGETED_EXP = "budgeted_expense";

    // Trips table column names
    public static final String TRIPS_PK_ID = "_id";
    public static final String TRIPS_TRIP_ID = "trip_id";
    public static final String TRIPS_CREATION_DATE = "touring";
    public static final String TRIPS_UPDATE_DATE = "accommodation";
    public static final String TRIPS_CLOSE_DATE = "date";
    public static final String TRIPS_NAME = "name";
    public static final String TRIPS_DESCRIPTION = "description";

    // locations table column names
    public static final String LOCATIONS_PK_ID = "_id";
    public static final String LOCATIONS_NAME = "name";
    public static final String LOCATIONS_DESCRIPTION = "description";
    public static final String LOCATIONS_CITY = "city";
    public static final String LOCATIONS_COUNTRY_CODE = "country_code";

    // budgeted expense table column names
    public static final String BUDGETED_EXP_PK_ID = "_id";
    public static final String BUDGETED_EXP_LOCATION_ID = "location_id";
    public static final String BUDGETED_EXP_TRIP_ID = "trip_id";
    public static final String BUDGETED_EXP_DATE_ARRIVE = "date_arrive_planned";
    public static final String BUDGETED_EXP_DATE_DEPART = "date_depart_planned";
    public static final String BUDGETED_EXP_AMOUNT = "amount";
    public static final String BUDGETED_EXP_DESCRIPTION = "description";
    public static final String BUDGETED_EXP_CATEGORY = "cattegory";
    public static final String BUDGETED_EXP_NAME_OF_SUPPLIER = "name_of_supplier";
    public static final String BUDGETED_EXP_ADDRESS = "address";

    // actual expense table column names
    public static final String ACTUAL_EXP_PK_ID = "_id";
    public static final String ACTUAL_EXP_BUDGETED_ID = "budgeted_id";
    public static final String ACTUAL_EXP_DATE_ARRIVE = "date_arrive";
    public static final String ACTUAL_EXP_DATE_DEPART = "date_depart";
    public static final String ACTUAL_EXP_AMOUNT = "ammount";
    public static final String ACTUAL_EXP_DESCRIPTION = "description";
    public static final String ACTUAL_EXP_CATEGORY = "category";
    public static final String ACTUAL_EXP_NAME_OF_SUPP = "name_of_supplier";
    public static final String ACTUAL_EXP_ADDRESS = "address";

    // database name
    public static final String DATABASE_NAME = "Madventure.db";

    // Version number
    private static final int DATABASE_VERSION = 1;

    // Create trips table statement
    public static final String TRIPS_TABLE_CREATE = "create table " + TABLE_NAME_TRIPS + "( " + TRIPS_PK_ID
            + " integer primary key autoincrement, " + TRIPS_TRIP_ID + " text not null, " + TRIPS_CREATION_DATE
            + " text not null, " + TRIPS_UPDATE_DATE + " text, " + TRIPS_CLOSE_DATE + " text, " + TRIPS_NAME
            + " text not null, " + TRIPS_DESCRIPTION + " text );";

    // Create locations table statement
    public static final String LOCATIONS_TABLE_CREATE = "create table " + TABLE_NAME_LOCATIONS + "(" + LOCATIONS_PK_ID
            + " integer primary key autoincrement, " + LOCATIONS_NAME + " text not null, " + LOCATIONS_DESCRIPTION
            + " text, " + LOCATIONS_CITY + " text, " + LOCATIONS_COUNTRY_CODE + " text);";

    // Create budgeted expense table statement
    public static final String BUDGETED_EXP_TABLE_CREATE = "create table " + TABLE_NAME_BUDGETED_EXP + "( "
            + BUDGETED_EXP_PK_ID + " integer primary key autoincrement, " + BUDGETED_EXP_LOCATION_ID
            + " integer not null, " + BUDGETED_EXP_TRIP_ID + " integer not null, " + BUDGETED_EXP_DATE_ARRIVE
            + " text not null, " + BUDGETED_EXP_DATE_DEPART + " text not null, " + BUDGETED_EXP_AMOUNT
            + " real not null default 0.0, " + BUDGETED_EXP_DESCRIPTION + " text not null default '', "
            + BUDGETED_EXP_CATEGORY + " text not null default '', " + BUDGETED_EXP_NAME_OF_SUPPLIER + " text, "
            + BUDGETED_EXP_ADDRESS + " text, " + "FOREIGN KEY(" + BUDGETED_EXP_LOCATION_ID + ") REFERENCES "
            + TABLE_NAME_LOCATIONS + "(" + LOCATIONS_PK_ID + "));" + "FOREIGN KEY(" + BUDGETED_EXP_TRIP_ID
            + ") REFERENCES " + TABLE_NAME_TRIPS + "(" + TRIPS_PK_ID + "));";

    // Create actual expense table statement
    public static final String ACTUAL_EXP_TABLE_CREATE = "create table " + TABLE_NAME_ACTUAL_EXP + "( "
            + ACTUAL_EXP_PK_ID + " integer primary key autoincrement, " + ACTUAL_EXP_BUDGETED_ID + " integer not null, "
            + ACTUAL_EXP_DATE_ARRIVE + " text not null, " + ACTUAL_EXP_DATE_DEPART + " text not null, "
            + ACTUAL_EXP_AMOUNT + " real not null default 0.0, " + ACTUAL_EXP_DESCRIPTION + " text default '', "
            + ACTUAL_EXP_CATEGORY + " text not null default '', " + ACTUAL_EXP_NAME_OF_SUPP
            + " text not null default '', " + ACTUAL_EXP_ADDRESS + " text not null default '', " + "FOREIGN KEY("
            + ACTUAL_EXP_BUDGETED_ID + ") REFERENCES " + TABLE_NAME_BUDGETED_EXP + "(" + BUDGETED_EXP_PK_ID + "));";

    // Static DBHelper to be used in other classes and prevent repeated
    // instantiation
    private static DBHelper dbh = null;

    /**
     * DBHelper constructor. Private so that DBHelper isn't always being
     * instantiated.
     *
     * @param context
     *            Context from where it is being instantiated
     */
    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Returns an instance of DBHelper.
     *
     * @param context
     *            Context from where it is being instantiated
     * @return DBHelper object
     */
    public static DBHelper getDBHelper(Context context) {
		/*
		 * Use the application context, which will ensure that you don't
		 * accidentally leak an Activity's context. See this article for more
		 * information: http://bit.ly/6LRzfx
		 */
        if (dbh == null) {
            dbh = new DBHelper(context.getApplicationContext());
        }

        return dbh;
    } // getDBHelper()

    /**
     * Called when we first create the database.
     *
     * @param SQLiteDatabase
     *            database to be created
     */
    @Override
    public void onCreate(SQLiteDatabase database) {
        createPopulateDB(database);
    }

    /**
     * Method that upgrades the database if needed
     *
     * @param SQLiteDatabase
     *            database to be upgraded
     * @param oldVersion
     *            the old version of the database
     * @param newVersion
     *            the new version of the database to be upgraded to
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion
                + ", which will destroy all old data");

        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_TRIPS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_LOCATIONS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_BUDGETED_EXP);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ACTUAL_EXP);
        }

        catch (SQLException e) {
            Log.e(DBHelper.class.getName(), "DROP exception" + Log.getStackTraceString(e));
            throw e;
        }

        createPopulateDB(db);
    }

    /**
     * Method called when the database needs to be opened.
     *
     * @param database
     *            SQLiteDatabase database to be opened
     */
    @Override
    public void onOpen(SQLiteDatabase database) {
        Log.i(DBHelper.class.getName(), "onOpen()");
    }

    /**
     * Method that is used to create and populate the database
     *
     * @param database
     *            SQLiteDatabase database to be populated
     */
    @SuppressLint("SimpleDateFormat")
    public void createPopulateDB(SQLiteDatabase database) {
        long previousPK;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String dateStr = dateFormat.format(new Date());
        // create trip
        ContentValues sampleTrip = new ContentValues();
        sampleTrip.put(TRIPS_NAME, "Sample Trip");
        sampleTrip.put(TRIPS_DESCRIPTION, "this is a sample trip used for testing purposes");
        sampleTrip.put(TRIPS_CREATION_DATE, "2015/11/28");
        sampleTrip.put(TRIPS_UPDATE_DATE, "2015/11/28");
        sampleTrip.put(TRIPS_CLOSE_DATE, "2015/11/28");
        sampleTrip.put(TRIPS_TRIP_ID, 0);

        // create location
        ContentValues sampleLocation = new ContentValues();
        sampleLocation.put(LOCATIONS_NAME, "bla");
        sampleLocation.put(LOCATIONS_CITY, "Sample city");
        sampleLocation.put(LOCATIONS_DESCRIPTION, "A sample descritption");

        // create actual expense
        ContentValues sampleAExp = new ContentValues();
        sampleAExp.put(ACTUAL_EXP_ADDRESS, "bla");
        sampleAExp.put(ACTUAL_EXP_NAME_OF_SUPP, "bla");
        sampleAExp.put(ACTUAL_EXP_DATE_ARRIVE, dateStr);
        sampleAExp.put(ACTUAL_EXP_DATE_DEPART, dateStr);

        // create itinerary
        ContentValues sampleItinerary = new ContentValues();
        sampleItinerary.put(BUDGETED_EXP_ADDRESS, "bla");
        sampleItinerary.put(BUDGETED_EXP_NAME_OF_SUPPLIER, "bla");
        sampleItinerary.put(BUDGETED_EXP_DATE_ARRIVE, dateStr);
        sampleItinerary.put(BUDGETED_EXP_DATE_DEPART, dateStr);

        try {
            database.execSQL(TRIPS_TABLE_CREATE);
            database.execSQL(LOCATIONS_TABLE_CREATE);
            database.execSQL(BUDGETED_EXP_TABLE_CREATE);
            database.execSQL(ACTUAL_EXP_TABLE_CREATE);

            // populating DB with sample data
            previousPK = database.insert(TABLE_NAME_TRIPS, null, sampleTrip);// trips
            sampleItinerary.put(BUDGETED_EXP_TRIP_ID, previousPK);
            previousPK = database.insert(TABLE_NAME_LOCATIONS, null, sampleLocation);// location
            sampleItinerary.put(BUDGETED_EXP_LOCATION_ID, previousPK);
            previousPK = database.insert(TABLE_NAME_BUDGETED_EXP, null, sampleItinerary);
            sampleAExp.put(ACTUAL_EXP_BUDGETED_ID, previousPK);
            database.insert(TABLE_NAME_ACTUAL_EXP, null, sampleAExp);
        }

        catch (SQLException e) {
            Log.e(DBHelper.class.getName(), "CREATE exception" + Log.getStackTraceString(e));
            throw e;
        }

    }

    /**
     * Method to update a trip with new data.
     *
     * @param tripId
     *            Id of trip to update
     * @param creationDate
     *            Date trip created
     * @param updateDate
     *            Date trip updated
     * @param closeDate
     *            Date trip closed
     * @param name
     *            Name of trip
     * @param description
     *            Description of trip
     * @return Id of the trip updated
     */
    public long updateTrip(int tripId, int webId, String creationDate, String updateDate, String closeDate, String name,
                           String description) {
        ContentValues cv = new ContentValues();
        cv.put(TRIPS_TRIP_ID, webId);
        cv.put(TRIPS_CREATION_DATE, creationDate);
        cv.put(TRIPS_UPDATE_DATE, updateDate);
        cv.put(TRIPS_CLOSE_DATE, closeDate);
        cv.put(TRIPS_NAME, name);
        cv.put(TRIPS_DESCRIPTION, description);

        return getWritableDatabase().update(TABLE_NAME_TRIPS, cv, TRIPS_TRIP_ID + " = ?",
                new String[] { Integer.toString(tripId) });
    }

    /**
     * Creates a new Itinerary and a new location.
     *
     * @param tripId
     *            Id of trip associated
     * @param areaName
     *            Location: name of area
     * @param locationDesc
     *            Location: Description
     * @param city
     *            Location: City
     * @param cc
     *            Location: Country code
     * @param arrived
     *            Date arrived
     * @param depart
     *            Date Departed
     * @param amt
     *            Budgeted Amount
     * @param itiDesc
     *            Itinerary description
     * @param categ
     *            Category
     * @param supplierName
     *            Supplier Name
     * @param address
     *            Address
     * @return
     */
    public long insertNewItinerary(int tripId, String areaName, String locationDesc, String city, String cc,
                                   String arrived, String depart, String amt, String itiDesc, String categ, String supplierName,
                                   String address)
    {
        long locId = insertNewLocation(areaName, locationDesc, city, cc);

        return insertNewBudgeted(tripId+"", locId+"", arrived, depart, amt, itiDesc, categ, supplierName, address);
    }

    /**
     * Creates a new Itinerary and link it to a existing location.
     *
     * @param tripId
     *            Id of trip associated
     * @param locId Location Id
     * @param arrived
     *            Date arrived
     * @param depart
     *            Date Departed
     * @param amt
     *            Budgeted Amount
     * @param itiDesc
     *            Itinerary description
     * @param categ
     *            Category
     * @param supplierName
     *            Supplier Name
     * @param address
     *            Address
     * @return
     */
    public long insertNewItineraryPresetLocation(int tripId, int locId,
                                                 String arrived, String depart, String amt, String itiDesc, String categ, String supplierName,
                                                 String address)
    {
        return insertNewBudgeted(tripId+"", locId+"", arrived, depart, amt, itiDesc, categ, supplierName, address);
    }

    /**
     * Create a new Trip.
     *
     * @param tripId
     *            Id of trip to update
     * @param creationDate
     *            Date trip created
     * @param updateDate
     *            Date trip updated
     * @param closeDate
     *            Date trip closed
     * @param name
     *            Name of trip
     * @param description
     *            Description of trip
     * @return Id of the trip updated
     */
    public long insertNewTrip(int webId, String creationDate, String updateDate, String closeDate, String name,
                              String description) {
        ContentValues cv = new ContentValues();
        cv.put(TRIPS_CREATION_DATE, creationDate);
        cv.put(TRIPS_UPDATE_DATE, updateDate);
        cv.put(TRIPS_CLOSE_DATE, closeDate);
        cv.put(TRIPS_NAME, name);
        cv.put(TRIPS_TRIP_ID, webId);
        cv.put(TRIPS_DESCRIPTION, description);

        return getWritableDatabase().insert(TABLE_NAME_TRIPS, null, cv);
    }

    /**
     * A method that queries the database for all budgeted expenses for a
     * specific date ordering them by the date
     *
     * @param date
     *            the date with which the itineraries will be selected
     * @return the cursor containing all itineraries for that date or null if
     *         the cursor is empty
     */
    @SuppressLint("SimpleDateFormat")
    public Cursor findItinerariesForDate(String dateStr) {
        Cursor c = getReadableDatabase().query(TABLE_NAME_BUDGETED_EXP, null, BUDGETED_EXP_DATE_ARRIVE + " = ?",
                new String[] { dateStr }, null, null, BUDGETED_EXP_DATE_ARRIVE);

        if (c.moveToFirst()) {
            return c;
        }

        else {
            return null;
        }
    }

    /**
     * Finds all itineraries associated with a specific trip.
     *
     * @param tripId
     *            Id of the trip
     * @return Cursor containing results of query
     */
    public Cursor findItinerariesForTrip(int tripId) {
        Cursor c = getReadableDatabase().query(TABLE_NAME_BUDGETED_EXP, null, BUDGETED_EXP_TRIP_ID + "=?",
                new String[] { "" + tripId }, null, null, BUDGETED_EXP_DATE_ARRIVE);
        if (c.moveToFirst()) {
            return c;
        }

        else {
            return null;
        }
    }

    /**
     * Finds all the trips.
     *
     * @return Cursor containing results of query
     */
    public Cursor findAllTrips() {
        Cursor c = getReadableDatabase().query(TABLE_NAME_TRIPS, null, null, null, null, null, TRIPS_CREATION_DATE);
        if (c.moveToFirst()) {
            return c;
        } else {
            return null;
        }
    }

    /**
     * Returns only the first trip in the database.
     *
     * @return Cursor containing results of query
     */
    public Cursor findFirstTripInDB() {
        Cursor c = getReadableDatabase().query(TABLE_NAME_TRIPS, null, null, null, null, null, TRIPS_PK_ID, "1");
        if (c.moveToFirst()) {
            return c;
        } else {
            return null;
        }
    }

    /**
     * Finds all Itineraries in DB.
     *
     * @return Cursor containing results of query
     */
    public Cursor findAllItineraries() {
        Cursor c = getReadableDatabase().query(TABLE_NAME_BUDGETED_EXP, null, null, null, null, null,
                BUDGETED_EXP_DATE_ARRIVE);

        if (c.moveToFirst()) {
            return c;
        }

        else {
            return null;
        }
    }

    /**
     * CRUD method to delete itineraries from database.
     *
     * @param id
     *            The id of the row to be deleted
     * @return number of rows affected
     */
    public int deleteItinerary(int id) {
        return getWritableDatabase().delete(TABLE_NAME_BUDGETED_EXP, BUDGETED_EXP_PK_ID + "=?",
                new String[] { String.valueOf(id) });
    }

    /**
     * CRUD method to find an itinerary row by id.
     *
     * @param id
     *            That belongs to the row that needs to be returned
     * @return Cursor cursor that holds the result of the query
     */
    public Cursor getItinerary(int id) {
        String query = "SELECT " + LOCATIONS_NAME + ", " + TABLE_NAME_LOCATIONS + "." + LOCATIONS_DESCRIPTION + ", "
                + LOCATIONS_CITY + ", " + LOCATIONS_COUNTRY_CODE + ", " + BUDGETED_EXP_DATE_ARRIVE + ", "
                + BUDGETED_EXP_DATE_DEPART + ", " + BUDGETED_EXP_AMOUNT + ", " + TABLE_NAME_BUDGETED_EXP + "."
                + BUDGETED_EXP_DESCRIPTION + ", " + BUDGETED_EXP_CATEGORY + ", " + BUDGETED_EXP_NAME_OF_SUPPLIER + ", "
                + BUDGETED_EXP_ADDRESS + ", " + TABLE_NAME_LOCATIONS + "." + LOCATIONS_PK_ID + " FROM "
                + TABLE_NAME_LOCATIONS + " INNER JOIN " + TABLE_NAME_BUDGETED_EXP + " ON " + TABLE_NAME_LOCATIONS + "."
                + LOCATIONS_PK_ID + " = " + TABLE_NAME_BUDGETED_EXP + "." + BUDGETED_EXP_LOCATION_ID + " WHERE "
                + TABLE_NAME_BUDGETED_EXP + "." + BUDGETED_EXP_PK_ID + " = ?;";

        Cursor cursor = getReadableDatabase().rawQuery(query, new String[] { String.valueOf(id) });

        if (cursor.moveToFirst()) {
            return cursor;
        }

        return null;
    }

    /**
     * Method that returns a cursor containing all the locations.
     *
     * @return all locations in a cursor object
     */
    public Cursor findAllLocations()
    {
        Cursor c = getReadableDatabase().query(TABLE_NAME_LOCATIONS, null, null, null, null, null,
                LOCATIONS_NAME);

        if (c.moveToFirst()) {
            return c;
        }

        else {
            return null;
        }
    }

    /**
     * CRUD method to find an itinerary row by id.
     *
     * @param id
     *            That belongs to the row that needs to be returned
     * @return Cursor cursor that holds the result of the query
     */
    public Cursor getItineraryActual(int id) {
        String query = "SELECT " + ACTUAL_EXP_AMOUNT + " FROM " + TABLE_NAME_ACTUAL_EXP + " WHERE "
                + ACTUAL_EXP_BUDGETED_ID + " = ?;";

        Cursor cursor = getReadableDatabase().rawQuery(query, new String[] { String.valueOf(id) });

        if (cursor.moveToFirst()) {
            return cursor;
        }

        return null;
    }

    /**
     * Updates an itinerary.
     *
     * @param itId
     *            Id of trip associated
     * @param locId
     *            Location: location id
     * @param areaName
     *            Location: name of area
     * @param locationDesc
     *            Location: Description
     * @param city
     *            Location: City
     * @param cc
     *            Location: Country code
     * @param arrived
     *            Date arrived
     * @param depart
     *            Date Departed
     * @param amt
     *            Budgeted Amount
     * @param itiDesc
     *            Itinerary description
     * @param categ
     *            Category
     * @param supplierName
     *            Supplier Name
     * @param address
     *            Address
     *
     * @return ID of the Itinerary updated
     */
    public int updateItinerary(int itId, int locId, String areaName, String locationDesc, String city, String cc,
                               String arrived, String depart, String amt, String itiDesc, String categ, String supplierName,
                               String address) {
        ContentValues cvloc = new ContentValues();
        cvloc.put(LOCATIONS_NAME, areaName);
        cvloc.put(LOCATIONS_DESCRIPTION, locationDesc);
        cvloc.put(LOCATIONS_CITY, city);
        cvloc.put(LOCATIONS_COUNTRY_CODE, cc);
        getWritableDatabase().update(TABLE_NAME_LOCATIONS, cvloc, LOCATIONS_PK_ID + " = ?",
                new String[] { Long.toString(locId) });

        ContentValues cviti = new ContentValues();
        cviti.put(BUDGETED_EXP_DATE_ARRIVE, arrived);
        cviti.put(BUDGETED_EXP_DATE_DEPART, depart);
        cviti.put(BUDGETED_EXP_AMOUNT, amt);
        cviti.put(BUDGETED_EXP_DESCRIPTION, itiDesc);
        cviti.put(BUDGETED_EXP_CATEGORY, categ);
        cviti.put(BUDGETED_EXP_NAME_OF_SUPPLIER, supplierName);
        cviti.put(BUDGETED_EXP_ADDRESS, address);
        return getWritableDatabase().update(TABLE_NAME_BUDGETED_EXP, cviti, BUDGETED_EXP_PK_ID + " = ?",
                new String[] { Long.toString(itId) });
    }

    /**
     * Updates an itinerary.
     *
     * @param itId
     *            Id of trip associated
     * @param locId
     *            Location: location id
     * @param areaName
     *            Location: name of area
     * @param locationDesc
     *            Location: Description
     * @param city
     *            Location: City
     * @param cc
     *            Location: Country code
     * @param arrived
     *            Date arrived
     * @param depart
     *            Date Departed
     * @param budgetAmt
     *            Budgeted Amount
     * @param itiDesc
     *            Itinerary description
     * @param categ
     *            Category
     * @param supplierName
     *            Supplier Name
     * @param address
     *            Address
     *
     * @return ID of the Itinerary updated
     */
    public int updateItineraryWithActual(int itId, int locId, String areaName, String locationDesc, String city,
                                         String cc, String arrived, String depart, String budgetAmt, String itiDesc, String categ,
                                         String supplierName, String address, String actualAmt) {
        ContentValues cvloc = new ContentValues();
        cvloc.put(LOCATIONS_NAME, areaName);
        cvloc.put(LOCATIONS_DESCRIPTION, locationDesc);
        cvloc.put(LOCATIONS_CITY, city);
        cvloc.put(LOCATIONS_COUNTRY_CODE, cc);
        getWritableDatabase().update(TABLE_NAME_LOCATIONS, cvloc, LOCATIONS_PK_ID + " = ?",
                new String[] { Long.toString(locId) });

        ContentValues budgeted = new ContentValues();
        budgeted.put(BUDGETED_EXP_DATE_ARRIVE, arrived);
        budgeted.put(BUDGETED_EXP_DATE_DEPART, depart);
        budgeted.put(BUDGETED_EXP_AMOUNT, budgetAmt);
        budgeted.put(BUDGETED_EXP_DESCRIPTION, itiDesc);
        budgeted.put(BUDGETED_EXP_CATEGORY, categ);
        budgeted.put(BUDGETED_EXP_NAME_OF_SUPPLIER, supplierName);
        budgeted.put(BUDGETED_EXP_ADDRESS, address);

        getWritableDatabase().update(TABLE_NAME_BUDGETED_EXP, budgeted, BUDGETED_EXP_PK_ID + " = ?",
                new String[] { Long.toString(itId) });

        ContentValues actual = new ContentValues();
        actual.put(ACTUAL_EXP_DATE_ARRIVE, arrived);
        actual.put(ACTUAL_EXP_DATE_DEPART, depart);
        actual.put(ACTUAL_EXP_AMOUNT, actualAmt);
        actual.put(ACTUAL_EXP_DESCRIPTION, itiDesc);
        actual.put(ACTUAL_EXP_CATEGORY, categ);
        actual.put(ACTUAL_EXP_NAME_OF_SUPP, supplierName);
        actual.put(ACTUAL_EXP_ADDRESS, address);

        return getWritableDatabase().update(TABLE_NAME_ACTUAL_EXP, actual, ACTUAL_EXP_PK_ID + " = ?",
                new String[] { Long.toString(itId) });
    }

    /**
     * Creates a new Itinerary with an actual price amount using an
     * already existing location.
     *
     * @param tripId
     *            Id of trip associated
     * @param locId Location Id
     * @param arrived
     *            Date arrived
     * @param depart
     *            Date Departed
     * @param buedgetAmt
     *            Budgeted Amount
     * @param itiDesc
     *            Itinerary description
     * @param categ
     *            Category
     * @param supplierName
     *            Supplier Name
     * @param address
     *            Address
     * @param actualAmt
     *            Actual Expense
     * @return
     */
    public long insertNewItineraryWithActualPresetLocation(int tripId, long locId,
                                                           String arrived, String depart, String budgetAmt, String itiDesc, String categ, String supplierName,
                                                           String address, String actualAmt)
    {

        long pk = insertNewBudgeted(tripId+"", locId+"", arrived, depart, budgetAmt, itiDesc, categ, supplierName, address);

        return insertNewActual(arrived, depart, actualAmt, itiDesc, categ, supplierName, address, pk);
    }

    /**
     * Creates a new Itinerary with an actual price amount.
     *
     * @param tripId
     *            Id of trip associated
     * @param areaName
     *            Location: name of area
     * @param locationDesc
     *            Location: Description
     * @param city
     *            Location: City
     * @param cc
     *            Location: Country code
     * @param arrived
     *            Date arrived
     * @param depart
     *            Date Departed
     * @param buedgetAmt
     *            Budgeted Amount
     * @param itiDesc
     *            Itinerary description
     * @param categ
     *            Category
     * @param supplierName
     *            Supplier Name
     * @param address
     *            Address
     * @param actualAmt
     *            Actual Expense
     * @return
     */
    public long insertNewItineraryWithActual(int tripId, String areaName, String locationDesc, String city, String cc,
                                             String arrived, String depart, String budgetAmt, String itiDesc, String categ, String supplierName,
                                             String address, String actualAmt)
    {

        long pkId = insertNewLocation(areaName, locationDesc, city, cc);

        long budgetedId = insertNewBudgeted(tripId+"", pkId+"", arrived, depart, budgetAmt, itiDesc, categ, supplierName, address);

        return insertNewActual(arrived, depart, actualAmt, itiDesc, categ, supplierName, address, budgetedId);
    }

    /**
     * Returns Cursor containing a Trip by its id on the PHP database.
     *
     * @param webId The id of the trip to get
     * @return Cursor containing the results of query
     */
    public Cursor getTripByWebId(int webId) {
        String query = "SELECT " + "*" + " FROM " + TABLE_NAME_TRIPS + " WHERE " + TRIPS_TRIP_ID + " = ?;";

        return getReadableDatabase().rawQuery(query, new String[] { String.valueOf(webId) });
    }


    /**
     * Gets a location by it's name.
     *
     * @param name The name of the location to be retrieved
     * @return Cursor containing the results of query
     */
    public Cursor getLocationByName(String name) {
        String query = "SELECT " + LOCATIONS_PK_ID + " FROM " + TABLE_NAME_LOCATIONS + " WHERE " + LOCATIONS_NAME + " = ?;";

        return getReadableDatabase().rawQuery(query, new String[] { String.valueOf(name) });
    }

    /**
     * Inserts a new location.
     *
     * @param areaName Name of the area
     * @param locationDesc Description of the location
     * @param city City of the location
     * @param cc Country code of the location
     * @return long The id of the newly inserted location
     */
    public long insertNewLocation(String areaName, String locationDesc, String city, String cc)
    {
        ContentValues cvloc = new ContentValues();
        cvloc.put(LOCATIONS_NAME, areaName);
        cvloc.put(LOCATIONS_DESCRIPTION, locationDesc);
        cvloc.put(LOCATIONS_CITY, city);
        cvloc.put(LOCATIONS_COUNTRY_CODE, cc);
        return getWritableDatabase().insert(TABLE_NAME_LOCATIONS, null, cvloc);
    }


    /**
     * Inserts a new budget.
     *
     * @param tripId The id of the trip the budget is associated with
     * @param locationId The id of the location associated with the budget
     * @param arrived Date arrived
     * @param depart Date departed
     * @param budgetAmt Budgeted amount
     * @param itiDesc Itinerary description
     * @param categ Category
     * @param supplierName Supplier name
     * @param address Address
     * @return long The id of the newly inserted budget
     */
    public long insertNewBudgeted(String tripId, String locationId, String arrived, String depart, String budgetAmt, String itiDesc, String categ, String supplierName, String address)
    {
        ContentValues budgeted = new ContentValues();
        budgeted.put(BUDGETED_EXP_DATE_ARRIVE, arrived);
        budgeted.put(BUDGETED_EXP_DATE_DEPART, depart);
        budgeted.put(BUDGETED_EXP_AMOUNT, budgetAmt);
        budgeted.put(BUDGETED_EXP_DESCRIPTION, itiDesc);
        budgeted.put(BUDGETED_EXP_CATEGORY, categ);
        budgeted.put(BUDGETED_EXP_NAME_OF_SUPPLIER, supplierName);
        budgeted.put(BUDGETED_EXP_ADDRESS, address);
        budgeted.put(BUDGETED_EXP_LOCATION_ID, locationId);
        budgeted.put(BUDGETED_EXP_TRIP_ID, tripId);

        return getWritableDatabase().insert(TABLE_NAME_BUDGETED_EXP, null, budgeted);
    }

    /**
     * Inserts a new actual.
     *
     * @param arrived Date arrived
     * @param depart Date departed
     * @param actAmt Actual amount
     * @param itiDesc Itinerary description
     * @param categ Category
     * @param supplierName Supplier name
     * @param address Address
     * @param budgetedId Budgeted Id
     * @return long The id of the newly inserted actual
     */
    public long insertNewActual(String arrived, String depart, String actAmt,
                                String itiDesc, String categ, String supplierName, String address, long budgetedId) {
        ContentValues budgeted = new ContentValues();
        budgeted.put(ACTUAL_EXP_BUDGETED_ID, budgetedId);
        budgeted.put(ACTUAL_EXP_DATE_ARRIVE, arrived);
        budgeted.put(ACTUAL_EXP_DATE_DEPART, depart);
        budgeted.put(ACTUAL_EXP_AMOUNT, actAmt);
        budgeted.put(ACTUAL_EXP_DESCRIPTION, itiDesc);
        budgeted.put(ACTUAL_EXP_CATEGORY, categ);
        budgeted.put(ACTUAL_EXP_NAME_OF_SUPP, supplierName);
        budgeted.put(ACTUAL_EXP_ADDRESS, address);

        return getWritableDatabase().insert(TABLE_NAME_ACTUAL_EXP, null, budgeted);
    }

    /**
     * CRUD method to delete itineraries from database.
     *
     * @param id
     *            The id of the row to be deleted
     * @return number of rows affected
     */
    public int deleteTripItineraries(int tripId) {
        return getWritableDatabase().delete(TABLE_NAME_BUDGETED_EXP, BUDGETED_EXP_TRIP_ID + "=?",
                new String[] { String.valueOf(tripId) });
    }
}