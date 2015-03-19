package net.brightron.jayaneetha.visitmihinthale.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by Admin on 3/11/15.
 */
public class PlacesProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final int PLACES = 100;
    private static final int PLACE = 101;
    private static final String sPlaceSelection = PlacesContract.PlacesEntry.TABLE_NAME + "." + PlacesContract.PlacesEntry._ID + " = ? ";
    private final String LOG_TAG = PlacesProvider.class.getSimpleName();
    private PlacesDbHelper mOpenHelper;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = PlacesContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, PlacesContract.PATH_PLACES, PLACES);
        uriMatcher.addURI(authority, PlacesContract.PATH_PLACE + "/#", PLACE);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new PlacesDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case PLACES:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        PlacesContract.PlacesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case PLACE:
                String placeID = PlacesContract.PlacesEntry.getPlaceIdFromUri(uri);
                retCursor = mOpenHelper.getReadableDatabase().query(
                        PlacesContract.PlacesEntry.TABLE_NAME,
                        projection,
                        sPlaceSelection,
                        new String[]{placeID},
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri " + uri);
        }
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case PLACES:
                return PlacesContract.PlacesEntry.CONTENT_TYPE;
            case PLACE:
                return PlacesContract.PlacesEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown Uri " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case PLACES: {
                long _id = db.insert(PlacesContract.PlacesEntry.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = PlacesContract.PlacesEntry.buildPlacesUri(_id);
                } else {
                    throw new SQLException("Failed to insert :" + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        if (null == selection) selection = "1";

        switch (match) {
            case PLACES:
                rowsDeleted = db.delete(PlacesContract.PlacesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case PLACES:
                rowsUpdated = db.update(PlacesContract.PlacesEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
