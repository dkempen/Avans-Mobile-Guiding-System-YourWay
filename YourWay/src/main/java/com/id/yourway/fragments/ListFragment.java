package com.id.yourway.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.id.yourway.R;
import com.id.yourway.activities.AppContext;
import com.id.yourway.adapters.RouteListAdapter;
import com.id.yourway.adapters.SightListAdapter;
import com.id.yourway.entities.Route;
import com.id.yourway.entities.Sight;
import com.id.yourway.providers.listeners.SightProviderListener;

import java.util.List;

import static android.support.constraint.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 *
 */


public class ListFragment extends Fragment implements IDetailFragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_list, null, false);


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle arguments = getArguments();
        int fragmentType = arguments.getInt("FRAGMENT_TYPE");
        if(fragmentType == IDetailFragment.FRAG_LIST_SIGHT) {
            AppContext.getInstance(getContext()).getSightManager().getSights(
                    sights -> createAndBindAdapterForSights(getView(), sights )
            );
        }
        else{
            AppContext.getInstance(getContext()).getRouteManager().getRoutes(
                    routes-> {createAndBindAdapterForRoutes(getView(), routes);}
            );

        }
    }

    public void createAndBindAdapterForSights(View view, List<Sight> sights) {
        mRecyclerView = view.findViewById(R.id.list_recyclerview_id);
        mRecyclerView.setHasFixedSize(true);


        //specify an adapter
        SightListAdapter adapter = new SightListAdapter(getContext(), sights);
        mRecyclerView.setAdapter(adapter);

        //linear layout
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    public void createAndBindAdapterForRoutes(View view, List<Route> routes){
        mRecyclerView = view.findViewById(R.id.list_recyclerview_id);
        mRecyclerView.setHasFixedSize(true);
        RouteListAdapter adapter = new RouteListAdapter(getContext(), routes);
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        //linear layout
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

}
