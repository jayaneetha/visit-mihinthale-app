package net.brightron.jayaneetha.visitmihinthale.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.graphics.SweepGradient;
import android.net.Uri;

/**
 * Created by Admin on 3/11/15.
 */
public class DbProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DbHelper mOpenHelper;

    private static final int ROUTES = 100;
    private static final int ROUTE_STARTING_FROM_STARTING_PLACE = 102;
    private static final int STARTING_PLACES = 101;
    private static final int PLACES = 200;
    private static final int PLACE = 201;

    private static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DbContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, DbContract.PATH_PLACES, PLACES);
        matcher.addURI(authority, DbContract.PATH_PLACES + "/#", PLACE);

        matcher.addURI(authority, DbContract.PATH_ROUTES, ROUTES);
        matcher.addURI(authority, DbContract.PATH_ROUTES + "/" + DbContract.PATH_STARTING_PLACES, STARTING_PLACES);
        matcher.addURI(authority, DbContract.PATH_ROUTES + "/#", ROUTE_STARTING_FROM_STARTING_PLACE);

        return matcher;
    }

    private static final SQLiteQueryBuilder sRoutesByStartingPlaceQueryBuilder;
    private static final SQLiteQueryBuilder sStartingPlaces;


    static {
        sRoutesByStartingPlaceQueryBuilder = new SQLiteQueryBuilder();
        sStartingPlaces = new SQLiteQueryBuilder();
        //FROM routes INNER JOIN places ON routes.place = places.id
        sRoutesByStartingPlaceQueryBuilder.setTables(
                DbContract.RoutesEntry.TABLE_NAME + " INNER JOIN " +
                        DbContract.PlacesEntry.TABLE_NAME + " ON " +
                        DbContract.RoutesEntry.TABLE_NAME + "." + DbContract.RoutesEntry.COLUMN_PLACE +
                        " = " +
                        DbContract.PlacesEntry.TABLE_NAME + "." + DbContract.PlacesEntry._ID
        );
        //FROM routes INNER JOIN places ON routes.srating_at = places.id
        sStartingPlaces.setTables(
                DbContract.RoutesEntry.TABLE_NAME + " INNER JOIN " +
                        DbContract.PlacesEntry.TABLE_NAME + " ON " +
                        DbContract.RoutesEntry.TABLE_NAME + "." + DbContract.RoutesEntry.COLUMN_STARTING_AT +
                        " = " +
                        DbContract.PlacesEntry.TABLE_NAME + "." + DbContract.PlacesEntry._ID
        );
    }

    private static final String sPlaceSelection =
            DbContract.PlacesEntry.TABLE_NAME + "." + DbContract.PlacesEntry._ID + " = ? ";


    private Cursor getPlacesOfRoutes(Uri uri, String[] projection, String[] selectionArgs, String sortOrder) {

        String starting_place = DbContract.RoutesEntry.getStartingAtFromUri(uri);

        return sRoutesByStartingPlaceQueryBuilder.query(mOpenHelper.getWritableDatabase(),
                projection,
                sPlaceSelection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }


    private Cursor getStartingPlaces(String[] projection, String sortOrder) {

        return sStartingPlaces.query(mOpenHelper.getReadableDatabase(),
                projection,
                null,
                null,
                DbContract.PlacesEntry.COLUMN_PLACE_NAME,
                null,
                sortOrder
        );

    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case ROUTES:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DbContract.RoutesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case PLACES:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DbContract.PlacesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null
                        , null,
                        sortOrder
                );
                break;
            case PLACE:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DbContract.PlacesEntry.TABLE_NAME,
                        projection,
                        sPlaceSelection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case STARTING_PLACES:
                retCursor = getStartingPlaces(projection, sortOrder);
                break;
            case ROUTE_STARTING_FROM_STARTING_PLACE:
                retCursor = getPlacesOfRoutes(uri, projection, selectionArgs, sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown Uri " + uri);
        }
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
            case PLACE:
                return DbContract.RoutesEntry.CONTENT_ITEM_TYPE;
            case STARTING_PLACES:
                return DbContract.RoutesEntry.CONTENT_TYPE;
            case ROUTE_STARTING_FROM_STARTING_PLACE:
                return DbContract.RoutesEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown Uri" + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
