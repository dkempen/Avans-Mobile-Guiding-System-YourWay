package com.id.yourway.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.id.yourway.R;
import com.id.yourway.activities.DetailActivity;
import com.id.yourway.entities.Sight;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SightListAdapter extends RecyclerView.Adapter<SightListAdapter.SightViewHolder>{
    private final static String TAG = SightListAdapter.class.getSimpleName();


    private List<Sight> sights;
    private Context context;

    public SightListAdapter(Context context, List<Sight> sights) {
        this.context = context;
        this.sights = sights;
    }

    @Override
    public int getItemCount() {
        return sights.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final SightViewHolder viewHolder, int position) {
        Sight sight = sights.get(position);

        if(sight.getTitle() != null)
            viewHolder.title.setText(sight.getTitle().replaceAll("\\r\\n|\\r|\\n", " "));
        else
            viewHolder.title.setText(sight.getAuthor().replaceAll("\\r\\n|\\r|\\n", " "));

        if(sight.getType().equals("Blindwall"))
        {
            tryImages(0, sight, viewHolder);

        } else if(sight.getType().equals("VVV"))
        {
            Drawable dw = viewHolder.thumbnail.getDrawable();
            if(dw instanceof  BitmapDrawable){
                ((BitmapDrawable)dw).getBitmap().recycle();
            }
            String imageUrl = "" + sight.getImages().get(0);
            int resid = context.getResources().getIdentifier(context.getPackageName() + ":drawable/p"+ imageUrl, null,null);
            viewHolder.thumbnail.setImageResource(resid);
        }
    }

    @NonNull
    @Override
    public SightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_sight, parent, false);

        return new SightViewHolder(v, context);
    }

    private void tryImages(int index, Sight sight, SightViewHolder holder) {
        String url = null;

        if(sight.getType().equals("Blindwall")) {
            try {
                url = sight.getImages().get(index);
                //Log.d(TAG, "https://api.blindwalls.gallery/" + sight.getImages().get(index));
            } catch (IndexOutOfBoundsException e) {
                Picasso.get().load(R.drawable.no_image_available).placeholder(R.drawable.placeholder).into(holder.thumbnail);
                Log.d(TAG, "tryImages: no image available for: " + sight.getTitle());
                //return;
            }

            final int finalIndex = index + 1;

            Picasso.get().load(url).placeholder(R.drawable.placeholder).into(holder.thumbnail, new Callback() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "onSuccess: " + sight.getTitle());
                }

                @Override
                public void onError(Exception e) {
                    Log.d(TAG, "onError: " + sight.getTitle());
                    tryImages(finalIndex, sight, holder);
                }
            });
        }
        else{

        }
    }

    public class SightViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView thumbnail;

        public SightViewHolder(View itemView, final Context ctx) {
            super(itemView);

            title = itemView.findViewById(R.id.item_sight_name_id);
            thumbnail = itemView.findViewById(R.id.item_sight_imageView);

            //OnClick listener voor cardview item.
            itemView.setOnClickListener((View v) -> {
                Sight sight = sights.get(getAdapterPosition());
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("SIGHT_OBJECT", sight);
                ctx.startActivity(intent);
            });
        }
    }
}
