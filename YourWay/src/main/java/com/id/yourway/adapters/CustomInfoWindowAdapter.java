package com.id.yourway.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.id.yourway.R;
import com.id.yourway.entities.Sight;
import com.squareup.picasso.Picasso;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public CustomInfoWindowAdapter(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.popup_maps, null);

        TextView title = view.findViewById(R.id.title);
        TextView author = view.findViewById(R.id.author);
        ImageView img = view.findViewById(R.id.pic);

        title.setText(marker.getTitle());

        Sight sight = (Sight) marker.getTag();
        String imageurl = sight.getImages().get(0);

        Picasso.get().load(imageurl).into(img);

        author.setText(sight.getAuthor());

        return view;
    }
}
