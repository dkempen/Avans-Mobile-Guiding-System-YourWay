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
        Sight sight = (Sight) marker.getTag();
        String authorString = sight.getAuthor();
        String addressString = sight.getAddress();
        ImageView imageView = view.findViewById(R.id.imageView3);
        if(sight.getType() == "Blindwall")
        {
            Picasso.get()
                    .load(sight.getImages().get(0))
                    .into(imageView);
        } else if(sight.getType() == "vvv")
        {
            int resid = context.getResources().getIdentifier(context.getPackageName() + ":drawable/p", sight.getImages().get(0), null);
            imageView.setImageResource(resid);
        }

        TextView author = view.findViewById(R.id.author);
        TextView address = view.findViewById(R.id.address);

        author.setText(authorString);
        address.setText(addressString);

        return view;
    }
}