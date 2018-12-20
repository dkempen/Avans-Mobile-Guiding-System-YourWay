package com.id.yourway.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class SightListAdapter extends RecyclerView.Adapter<SightListAdapter.SightViewHolder> {
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
        if (viewHolder.thumbnail != null)
            viewHolder.thumbnail.setImageDrawable(null);

        if (sight.getTitle() != null)
            viewHolder.title.setText(sight.getTitle().
                    replaceAll("\\r\\n|\\r|\\n", " "));
        else
            viewHolder.title.setText(sight.getAuthor().
                    replaceAll("\\r\\n|\\r|\\n", " "));


        if (sight.getType().equals("Blindwall")) {
            tryImages(0, sight, viewHolder);
        } else if (sight.getType().equals("VVV")) {
            String imageUrl = "" + sight.getImages().get(0);
            int resid = context.getResources().getIdentifier(context.getPackageName()
                    + ":drawable/p" + imageUrl, null, null);
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(context.getResources(), resid, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, 100, 100);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), resid, options);
            viewHolder.thumbnail.setImageBitmap(bmp);
        }
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
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

        if (sight.getType().equals("Blindwall")) {
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
    }

    class SightViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView thumbnail;

        SightViewHolder(View itemView, final Context ctx) {
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
