package com.id.yourway.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.id.yourway.DrawerItem;
import com.id.yourway.R;
import com.id.yourway.entities.Sight;
import com.id.yourway.fragments.HelpFragment;
import com.id.yourway.fragments.MapFragment;
import com.id.yourway.fragments.SightListFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MapFragment.class.getSimpleName();

    private static MainActivity instance;

    public static MainActivity getInstance() {
        return instance;
    }
    private Map<String, Sight> sightMap = new HashMap<>();

    private DrawerLayout drawerLayout;

    private MapFragment mapFragment;
    private HelpFragment helpFragment;
    private SightListFragment sightListFragment;

    List<DrawerItem> dataList;
    private android.support.v4.app.FragmentManager fragmentManager;
    private List<Sight> sights;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();
        updateSights();
        long id =  Thread.currentThread().getId();
        //NavigationDrawer
        dataList = new ArrayList<>();
        instance = this;
        fragmentManager = getSupportFragmentManager();
        mapFragment = new MapFragment();
        sightListFragment = new SightListFragment();
        helpFragment = new HelpFragment();

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.maps_item:
                        fragmentManager.beginTransaction().replace(R.id.fragment,
                                mapFragment).addToBackStack(null).commitAllowingStateLoss();
                        break;

                    case  R.id.routes_item:
                        break;

                    case  R.id.sight_item :
                        fragmentManager.beginTransaction().replace(R.id.fragment,
                                sightListFragment).addToBackStack(null).commitAllowingStateLoss();
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

        fragmentManager.beginTransaction().replace(R.id.fragment, mapFragment).commit();

        try {
            mapFragment.getGps();
            Log.i(TAG, "onCreate: getGps successful");
        } catch (Exception e) {
            Log.e(TAG, "onCreate: ", e);
        }
    }

    private void setupToolbar() {
        Toolbar mToolbar = findViewById(R.id.tool_bar);
        mToolbar.bringToFront();
        ImageButton searchButton = findViewById(R.id.toolBarSearchButton);
        searchButton.setVisibility(View.INVISIBLE);

        setSupportActionBar(mToolbar);

        ImageButton settingsButton = findViewById(R.id.toolBarSettingsButton);
        settingsButton.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, PreferencesActivity.class)));

        ImageButton helpButton = findViewById(R.id.toolBarHelpButton);
        helpButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            HelpFragment helpFragment = new HelpFragment();
            helpFragment.show(ft, "HELP");
        });

        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Map");
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
    }

    public void updateSights() {
        AppContext.getInstance(this).getSightManager().getSights(sights -> {
            mapFragment.removeMarkers();
            sightMap = new HashMap<>();
            for (Sight sight : sights) {
                sightMap.put("" + sight.getId(), sight);
                mapFragment.addSight(sight);
            }
            setSights(sights);
        });
    }

    public void setSights(List<Sight> sightslist) {
        sights = sightslist;
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
