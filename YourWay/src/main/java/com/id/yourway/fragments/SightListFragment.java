package com.id.yourway.fragments;

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
import com.id.yourway.adapters.SightListAdapter;
import com.id.yourway.entities.Sight;
import com.id.yourway.providers.listeners.SightProviderListener;

import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class SightListFragment extends Fragment implements IDetailFragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SightListAdapter mAdapter;
    private List<Sight> sights;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_list, null, false);
        AppContext.getInstance(getContext()).getSightManager().getSights(sights -> {
            setSights(sights);
            if(getSights() != null)
                createRecyclerView(view);
        });

        Log.d(TAG, "onCreateView: ");
        return view;
    }

    public void createRecyclerView(View view) {
        mRecyclerView = view.findViewById(R.id.list_recyclerview_id);
        mRecyclerView.setHasFixedSize(true);

        //specify an adapter
        mAdapter = new SightListAdapter(getContext(), sights);
        mRecyclerView.setAdapter(mAdapter);

        //linear layout
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    public void setSights(List<Sight> sights) {
        this.sights = sights;
    }

    public List<Sight> getSights() {
        return sights;
    }
}
