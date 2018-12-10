package com.id.yourway.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.id.yourway.R;
import com.id.yourway.activities.DetailActivity;
import com.id.yourway.entities.Sight;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SightListAdapter extends RecyclerView.Adapter<SightListAdapter.SightViewHolder>{

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
        viewHolder.nameTextView.setText(sight.getAuthor());
        String imageUrl = sight.getImages().get(0);
        Picasso.get().load(imageUrl).into(viewHolder.imageView);
    }

    @NonNull
    @Override
    public SightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_sight, parent, false);

        return new SightViewHolder(v, context);
    }

    public class SightViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        ImageView imageView;

        public SightViewHolder(View itemView, final Context ctx) {
            super(itemView);
            context = ctx;

            nameTextView = itemView.findViewById(R.id.item_sight_name_id);
            imageView = itemView.findViewById(R.id.item_sight_imageView);

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
