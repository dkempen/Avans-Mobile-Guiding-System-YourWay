package com.id.yourway.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.id.yourway.R;
import com.id.yourway.activities.listeners.RouteReadyListener;
import com.id.yourway.entities.Route;
import com.id.yourway.entities.Sight;
import com.id.yourway.fragments.HelpFragment;
import com.id.yourway.fragments.IDetailFragment;
import com.id.yourway.fragments.ListFragment;
import com.id.yourway.fragments.MapFragment;
import com.id.yourway.fragments.PreferenceFragment;
import com.id.yourway.providers.listeners.SightProviderListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private RouteReadyListener raListener;
    private static final String TAG = MapFragment.class.getSimpleName();

    private Map<String, Sight> sightMap = new HashMap<>();

    private DrawerLayout drawerLayout;
    private MapFragment mapFragment;
    private HelpFragment helpFragment;
    private ListFragment listFragment;

    private Toolbar mToolbar;
    private ActionBar actionbar;

    private android.support.v4.app.FragmentManager fragmentManager;
    private List<Sight> sights;
    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();
        updateSights();


        //NavigationDrawer
        fragmentManager = getSupportFragmentManager();
        listFragment = new ListFragment();
        mapFragment = new MapFragment();
        helpFragment = new HelpFragment();
        drawerLayout = findViewById(R.id.drawer_layout);

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean firstStart = preferences.getBoolean("firstStart", true);

        if (firstStart) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            HelpFragment helpFragment = new HelpFragment();
            helpFragment.show(ft, "HELP");

            SharedPreferences preferences1 = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences1.edit();
            editor.putBoolean("firstStart", false);
        }

        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.maps_item:
                    fragmentManager.beginTransaction().replace(R.id.fragment,
                            mapFragment).addToBackStack(null).commitAllowingStateLoss();
                    break;
                case R.id.routes_item: {
                    listFragment = new ListFragment();
                    Bundle args = new Bundle();
                    args.putInt(IDetailFragment.FRAGMENT_TYPE, IDetailFragment.FRAG_LIST_ROUTES);
                    listFragment.setArguments(args);
                    fragmentManager.beginTransaction().replace(R.id.fragment,
                            listFragment).addToBackStack(null).commitAllowingStateLoss();
                }
                break;

                case R.id.sight_item: {
                    listFragment = new ListFragment();
                    Bundle args = new Bundle();
                    args.putInt(IDetailFragment.FRAGMENT_TYPE, IDetailFragment.FRAG_LIST_SIGHT);
                    listFragment.setArguments(args);
                    fragmentManager.beginTransaction().replace(R.id.fragment,
                            listFragment).addToBackStack(null).commitAllowingStateLoss();
                }
                break;
                case R.id.settings_item:
                    PreferenceFragment preferenceFragment = new PreferenceFragment();
                    fragmentManager.beginTransaction().replace(R.id.fragment, preferenceFragment).addToBackStack(null).commitAllowingStateLoss();
                    break;
                case R.id.help_item:
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    HelpFragment helpFragment = new HelpFragment();
                    helpFragment.show(ft, "HELP");
                    break;
            }
            if (item.getGroupId() == R.id.top_group_drawer)
                drawerLayout.closeDrawers();
            return true;
        });

        fragmentManager.beginTransaction().replace(R.id.fragment, mapFragment).commit();

        try {
            mapFragment.getGps(this);
            Log.i(TAG, "onCreate: getGps successful");
        } catch (Exception e) {
            Log.e(TAG, "onCreate: ", e);
            AppContext.getInstance(this).getFeedbackManager().onGPSLost(this);
        }
    }

    public void setRouteReadyListener(RouteReadyListener raListener) {
        this.raListener = raListener;
    }


    public void setRouteAndSwitchToHome(Route route) {
        fragmentManager.beginTransaction().replace(R.id.fragment,
                mapFragment).addToBackStack(null).commitAllowingStateLoss();
        if (raListener != null) {
            raListener.RouteReady(route);
        }
        //this.route = route;
        mapFragment.removeMarkers();
        if (mapFragment.getRoute() != null) {
            mapFragment.deleteRoute();
            mapFragment.deletePolyLinesOnMap();
        }
        List<Sight> sights = route.getSights();
        for (Sight sight : sights) {
            mapFragment.addSightInternal(sight);
        }
        mapFragment.setRoute(route);
        Log.e("hello", "hello");
    }

    private void setupToolbar() {
        mToolbar = findViewById(R.id.tool_bar);
        mToolbar.bringToFront();
        ImageButton searchButton = findViewById(R.id.toolBarSearchButton);
        searchButton.setVisibility(View.INVISIBLE);
        toolbarTitle = findViewById(R.id.toolBarTextView);

        setSupportActionBar(mToolbar);

        ImageButton settingsButton = findViewById(R.id.toolBarSettingsButton);
        settingsButton.setOnClickListener(v ->
                openPreferenceFragment());

        ImageButton helpButton = findViewById(R.id.toolBarHelpButton);
        helpButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            HelpFragment helpFragment = new HelpFragment();
            helpFragment.show(ft, "HELP");
        });

        actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Your Way");
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
    }

    private void openPreferenceFragment() {
        PreferenceFragment preferenceFragment = new PreferenceFragment();
        fragmentManager.beginTransaction().replace(R.id.fragment, preferenceFragment).addToBackStack(null).commitAllowingStateLoss();
    }

    public void updateSights() {
        AppContext.getInstance(this)
                .getSightManager()
                .getSights(new SightProviderListener() {
                    @Override
                    public void onSightsAvailable(List<Sight> sights) {
                        mapFragment.removeMarkers();
                        sightMap = new HashMap<>();
                        for (Sight sight : sights) {
                            sightMap.put("" + sight.getId(), sight);
                            mapFragment.addSight(sight);
                        }
                        setSights(sights);
                    }

                    @Override
                    public void onError(VolleyError error) {
                        AppContext.getInstance(getBaseContext())
                                .getFeedbackManager()
                                .onError(getBaseContext(), String.valueOf(error));
                    }
                });
    }

    public void setSights(List<Sight> sightslist) {
        sights = sightslist;
    }

    public List<Sight> getSights() {
        return sights;
    }

    public void setToolbarTitle(String title) {
        if (toolbarTitle == null)
            return;
        toolbarTitle.setText(title);
    }

    public void setToolbarSettingsEnabled(boolean settingsEnabled) {
        ImageButton settingsButton = findViewById(R.id.toolBarSettingsButton);
        settingsButton.setVisibility(settingsEnabled ? View.VISIBLE : View.INVISIBLE);
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
