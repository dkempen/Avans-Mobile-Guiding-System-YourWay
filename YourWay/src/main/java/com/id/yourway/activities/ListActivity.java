package com.id.yourway.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.id.yourway.R;
import com.id.yourway.adapters.SightListAdapter;
import com.id.yourway.entities.Sight;
import com.id.yourway.fragments.HelpFragment;
import com.id.yourway.providers.listeners.SightProviderListener;

import java.util.List;

public class ListActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SightListAdapter mAdapter;
    private List<Sight> sights;
    private HelpFragment helpFragment;
    private android.support.v4.app.FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        fragmentManager = getSupportFragmentManager();
        helpFragment = new HelpFragment();


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.maps_item:
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        break;
                    case  R.id.routes_item:
                        break;
                    case  R.id.sight_item :
                        Intent intent1 = new Intent(getApplicationContext(), ListActivity.class);
                        startActivity(intent1);
                        break;
                    case  R.id.settings_item :
                        Intent intent2 = new Intent(getApplicationContext(), PreferencesActivity.class);
                        startActivity(intent2);
                        break;
                    case  R.id.help_item :
                        fragmentManager.beginTransaction().replace(R.id.fragment,
                                helpFragment).addToBackStack(null).commitAllowingStateLoss();
                        break;
                }
                if (item.getGroupId() == R.id.top_group_drawer)
                    drawerLayout.closeDrawers();

                return true;
            }
        });

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

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(menuItem);
    }
}
