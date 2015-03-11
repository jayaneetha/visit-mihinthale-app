package net.brightron.jayaneetha.visitmihinthale.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Admin on 3/11/15.
 */
public class PlacesDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "visit_mihinthale";

    public PlacesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_PLACES_TABLE = "CREATE TABLE " + PlacesContract.PlacesEntry.TABLE_NAME + " (" +
                PlacesContract.PlacesEntry._ID + " INTEGER PRIMARY KEY," +
                PlacesContract.PlacesEntry.COLUMN_PLACE_NAME + " TEXT UNIQUE NOT NULL," +
                PlacesContract.PlacesEntry.COLUMN_COORD_LAT + " REAL NOT NULL," +
                PlacesContract.PlacesEntry.COLUMN_COORD_LONG + " REAL NOT NULL," +
                PlacesContract.PlacesEntry.COLUMN_DESCRIPTION + " TEXT NULL," +
                PlacesContract.PlacesEntry.COLUMN_IMAGE_SRC + " TEXT NULL" +
                " );";
        sqLiteDatabase.execSQL(SQL_CREATE_PLACES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PlacesContract.PlacesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
