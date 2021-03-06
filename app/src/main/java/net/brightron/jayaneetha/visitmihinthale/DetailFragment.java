package net.brightron.jayaneetha.visitmihinthale;

/**
 * Created by Admin on 3/13/15.
 */


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
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
import android.widget.Toast;

import net.brightron.jayaneetha.visitmihinthale.database.PlacesContract;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    /*static final String DETAIL_URI = "URI";*/
    static final String DETAIL_URI = "DFTAG";
    static final int COL_PLACE_ID = 0;
    static final int COL_PLACE_NAME = 1;
    static final int COL_PLACE_COORD_LAT = 2;
    static final int COL_PLACE_COORD_LONG = 3;
    static final int COL_PLACE_DESCRIPTION = 4;
    static final int COL_PLACE_IMAGE_SRC = 5;
    private static final int DETAIL_LOADER = 0;
    private static final String SHARE_HASHTAG = " #VisitMihinthale";
    private static final String SHARE_TITLE = "Visit Mihinthale";
    private final String LOG_TAG = DetailFragment.class.getSimpleName();
    private final String[] PLACES_COLUMNS = {
            PlacesContract.PlacesEntry.TABLE_NAME + "." + PlacesContract.PlacesEntry._ID,
            PlacesContract.PlacesEntry.COLUMN_PLACE_NAME,
            PlacesContract.PlacesEntry.COLUMN_COORD_LAT,
            PlacesContract.PlacesEntry.COLUMN_COORD_LONG,
            PlacesContract.PlacesEntry.COLUMN_DESCRIPTION,
            PlacesContract.PlacesEntry.COLUMN_IMAGE_SRC
    };
    private String mText;
    private String GEO_COORD = "geo:0,0";
    private Uri mUri;
    private ShareActionProvider mShareActionProvider;

    private TextView mPlaceName;
    private ImageView mImage;
    private TextView mDescription;

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
            mShareActionProvider.setShareIntent(createImageShareIntent());
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    /*private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mText + SHARE_HASHTAG);
        return shareIntent;
    }*/

    private Intent createImageShareIntent() {

        ImageView imageView = (ImageView) getView().findViewById(R.id.detail_image);
        Uri imageUri = getLocalBitmapUri(imageView);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, mText + SHARE_HASHTAG);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, SHARE_TITLE);
        if (imageUri != null) {
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            shareIntent.setType("image/*");
        } else {
            shareIntent.setType("text/plain");
        }
        return shareIntent;
    }

    // Returns the URI path to the Bitmap displayed in specified ImageView
    //Referred from : https://guides.codepath.com/android/Sharing-Content-with-Intents
    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);

    }

    public void openPlaceMap() {
        if (MainActivity.connectedNetwork.equals("2G")) {
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "You are on a 2G Network", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Uri geoLocation = Uri.parse(GEO_COORD);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(geoLocation);

            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                MainActivity.mSavedInstance = true;
                startActivity(intent);
            } else {
                Log.d(LOG_TAG, "Couldn't call " + GEO_COORD + ", no receiving apps installed!");
            }
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

        Bundle arguments = getArguments();

        if (MainActivity.mTwoPane) {
            if (arguments != null) {
                mUri = arguments.getParcelable(DetailFragment.DETAIL_URI);
            }
        } else {
            Intent intent = getActivity().getIntent();
            if (intent != null) {
                mUri = intent.getData();
            }
        }

        mPlaceName = (TextView) rootView.findViewById(R.id.detail_place_name);
        mImage = (ImageView) rootView.findViewById(R.id.detail_image);
        mDescription = (TextView) rootView.findViewById(R.id.detail_place_description);

        return rootView;
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        if (null != mUri) {
            return new CursorLoader(
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
        if (cursor != null && cursor.moveToFirst()) {

            String place_name = cursor.getString(COL_PLACE_NAME);
            String description = cursor.getString(COL_PLACE_DESCRIPTION);
            long coord_long = cursor.getLong(COL_PLACE_COORD_LONG);
            long coord_lat = cursor.getLong(COL_PLACE_COORD_LAT);
            String image_src = cursor.getString(COL_PLACE_IMAGE_SRC);

            mText = "I'm at " + place_name;
            GEO_COORD = "geo:" + Long.toString(coord_lat) + "," + Long.toString(coord_long);

            mPlaceName.setText(place_name);
            mImage.setImageResource(getImageResource(image_src));
            mDescription.setText(description);

            if (mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(createImageShareIntent());
            }
        }

    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

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


}