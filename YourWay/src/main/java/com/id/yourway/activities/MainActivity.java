package com.id.yourway.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.id.yourway.DrawerItem;
import com.id.yourway.R;
import com.id.yourway.adapters.CustomDrawerAdapter;
import com.id.yourway.fragments.FragmentLayoutItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {


    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    CustomDrawerAdapter adapter;

    List<DrawerItem> dataList;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //NavigationDrawer
        dataList = new ArrayList<DrawerItem>();
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        addItems();

        adapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item,
                dataList);

        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                null, R.string.drawer_open,
                R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                //getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                //getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            SelectItem(0);
        }
    }

    private void addItems() {
        dataList.add(new DrawerItem("Kaart", R.drawable.ic_launcher_foreground));
        dataList.add(new DrawerItem("Routes", R.drawable.ic_launcher_foreground));
        dataList.add(new DrawerItem("Bezienswaardigheden", R.drawable.ic_launcher_foreground));
        dataList.add(new DrawerItem("Ïnstellingen", R.drawable.ic_launcher_foreground));
        dataList.add(new DrawerItem("Help", R.drawable.ic_launcher_foreground));
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void SelectItem(int possition) {

        Fragment fragment = null;
        Bundle args = new Bundle();
        switch (possition) {
            case 0:
                fragment = new FragmentLayoutItem();
                args.putString(FragmentLayoutItem.ITEM_NAME, dataList.get(possition)
                        .getItemName());
                args.putInt(FragmentLayoutItem.IMAGE_RESOURCE_ID, dataList.get(possition)
                        .getImgResID());
                break;
            case 1:
                fragment = new FragmentLayoutItem();
                args.putString(FragmentLayoutItem.ITEM_NAME, dataList.get(possition)
                        .getItemName());
                args.putInt(FragmentLayoutItem.IMAGE_RESOURCE_ID, dataList.get(possition)
                        .getImgResID());
                break;
            case 2:
                fragment = new FragmentLayoutItem();
                args.putString(FragmentLayoutItem.ITEM_NAME, dataList.get(possition)
                        .getItemName());
                args.putInt(FragmentLayoutItem.IMAGE_RESOURCE_ID, dataList.get(possition)
                        .getImgResID());
                break;
            case 3:
                fragment = new FragmentLayoutItem();
                args.putString(FragmentLayoutItem.ITEM_NAME, dataList.get(possition)
                        .getItemName());
                args.putInt(FragmentLayoutItem.IMAGE_RESOURCE_ID, dataList.get(possition)
                        .getImgResID());
                break;
            case 4:
                fragment = new FragmentLayoutItem();
                args.putString(FragmentLayoutItem.ITEM_NAME, dataList.get(possition)
                        .getItemName());
                args.putInt(FragmentLayoutItem.IMAGE_RESOURCE_ID, dataList.get(possition)
                        .getImgResID());
                break;
        }
        fragment.setArguments(args);
        FragmentManager frgManager = getFragmentManager();
        frgManager.beginTransaction().replace(R.id.content_frame, fragment)
                .commit();

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
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            SelectItem(position);



        }
    }
}

