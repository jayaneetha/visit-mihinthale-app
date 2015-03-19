package net.brightron.jayaneetha.visitmihinthale;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Admin on 3/12/15.
 */
public class PlacesAdapter extends CursorAdapter {
    public PlacesAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_main, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        String placeName = cursor.getString(FragmentMain.COL_PLACE_NAME);
        viewHolder.textView.setText(placeName);
    }

    public static class ViewHolder {
        public final TextView textView;

        public ViewHolder(View view) {
            this.textView = (TextView) view.findViewById(R.id.list_item_textview);
        }
    }
}
