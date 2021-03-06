package com.andrespenaloza.intellitracker.objects;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;

/**
 * Created by Andres on 27-06-2015.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {
    ////
    public static final String TABLE_TRACKINGITEM = "trackingitems";
    public static final String TRACKINGITEM_ID = "id";
    public static final String TRACKINGITEM_NAME = "name";
    public static final String TRACKINGITEM_TRACK_NUMBER = "trackNumber";
    public static final String TRACKINGITEM_DATE_CREATED = "dateCreated";
    public static final String TRACKINGITEM_DATE_LAST_QUERY = "dateLastQuery";
    public static final String TRACKINGITEM_LAST_TRACK_RESULT_LIST = "lastTrackResultList";
    public static final String TRACKINGITEM_LAST_TRACK_PACKAGE_STATUS = "lastTrackPackageStatus";
    public static final String TRACKINGITEM_PACKAGE_STATUS_MANUAL = "packageStatusManual";
    public static final String TRACKINGITEM_ORIGIN_COUNTRY = "originCountry";
    public static final String TRACKINGITEM_DESTINATION_COUNTRY = "destinationCountry";
    public static final String TRACKINGITEM_COURIER_IDS = "courierIds";
    ////
    public static final String TABLE_LABELCOLORS = "labelColors";
    public static final String LABELCOLORS_NAME = "name";
    public static final String LABELCOLORS_TEXT_COLOR = "textColor";
    public static final String LABELCOLORS_BACKGROUND_COLOR = "backgroundColor";
    ////
    public static final String TABLE_LABEL = "labels";
    public static final String LABEL_ID = "id";
    public static final String LABEL_NAME = "name";
    public static final String LABEL_COLOR_NAME = "colorName";
    ////
    public static final String TABLE_LABEL_TRACKINGITEM = "labelTrackingItems";
    public static final String LT_ID_TRACKINGITEM = "idTrackingItem";
    public static final String LT_ID_LABEL = "idLabel";
    ////

    private static final String DATABASE_NAME = "intellitracker.db";
    private static final int DATABASE_VERSION = 7;

    // Database creation sql statement
    private static final String DATABASE_CREATE_TRAKINGITEM = "create table "
            + TABLE_TRACKINGITEM + "("
            + TRACKINGITEM_ID + " integer primary key autoincrement, "
            + TRACKINGITEM_NAME + " text not null, "
            + TRACKINGITEM_TRACK_NUMBER + " text not null, "
            + TRACKINGITEM_DATE_CREATED + " text not null, "
            + TRACKINGITEM_DATE_LAST_QUERY + " text, "
            + TRACKINGITEM_LAST_TRACK_RESULT_LIST + " string, "
            + TRACKINGITEM_LAST_TRACK_PACKAGE_STATUS + " integer, "
            + TRACKINGITEM_PACKAGE_STATUS_MANUAL + " integer, "
            + TRACKINGITEM_ORIGIN_COUNTRY + " string, "
            + TRACKINGITEM_DESTINATION_COUNTRY + " string, "
            + TRACKINGITEM_COURIER_IDS + " text "
            + ");";
    private static final String DATABASE_CREATE_LABELCOLORS = "create table "
            + TABLE_LABELCOLORS + "("
            + LABELCOLORS_NAME + " text primary key , "
            + LABELCOLORS_TEXT_COLOR + " integer not null, "
            + LABELCOLORS_BACKGROUND_COLOR + " integer not null "
            + ");";
    private static final String DATABASE_CREATE_LABEL = "create table "
            + TABLE_LABEL + "("
            + LABEL_ID + " integer primary key autoincrement, "
            + LABEL_NAME + " text not null, "
            + LABEL_COLOR_NAME + " text not null, "
            + " FOREIGN KEY ("+LABEL_COLOR_NAME+") REFERENCES "+TABLE_LABELCOLORS+" ("+LABELCOLORS_NAME+")"
            + ");";
    private static final String DATABASE_CREATE_LABEL_TRACKINGITEM = "create table "
            + TABLE_LABEL_TRACKINGITEM + "("
            + LT_ID_TRACKINGITEM + " integer not null, "
            + LT_ID_LABEL + " integer not null, "
            + "PRIMARY KEY ("
            + LT_ID_TRACKINGITEM + ", "
            + LT_ID_LABEL + "),"
            + " FOREIGN KEY ("+LT_ID_TRACKINGITEM+") REFERENCES "+TABLE_TRACKINGITEM+" ("+TRACKINGITEM_ID+"),"
            + " FOREIGN KEY ("+LT_ID_LABEL+") REFERENCES "+TABLE_LABEL+" ("+LABEL_ID+")"
            + ");";
    private static final String DATABASE_DROP_ALL =
            "DROP TABLE " + TABLE_TRACKINGITEM +
            "; DROP TABLE " + TABLE_LABELCOLORS +
            "; DROP TABLE " + TABLE_LABEL +
            "; DROP TABLE " + TABLE_LABEL_TRACKINGITEM + ";" ;

    MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
//        database.execSQL("DROP TABLE " + TABLE_TRACKINGITEM + ";");
//        database.execSQL("DROP TABLE " + TABLE_LABELCOLORS + ";");
//        database.execSQL("DROP TABLE " + TABLE_LABEL + ";");
//        database.execSQL("DROP TABLE " + TABLE_LABEL_TRACKINGITEM + ";");

//        HashMap<Integer, TrackingItem> items = getAllTrackingItems(database);
//        database.execSQL("DROP TABLE " + TABLE_TRACKINGITEM + ";");
//        database.execSQL(DATABASE_CREATE_TRAKINGITEM);
//        for (TrackingItem i : items.values()){
//            saveTrackingItem(i,database);
//        }

        database.execSQL(DATABASE_CREATE_TRAKINGITEM);
        database.execSQL(DATABASE_CREATE_LABELCOLORS);
        database.execSQL(DATABASE_CREATE_LABEL);
        database.execSQL(DATABASE_CREATE_LABEL_TRACKINGITEM);
    }

    public HashMap<Integer, TrackingItem> getAllTrackingItems(SQLiteDatabase database) {
        HashMap<Integer, TrackingItem> trackingItems = new HashMap<Integer, TrackingItem>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_TRACKINGITEM,
                ItemManager.TrackingItemColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TrackingItem trackingItem = ItemManager.cursorToTrackingItem(cursor);
            trackingItems.put(trackingItem.getId(),trackingItem);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return trackingItems;
    }

    public boolean saveTrackingItem(TrackingItem trackingItem,SQLiteDatabase database) {
        DateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.TRACKINGITEM_ID, trackingItem.getId());
        values.put(MySQLiteHelper.TRACKINGITEM_NAME, trackingItem.getName());
        values.put(MySQLiteHelper.TRACKINGITEM_TRACK_NUMBER, trackingItem.getTrackingNumber());
        values.put(MySQLiteHelper.TRACKINGITEM_DATE_CREATED, iso8601Format.format(trackingItem.getDateCreated()) );
        values.put(MySQLiteHelper.TRACKINGITEM_DATE_LAST_QUERY, iso8601Format.format(trackingItem.getLastQuery()));
        values.put(MySQLiteHelper.TRACKINGITEM_LAST_TRACK_RESULT_LIST, TrackingItem.statusListToString(trackingItem.getStatusList()));
        values.put(MySQLiteHelper.TRACKINGITEM_LAST_TRACK_PACKAGE_STATUS, trackingItem.getPackageStatus());
        values.put(MySQLiteHelper.TRACKINGITEM_PACKAGE_STATUS_MANUAL,trackingItem.getPackageStatusManual());
        values.put(MySQLiteHelper.TRACKINGITEM_ORIGIN_COUNTRY, trackingItem.getOriginCountry());
        values.put(MySQLiteHelper.TRACKINGITEM_DESTINATION_COUNTRY, trackingItem.getDestinationCountry());
        values.put(MySQLiteHelper.TRACKINGITEM_COURIER_IDS, TrackingItem.courierIdsToString(trackingItem.getCourierIds()));
        long insertId = database.insert(MySQLiteHelper.TABLE_TRACKINGITEM,null,values);
        return insertId != -1;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TRACKINGITEM_NAME);
        onCreate(db);
    }
}
