package com.id.yourway.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.id.yourway.R;
import com.id.yourway.adapters.SightListAdapter;
import com.id.yourway.entities.Sight;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SightListAdapter mAdapter;
    List<Sight> sights;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.list_recyclerview_id);
        mRecyclerView.setHasFixedSize(true);

//        List<Sight> sights = new ArrayList<>();
//        List<String> images = new ArrayList<>();
//        Sight sight = new Sight(1,1,100,100,1,100.0,100.0,"joejoe", 1,"jaja","100",
//                "haha", "ja", "ja","jaa","joejoe", "joejoe", "jaa", "ja", "ja",
//                "ja", "ja", "ja", images);
//        sights.add(sight);

        //specify an adapter
        mAdapter = new SightListAdapter(this, sights);
        mRecyclerView.setAdapter(mAdapter);

        //linear layout
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }
}
