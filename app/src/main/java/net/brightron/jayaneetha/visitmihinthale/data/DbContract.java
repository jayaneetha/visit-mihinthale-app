package net.brightron.jayaneetha.visitmihinthale.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Admin on 3/10/15.
 */
public class DbContract {

    public static final String CONTENT_AUTHORITY = "net.brightron.jayaneetha.visitmihinthale";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_PLACES = "places";
    public static final String PATH_STARTING_PLACES = "routes/starting_places";
    public static final String PATH_ROUTES = "routes";

    public static final class PlacesEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PLACES).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLACES;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLACES;

        public static final String TABLE_NAME = "places";
        public static final String COLUMN_PLACE_NAME = "place_name";
        public static final String COLUMN_COORD_LAT = "coord_lat";
        public static final String COLUMN_COORD_LONG = "coord_long";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_IMAGE_SRC = "image_src";

        public static Uri buildPlacesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildPlaceUri(long id) {
            return CONTENT_URI.buildUpon().appendQueryParameter(PlacesEntry._ID, Long.toString(id)).build();
        }

        public static String getPlaceFromUri(Uri uri) {
            return uri.getQueryParameter(PlacesEntry._ID);
        }
    }

    public static final class RoutesEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ROUTES).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ROUTES;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ROUTES;

        public static final String TABLE_NAME = "routes";

        public static final String COLUMN_STARTING_AT = "starting_at";
        public static final String COLUMN_ORDER = "order";
        public static final String COLUMN_PLACE = "place_id";

        public static Uri buildRoutesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildRoutesStartingPlacesUri() {
            return CONTENT_URI.buildUpon().appendPath(PATH_STARTING_PLACES).build();
        }

        public static Uri buildRouteStartingFromStartingPlace(long starting_place) {
            return CONTENT_URI.buildUpon().appendPath(PATH_ROUTES)
                    .appendQueryParameter(COLUMN_STARTING_AT, Long.toString(starting_place)).build();
        }

        public static String getStartingAtFromUri(Uri uri) {
            return uri.getQueryParameter(COLUMN_STARTING_AT);
        }

        public static String getStartingPlacesFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

    }
}
