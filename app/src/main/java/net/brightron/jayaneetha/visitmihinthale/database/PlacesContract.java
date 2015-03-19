package net.brightron.jayaneetha.visitmihinthale.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Admin on 3/11/15.
 */
public class PlacesContract {
    public static final String CONTENT_AUTHORITY = "net.brightron.jayaneetha.visitmihinthale";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_PLACES = "places";
    public static final String PATH_PLACE = "place";

    public static final class PlacesEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PLACES).build();
        public static final Uri CONTENT_URI_PLACE = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PLACE).build();
        public static final String TABLE_NAME = "places";
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLACES;
        public static final String COLUMN_PLACE_NAME = "place_name";
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLACES;
        public static final String COLUMN_COORD_LAT = "coord_lat";
        public static final String COLUMN_COORD_LONG = "coord_long";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_IMAGE_SRC = "image_src";

        public static Uri buildPlacesUri(long _id) {
            return ContentUris.withAppendedId(CONTENT_URI, _id);
        }

        public static Uri buildPlaceUri(long _id) {
            return CONTENT_URI_PLACE.buildUpon().appendPath(Long.toString(_id)).build();
        }

        public static String getPlaceIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }


    }
}
