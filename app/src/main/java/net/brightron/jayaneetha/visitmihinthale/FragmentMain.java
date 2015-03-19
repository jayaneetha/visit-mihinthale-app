package net.brightron.jayaneetha.visitmihinthale;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import net.brightron.jayaneetha.visitmihinthale.database.PlacesContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentMain extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    static final int COL_PLACE_ID = 0;
    static final int COL_PLACE_NAME = 1;
    static final int COL_PLACE_COORD_LAT = 2;
    static final int COL_PLACE_COORD_LONG = 3;
    static final int COL_PLACE_DESCRIPTION = 4;
    static final int COL_PLACE_IMAGE_SRC = 5;
    private static final int LOADER = 0;
    private static final String[] PLACES_COLUMNS = {
            PlacesContract.PlacesEntry.TABLE_NAME + "." + PlacesContract.PlacesEntry._ID,
            PlacesContract.PlacesEntry.COLUMN_PLACE_NAME,
            PlacesContract.PlacesEntry.COLUMN_COORD_LAT,
            PlacesContract.PlacesEntry.COLUMN_COORD_LONG,
            PlacesContract.PlacesEntry.COLUMN_DESCRIPTION,
            PlacesContract.PlacesEntry.COLUMN_IMAGE_SRC
    };
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    //ArrayAdapter<String> mArrayAdapter;
    PlacesAdapter mPlacesAdapter;

    public FragmentMain() {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Uri placesUri = PlacesContract.PlacesEntry.CONTENT_URI;
        final Cursor cursor = getActivity().getContentResolver().query(placesUri, null, null, null, null);
        mPlacesAdapter = new PlacesAdapter(getActivity(), cursor, 0);
        /*
        String[] data = {
                "Mihinthale",
                "Kaludiya Pokuna",
                "Mihinthale Town",
                "Katu Seya",
                "Kantaka Chaythya"
        };
        List<String> dataList =new ArrayList<String>(Arrays.asList(data));
        mArrayAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_main,
                R.id.list_item_textview,
                dataList);*/

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_main);
        listView.setAdapter(mPlacesAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor itemCursor = (Cursor) adapterView.getItemAtPosition(position);
                if (itemCursor != null) {
                    ((Callback) getActivity()).onItemSelected(PlacesContract.PlacesEntry.buildPlaceUri(itemCursor.getLong(COL_PLACE_ID)));
                    /*Intent intent = new Intent(getActivity(),DetailActivity.class)
                            .setData(PlacesContract.PlacesEntry.buildPlaceUri(itemCursor.getLong(COL_PLACE_ID)));
                    startActivity(intent);*/
                }
            }
        });

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(
                getActivity(),
                PlacesContract.PlacesEntry.CONTENT_URI,
                new String[]{PlacesContract.PlacesEntry.COLUMN_PLACE_NAME, PlacesContract.PlacesEntry._ID},
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mPlacesAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mPlacesAdapter.swapCursor(null);
    }

    public interface Callback {
        public void onItemSelected(Uri uri);
    }
}