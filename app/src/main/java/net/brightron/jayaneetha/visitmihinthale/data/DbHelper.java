package net.brightron.jayaneetha.visitmihinthale.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Admin on 3/10/15.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "VisitMihinthale";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_PLACES_TABLE = "CREATE TABLE " + DbContract.PlacesEntry.TABLE_NAME + "(" +
                DbContract.PlacesEntry._ID + " INTEGER PRIMARY KEY," +
                DbContract.PlacesEntry.COLUMN_PLACE_NAME + " TEXT UNIQUE NOT NULL," +
                DbContract.PlacesEntry.COLUMN_COORD_LAT + " REAL NOT NULL," +
                DbContract.PlacesEntry.COLUMN_COORD_LONG + " REAL NOT NULL," +
                DbContract.PlacesEntry.COLUMN_DESCRIPTION + " TEXT NULL," +
                DbContract.PlacesEntry.COLUMN_IMAGE_SRC + " TEXT NULL" +
                " );";
        final String SQL_CREATE_ROUTES_TABLE = "CREATE TABLE " + DbContract.RoutesEntry.TABLE_NAME + " (" +
                DbContract.RoutesEntry._ID + " INTEGER PRIMARY KEY," +
                DbContract.RoutesEntry.COLUMN_STARTING_AT + "INTEGER NOT NULL," +
                DbContract.RoutesEntry.COLUMN_ORDER + "INTEGER NULL," +
                DbContract.RoutesEntry.COLUMN_PLACE + "INTEGER NOT NULL," +

                "FOREIGN KEY (" + DbContract.RoutesEntry.COLUMN_PLACE + ") REFERENCES " +
                DbContract.PlacesEntry.TABLE_NAME + " (" + DbContract.PlacesEntry._ID + ");";
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DbContract.PlacesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DbContract.RoutesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
