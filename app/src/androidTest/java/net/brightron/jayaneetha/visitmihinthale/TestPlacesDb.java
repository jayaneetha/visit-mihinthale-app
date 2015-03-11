package net.brightron.jayaneetha.visitmihinthale;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import net.brightron.jayaneetha.visitmihinthale.database.PlacesContract;
import net.brightron.jayaneetha.visitmihinthale.database.PlacesDbHelper;

/**
 * Created by Admin on 3/11/15.
 */
public class TestPlacesDb extends AndroidTestCase {
    public static final String LOG_TAG = TestPlacesDb.class.getSimpleName();

    void deleteDatabase() {
        mContext.deleteDatabase(PlacesDbHelper.DATABASE_NAME);
    }

    @Override
    protected void setUp() throws Exception {
        deleteDatabase();
        assertEquals(1, insertPlaces());
        testTables();
    }

    public long insertPlaces() {
        PlacesDbHelper placesDbHelper = new PlacesDbHelper(mContext);
        SQLiteDatabase sqLiteDatabase = placesDbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(PlacesContract.PlacesEntry.COLUMN_PLACE_NAME, "ASD");
        contentValues.put(PlacesContract.PlacesEntry.COLUMN_COORD_LAT, 80.0);
        contentValues.put(PlacesContract.PlacesEntry.COLUMN_COORD_LONG, 8.0);
        contentValues.put(PlacesContract.PlacesEntry.COLUMN_DESCRIPTION, "Desc");
        contentValues.put(PlacesContract.PlacesEntry.COLUMN_IMAGE_SRC, "Image");

        long placeId = sqLiteDatabase.insert(PlacesContract.PlacesEntry.TABLE_NAME, null, contentValues);

        return placeId;
    }

    public Cursor testPlacesTable() {
        PlacesDbHelper placesDbHelper = new PlacesDbHelper(mContext);
        SQLiteDatabase sqLiteDatabase = placesDbHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(PlacesContract.PlacesEntry.TABLE_NAME, null, null, null, null, null, null);
        return cursor;
    }

    public void testTables() {
        Cursor cursor = testPlacesTable();
        assertTrue("Error: No Records returned from location query", cursor.moveToFirst());
        cursor.close();
    }
}
