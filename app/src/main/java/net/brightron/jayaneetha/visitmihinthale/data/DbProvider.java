package net.brightron.jayaneetha.visitmihinthale.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by Admin on 3/10/15.
 */
public class DbProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DbHelper mOpenHelper;

    private static final int ROUTES = 100;
    static final int PLACES = 300;

    private static final SQLiteQueryBuilder sPlacesByRoutesQueryBuilder;

    static {
        sPlacesByRoutesQueryBuilder = new SQLiteQueryBuilder();
        sPlacesByRoutesQueryBuilder.setTables(
                DbContract.RoutesEntry.TABLE_NAME + " INNER JOIN " + DbContract.PlacesEntry.TABLE_NAME + " ON " +
                        DbContract.RoutesEntry.TABLE_NAME +
                        "." + DbContract.RoutesEntry.COLUMN_PLACE +
                        " = " + DbContract.PlacesEntry.TABLE_NAME +
                        "." + DbContract.PlacesEntry._ID
        );
    }

    private static final String sRoutesSelection = DbContract.RoutesEntry.TABLE_NAME +
            "." + DbContract.RoutesEntry.COLUMN_STARTING_AT + " = ? ";

    private Cursor getPlacesByRoutes(Uri uri, String[] projection, String sortOrder) {
        String starting_at = DbContract.RoutesEntry.getRouteStartAtFromUri(uri);
        String[] selectionArgs = new String[]{starting_at};
        String selection = sRoutesSelection;

        return sPlacesByRoutesQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DbContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, DbContract.PATH_ROUTES, ROUTES);
        matcher.addURI(authority, DbContract.PATH_PLACES, PLACES);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor = null;

        switch (sUriMatcher.match(uri)) {
            case ROUTES: {
                retCursor = getPlacesByRoutes(uri, projection, sortOrder);
                break;
            }

        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ROUTES:
                return DbContract.RoutesEntry.CONTENT_TYPE;
            case PLACES:
                return DbContract.PlacesEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case PLACES: {
                long _id = db.insert(DbContract.PlacesEntry.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = DbContract.PlacesEntry.buildPlacesUri(_id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case ROUTES: {
                long _id = db.insert(DbContract.RoutesEntry.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = DbContract.RoutesEntry.buildRoutesUri(_id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if (null == selection) selection = "1";
        switch (match) {
            case PLACES:
                rowsDeleted = db.delete(DbContract.PlacesEntry.TABLE_NAME, selection, selectionArgs);
                break;

        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
