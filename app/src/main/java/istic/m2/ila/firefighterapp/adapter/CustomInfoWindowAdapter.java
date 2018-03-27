package istic.m2.ila.firefighterapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import istic.m2.ila.firefighterapp.R;

/**
 * Created by adou on 21/03/18.
 */

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View myContentsView;

    public CustomInfoWindowAdapter(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        myContentsView = inflater.inflate(R.layout.custom_info_contents, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        // TODO - Adapter le contenu
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        // TODO - Adapter le contenu
        TextView tvTitle = ((TextView)myContentsView.findViewById(R.id.custom_info_window_title));
        tvTitle.setText(marker.getTitle());
        TextView tvSnippet = ((TextView)myContentsView.findViewById(R.id.custom_info_window_snippet));
        tvSnippet.setText(marker.getSnippet());

        return myContentsView;
    }
}
