package net.brightron.jayaneetha.visitmihinthale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class tourPathFragment extends Fragment {

    public tourPathFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_tour_path, container, false);

        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("item1");
        spinnerArray.add("item2");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = (Spinner) rootView.findViewById(R.id.spinner);
        sItems.setAdapter(adapter);

        Button btn = (Button) rootView.findViewById(R.id.btnTest);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayAdapter<String> mMainListAdapter;
                String list_main_data[] = {
                        "Tour Paths",
                        "Lodginig & Dining",
                        "Governmental Organizations"
                };

                List<String> list_main = new ArrayList<String>(Arrays.asList(list_main_data));
                mMainListAdapter = new ArrayAdapter<String>(
                        getActivity(),
                        R.layout.fragment_tour_path,
                        R.id.list_item_main_textview,
                        list_main);

                ListView main_listview = (ListView) rootView.findViewById(R.id.tour_path_listview);
                main_listview.setAdapter(mMainListAdapter);
            }
        });

        return rootView;
    }
}
