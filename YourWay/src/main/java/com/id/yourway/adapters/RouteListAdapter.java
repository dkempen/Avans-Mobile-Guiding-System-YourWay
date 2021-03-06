package com.id.yourway.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

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
    private RouteManager routeManager;

    public RouteListAdapter(Context context, List<Route> routes, MainActivity activity) {
        this.routes = routes;
        this.activity = activity;
        this.context = context;
        routeManager = AppContext.getInstance(context).getRouteManager();
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
        routeViewHolder.progressBar.setMax(currentRoute.getSights().size());
        routeViewHolder.progressBar.setProgress(routeManager.getRouteProgression(currentRoute.getName()));
    }


    @Override
    public int getItemCount() {
        return routes.size();
    }

    class RouteViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, lengthKmTextView, numberOfPOIS;
        ImageButton reset;
        ProgressBar progressBar;

        RouteViewHolder(@NonNull View itemView, MainActivity activity) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.item_route_name);
            lengthKmTextView = itemView.findViewById(R.id.item_route_length_km);
            numberOfPOIS = itemView.findViewById(R.id.item_num_of_pois);
            reset = itemView.findViewById(R.id.reset_route);
            progressBar = itemView.findViewById(R.id.progressBar2);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                activity.setRoute(routes.get(position), true);
            });
            reset.setOnClickListener(v ->{
                RouteManager routeManager = AppContext.getInstance(context).getRouteManager();
                int position = getAdapterPosition();
                routeManager.storeRouteProgression(routes.get(position).getName(), 0);
                progressBar.setProgress(0);

                Route activeRoute = activity.getMapFragment().getRoute();
                if(activeRoute != null && activeRoute.getName().equals(routes.get(position).getName()))
                    activity.setRouteProgress(routes.get(position));

                AppContext.getInstance(activity.getApplicationContext()).getFeedbackManager()
                        .onRouteReset(activity.getApplicationContext());
            });
        }
    }
}
