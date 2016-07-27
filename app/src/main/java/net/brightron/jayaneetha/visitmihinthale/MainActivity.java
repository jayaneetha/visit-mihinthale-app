package net.brightron.jayaneetha.visitmihinthale;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import net.brightron.jayaneetha.visitmihinthale.database.PlacesContract;


public class MainActivity extends ActionBarActivity implements FragmentMain.Callback {
    public static final String DETAILFRAGMENT_TAG = "DFTAG";
    public static boolean mTwoPane;
    public static String connectedNetwork;
    static boolean mSavedInstance = false;
    private static boolean rotate = false;
    private static Uri selected_uri = PlacesContract.DEFAULT_URI;
    private final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onDestroy() {
        rotate = true;
        super.onDestroy();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectedNetwork = new Util().getNetworkClass(getApplicationContext());

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
                mSavedInstance = false;
            } else {
                mSavedInstance = true;
            }
        } else {
            mTwoPane = false;
        }
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
//        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }
       */

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        DetailFragment df = (DetailFragment) getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG);
        if (null != df) {
            df.onPlaceChanged(1);
        }

        if (mTwoPane) {
            Bundle args = new Bundle();
            args.putParcelable(DetailFragment.DETAIL_URI, selected_uri);

            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setArguments(args);

            if (!mSavedInstance) {
                getSupportFragmentManager().beginTransaction().replace(R.id.detail_container, detailFragment, DETAILFRAGMENT_TAG).commit();
            }

        } else {
            if (rotate) {
                Intent intent = new Intent(this, DetailActivity.class).setData(selected_uri);
                startActivity(intent);
                rotate = false;
            }
        }

    }

    @Override
    public void onItemSelected(Uri uri) {
        selected_uri = uri;
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
