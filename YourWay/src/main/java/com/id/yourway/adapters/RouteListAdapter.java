package com.id.yourway.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.id.yourway.R;
import com.id.yourway.activities.AppContext;
import com.id.yourway.activities.MainActivity;
import com.id.yourway.business.RouteManager;
import com.id.yourway.entities.Route;

import java.util.List;

public class RouteListAdapter extends RecyclerView.Adapter<RouteListAdapter.RouteViewHolder> {

    private List<Route> routes;
    private MainActivity activity;
    private Context context;

    public RouteListAdapter(Context context, List<Route> routes, MainActivity activity) {
        this.routes = routes;
        this.activity = activity;
        this.context = context;
    }

    @NonNull
    @Override
    public RouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        Route currentRoute = routes.get(i);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_route, parent, false);
        return new RouteViewHolder(view, activity);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteViewHolder routeViewHolder, int i) {
        Route currentRoute = routes.get(i);
        routeViewHolder.nameTextView.setText(currentRoute.getName());
        routeViewHolder.lengthKmTextView.setText(Double.toString(currentRoute.getLengthInKm()) + " km");
        routeViewHolder.numberOfPOIS.setText(Integer.toString(currentRoute.getSights().size()) + " POI's");
    }


    @Override
    public int getItemCount() {
        return routes.size();
    }

    class RouteViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, lengthKmTextView, numberOfPOIS;
        ImageButton reset;

        RouteViewHolder(@NonNull View itemView, MainActivity activity) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.item_route_name);
            lengthKmTextView = itemView.findViewById(R.id.item_route_length_km);
            numberOfPOIS = itemView.findViewById(R.id.item_num_of_pois);
            reset = itemView.findViewById(R.id.reset_route);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                activity.setRouteAndSwitchToHome(routes.get(position));

                //do things
            });
            reset.setOnClickListener(v ->{
                RouteManager routeManager = AppContext.getInstance(context).getRouteManager();
                int position = getAdapterPosition();
                routeManager.storeRouteProgression(routes.get(position).getName(), 0);
                Toast.makeText(context, "Reset Ok!", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
