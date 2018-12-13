package com.id.yourway.activities;

import android.app.Fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toolbar;

import com.id.yourway.DrawerItem;
import com.id.yourway.R;
import com.id.yourway.entities.Sight;
import com.id.yourway.fragments.MapFragment;
import com.id.yourway.adapters.CustomDrawerAdapter;
import com.id.yourway.fragments.FragmentLayoutItem;
import com.id.yourway.providers.listeners.SightProviderListener;

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

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    CustomDrawerAdapter adapter;

    List<DrawerItem> dataList;
    private MapFragment mapFragment;
    private android.support.v4.app.FragmentManager fragmentManager;
    private List<Sight> sights;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateSights();
        //NavigationDrawer
        dataList = new ArrayList<DrawerItem>();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        instance = this;
        fragmentManager = getSupportFragmentManager();
        mapFragment = new MapFragment();

        fragmentManager.beginTransaction().replace(R.id.fragment, mapFragment).commit();
        addItems();

        if (savedInstanceState == null) {

            if (dataList.get(0).isSpinner()
                    & dataList.get(1).getTitle() != null) {
                SelectItem(2);
            } else if (dataList.get(0).getTitle() != null) {
                SelectItem(1);
            } else {
                SelectItem(0);
            }
        }

        adapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item,
                dataList);

        try {
            mapFragment.getGps();
            Log.i(TAG, "onCreate: getGps successful");
        } catch (Exception e) {
            Log.e(TAG, "onCreate: ", e);
        }

        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerTitle = getTitle();
        mTitle = mDrawerTitle;

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                null, R.string.drawer_open,
                R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            SelectItem(0);
        }
    }

    public void updateSights() {
        AppContext.getInstance(this).getSightManager().getSights(new SightProviderListener() {
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
        });
    }

    public void setSights(List<Sight> sightslist) {
        sights = sightslist;

    }

    public List<Sight> getSights() {
        return sights;
    }

    private void addItems() {
        dataList.add(new DrawerItem("My Favorites")); // adding a header to the list
        addDrawer("Kaart", R.drawable.maps);
        addDrawer("Routes", R.drawable.routes);
        addDrawer("Bezienswaardigheden", R.drawable.eye);
        addDrawer("Instellingen", R.drawable.settings);
        addDrawer("Help", R.drawable.help);
    }

    private void addDrawer(String title, int ImgID){
        dataList.add(new DrawerItem(title, ImgID));
    }



    public void SelectItem(int possition) {

        Bundle args = new Bundle();
        System.out.println(possition);
        switch (possition) {
            case 1:
                System.out.println("Zucht");
                break;
            case 2:
                //SightDetailFragment sightFragment = new SightDetailFragment();            case 3:
                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                startActivity(intent);
                System.out.println("Zucht");
                break;
            case 4:
                Intent intent2 = new Intent(getApplicationContext(), PreferencesActivity.class);
                startActivity(intent2);
                break;
            case 5:
                //HelpFragment fragment = new HelpFragment();
                System.out.println("Zucht");
                break;
        }

        mDrawerList.setItemChecked(possition, true);
        setTitle(dataList.get(possition).getItemName());
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item))
            return true;
        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            if (dataList.get(position).getTitle() == null) {
                SelectItem(position);
            }
        }
    }
}
