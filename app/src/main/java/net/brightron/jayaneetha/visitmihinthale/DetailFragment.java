package net.brightron.jayaneetha.visitmihinthale;

/**
 * Created by Admin on 3/13/15.
 */

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import net.brightron.jayaneetha.visitmihinthale.database.PlacesContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String LOG_TAG = DetailFragment.class.getSimpleName();
    private static final int DETAIL_LOADER = 0;
    /*static final String DETAIL_URI = "URI";*/
    static final String DETAIL_URI = "DFTAG";
    private String mText;
    private static final String SHARE_HASHTAG = " #VisitMihinthale";
    private String GEO_COORD = "geo:0,0";
    private Uri mUri;

    private ShareActionProvider mShareActionProvider;

    private final String[] PLACES_COLUMNS = {
            PlacesContract.PlacesEntry.TABLE_NAME + "." + PlacesContract.PlacesEntry._ID,
            PlacesContract.PlacesEntry.COLUMN_PLACE_NAME,
            PlacesContract.PlacesEntry.COLUMN_COORD_LAT,
            PlacesContract.PlacesEntry.COLUMN_COORD_LONG,
            PlacesContract.PlacesEntry.COLUMN_DESCRIPTION,
            PlacesContract.PlacesEntry.COLUMN_IMAGE_SRC
    };

    static final int COL_PLACE_ID = 0;
    static final int COL_PLACE_NAME = 1;
    static final int COL_PLACE_COORD_LAT = 2;
    static final int COL_PLACE_COORD_LONG = 3;
    static final int COL_PLACE_DESCRIPTION = 4;
    static final int COL_PLACE_IMAGE_SRC = 5;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    void onPlaceChanged(long _id) {
        Uri uri = mUri;
        if (null != uri) {
            String placeId = PlacesContract.PlacesEntry.getPlaceIdFromUri(uri);
            Uri updatedUri = PlacesContract.PlacesEntry.buildPlaceUri(_id);
            mUri = updatedUri;
            getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detailfragment, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        if (mText != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mText + SHARE_HASHTAG);
        return shareIntent;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);

        super.onActivityCreated(savedInstanceState);

    }

    public void openPlaceMap() {
            /*Uri geoLocation = Uri.parse("geo:0,0?").buildUpon()
                    .appendQueryParameter("q", GEO_COORD).build();*/
        Uri geoLocation = Uri.parse(GEO_COORD);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);

       /* Log.v(LOG_TAG, "GEO LoACTIO" + geoLocation.toString());*/

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d(LOG_TAG, "Couldn't call " + GEO_COORD + ", no receiving apps installed!");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        final Button openMap = (Button) rootView.findViewById(R.id.open_map);
        openMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPlaceMap();
            }
        });

        Log.v(LOG_TAG, "KKKKKKKKKKK");

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(DetailFragment.DETAIL_URI);
            Log.v(LOG_TAG, "wwwwwwWWWW");
        }
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            Log.v(LOG_TAG, "QQQQQQQQQ");
            mUri = Uri.parse(intent.getDataString());

        }
        /*Log.v(LOG_TAG, "OnCreateView");
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        Intent intent = getActivity().getIntent();
        if(intent!=null){
            mText = intent.getDataString();
            TextView textView = (TextView)rootView.findViewById(R.id.detail_textview);
            textView.setText(mText);
        }
        return rootView;*/
        return rootView;
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        /*Log.v(LOG_TAG,"OnCreateLoader");*/

        /*Intent intent = getActivity().getIntent();
        if(intent==null || intent.getData() == null) {
            return null;
        }
        Log.v(LOG_TAG,intent.getData().toString());*/
        if (null != mUri) {
            return new android.support.v4.content.CursorLoader(
                    getActivity(),
                    mUri,
                    PLACES_COLUMNS,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor cursor) {
        /*Log.v(LOG_TAG,"onLoadFinished");*/
        if (!cursor.moveToFirst()) {
            return;
        }
        String place_name = cursor.getString(COL_PLACE_NAME);
        String description = cursor.getString(COL_PLACE_DESCRIPTION);
        long coord_long = cursor.getLong(COL_PLACE_COORD_LONG);
        long coord_lat = cursor.getLong(COL_PLACE_COORD_LAT);
        String image_src = cursor.getString(COL_PLACE_IMAGE_SRC);

        mText = "I'm at " + place_name;
        GEO_COORD = "geo:" + Long.toString(coord_lat) + "," + Long.toString(coord_long);

        TextView placeNameTV = (TextView) getView().findViewById(R.id.detail_place_name);
        placeNameTV.setText(place_name);

        TextView descriptionTV = (TextView) getView().findViewById(R.id.detail_place_description);
        descriptionTV.setText(description);

        ImageView detailImage = (ImageView) getView().findViewById(R.id.detail_image);
        detailImage.setImageResource(getImageResource(image_src));
    }

    int getImageResource(String image_src) {
        int resID;
        switch (image_src) {
            case "mihinthale":
                resID = R.drawable.mihinthale;
                break;
            case "kantaka-chethiya":
                resID = R.drawable.kantaka_chethiya;
                break;
            case "mihinthale-hospital":
                resID = R.drawable.mihinthale_hospital;
                break;
            default:
                resID = R.drawable.mihinthale;
        }
        return resID;
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }


}