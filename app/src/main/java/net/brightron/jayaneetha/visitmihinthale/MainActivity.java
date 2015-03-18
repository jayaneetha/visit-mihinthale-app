package net.brightron.jayaneetha.visitmihinthale;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import net.brightron.jayaneetha.visitmihinthale.database.PlacesContract;
import net.brightron.jayaneetha.visitmihinthale.database.PlacesDbHelper;


public class MainActivity extends ActionBarActivity implements FragmentMain.Callback {
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    public static boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        if (findViewById(R.id.detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.detail_container, new DetailFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }

/*
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new FragmentMain())
                    .commit();
        }*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_test_insert) {
            /*long _id = addPlace("Two", 89.0, 8.4, "dere");*/
            /*Log.v(LOG_TAG, "TEXT " + _id);*/
        }

        return super.onOptionsItemSelected(item);
    }

    /*long addPlace(String placeName, double coord_lat, double coord_long, String description) {
        PlacesDbHelper dbHelper = new PlacesDbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(PlacesContract.PlacesEntry.COLUMN_PLACE_NAME, placeName);
        contentValues.put(PlacesContract.PlacesEntry.COLUMN_COORD_LAT, coord_lat);
        contentValues.put(PlacesContract.PlacesEntry.COLUMN_COORD_LONG, coord_long);
        contentValues.put(PlacesContract.PlacesEntry.COLUMN_DESCRIPTION, description);
        contentValues.put(PlacesContract.PlacesEntry.COLUMN_IMAGE_SRC, "IMAFE");

        long locationRowId;
        locationRowId = db.insert(PlacesContract.PlacesEntry.TABLE_NAME, null, contentValues);
        return locationRowId;
    }
*/
    @Override
    protected void onResume() {
        super.onResume();

        DetailFragment df = (DetailFragment) getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG);
        if (null != df) {
            df.onPlaceChanged(1);
        }

    }

    @Override
    public void onItemSelected(Uri uri) {
        if (mTwoPane) {
            Bundle args = new Bundle();
            args.putParcelable(DetailFragment.DETAIL_URI, uri);

            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction().replace(R.id.detail_container, detailFragment, DETAILFRAGMENT_TAG).commit();

        } else {
            Intent intent = new Intent(this, DetailActivity.class).setData(uri);
            startActivity(intent);
        }
    }
}
