package com.id.yourway.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.id.yourway.R;
import com.id.yourway.entities.Sight;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final static String TAG = CustomInfoWindowAdapter.class.getSimpleName();

    private boolean isRefreshed;
    private Context context;

    public CustomInfoWindowAdapter(Context ctx) {
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity) context).getLayoutInflater()
                .inflate(R.layout.popup_maps, null);
        Sight sight = (Sight) marker.getTag();
        String authorString = sight.getAuthor();
        String addressString = sight.getAddress();
        ImageView imageView = view.findViewById(R.id.imageView3);
        if (sight.getType().equals("Blindwall")) {
            tryImages(0, sight, imageView, marker);
        } else {
            String imageUrl = "" + sight.getImages().get(0);
            int resid = context.getResources().getIdentifier(context.getPackageName()
                    + ":drawable/p" + imageUrl, null, null);
            imageView.setImageResource(resid);
        }

        TextView author = view.findViewById(R.id.author);
        TextView address = view.findViewById(R.id.address);

        author.setText(authorString);
        address.setText(addressString);

        return view;
    }

    private void tryImages(int index, Sight sight, ImageView view, Marker marker) {
        String wronglyFormatedUrl = sight.getImages().get(index);
        int extIndex = wronglyFormatedUrl.lastIndexOf(".");
        String plainUrl = wronglyFormatedUrl.substring(0, extIndex);
        String extension = wronglyFormatedUrl.substring(extIndex).toLowerCase();
        String newUrl = plainUrl+extension;
        Picasso.get().load(newUrl).into(view, new Callback() {
            @Override
            public void onSuccess() {

                if(!isRefreshed) {
                    isRefreshed = true;
                    Log.d(TAG, "onSuccess: " + sight.getTitle());
                    marker.showInfoWindow();
                }
            }

            @Override
            public void onError(Exception e) {

                Log.d(TAG, "onError: " + sight.getTitle());
            }
        });
    }
}
