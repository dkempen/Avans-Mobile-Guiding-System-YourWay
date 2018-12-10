package com.id.yourway.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.id.yourway.R;
import com.id.yourway.adapters.SightListAdapter;
import com.id.yourway.entities.Sight;
import com.id.yourway.providers.listeners.SightProviderListener;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SightListAdapter mAdapter;
    private List<Sight> sights;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        AppContext.getInstance(this).getSightManager().getSights(new SightProviderListener() {
            @Override
            public void onSightsAvailable(List<Sight> sights) {
                setSights(sights);
                if(getSights() != null)
                    createRecyclerView();
            }
        });
    }

    public void createRecyclerView()
    {
        mRecyclerView = (RecyclerView) findViewById(R.id.list_recyclerview_id);
        mRecyclerView.setHasFixedSize(true);


        //specify an adapter
        mAdapter = new SightListAdapter(this, sights);
        mRecyclerView.setAdapter(mAdapter);

        //linear layout
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    public void setSights(List<Sight> sights) {
        this.sights = sights;
    }

    public List<Sight> getSights() {
        return sights;
    }
}
