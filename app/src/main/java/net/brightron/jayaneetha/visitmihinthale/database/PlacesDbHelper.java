package net.brightron.jayaneetha.visitmihinthale.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Admin on 3/11/15.
 */
public class PlacesDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 3;
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
        final String SQL_INSERT_INTO_PLACES_TABLE_1 = "INSERT INTO places VALUES ('1', 'Mihinthale', '8.3523765', '80.5152047', 'Mihintale is a mountain peak near Anuradhapura in Sri Lanka. It is believed by Sri Lankans to be the site of a meeting between the Buddhist monk Mahinda and King Devanampiyatissa which inaugurated the presence of Buddhism in Sri Lanka. It is now a pilgrimage site, and the site of several religious monuments and abandoned structures.', 'mihinthale');";
        final String SQL_INSERT_INTO_PLACES_TABLE_2 = "INSERT INTO places VALUES ('2', 'Kantaka Chethiya', '8.3523552', '80.5140567', 'Kantaka Chethiya was renovated in 1930’s to the current status. When this stupa was discovered, it has been a just a mound of earth covered by various debris. This has been known as the Kiribadapavu Dagaba, Kiribat Vehera, or Giribhanda during this time. But a stone inscription found close by has identified the original name of this stupa as Kantaka Chethiya.', 'kantaka-chethiya');";
        final String SQL_INSERT_INTO_PLACES_TABLE_3 = "INSERT INTO places VALUES ('3', 'Mihinthale Ancient Hospital Complex', '8.3550734', '80.5118734', 'It was reported by Chinese Mahayana Buddhist priest “Fa- Hsien” who visited the cave in the 5th century that Mihinthalawa was home to over 2000 Buddhist monks at that time. To support that number of monks, Mihintalawa should have been a complete monastery with all facilities for the resident monks.', 'mihinthale-hospital');";

        sqLiteDatabase.execSQL(SQL_CREATE_PLACES_TABLE);
        sqLiteDatabase.execSQL(SQL_INSERT_INTO_PLACES_TABLE_1);
        sqLiteDatabase.execSQL(SQL_INSERT_INTO_PLACES_TABLE_2);
        sqLiteDatabase.execSQL(SQL_INSERT_INTO_PLACES_TABLE_3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PlacesContract.PlacesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }


}
