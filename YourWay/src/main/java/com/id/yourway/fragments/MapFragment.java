package com.id.yourway.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.id.yourway.activities.DetailActivity;
import com.id.yourway.activities.MainActivity;
import com.id.yourway.adapters.CustomInfoWindowAdapter;
import com.id.yourway.entities.Sight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import static android.content.Context.LOCATION_SERVICE;


public class MapFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraMoveListener, LocationListener, GoogleMap.OnInfoWindowClickListener {
    private static final String TAG = MapFragment.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_ID = 1;
    private static final int BOUND_PADDING = 100;
    private GoogleMap mMap;
    private List<Sight> sights;
    private Map<Marker, Sight> markerSightMap;
    private Queue<Runnable> runnables;
    private android.location.Location location;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMapAsync(this);
        sights = new ArrayList<>();
        markerSightMap = new HashMap<>();
        runnables = new LinkedBlockingQueue<>();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnInfoWindowClickListener(this);

        if (!checkPermission(Manifest.permission.ACCESS_FINE_LOCATION))
            requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_PERMISSIONS_ID);
        else
            mMap.setMyLocationEnabled(true);

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                .target(new LatLng(51.59144918270287, 4.775340557098389))
                .bearing(0)
                .zoom(15)
                .build()));
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(new LatLng(51.58651785207713, 4.708317190123125))
                .include(new LatLng(51.61099091944258 , 4.770882942675826))
                .include(new LatLng(51.56619630165785, 4.864953377246138))
                .include(new LatLng(51.53379788028693, 4.771831899595782))
                .build();

        mMap.setLatLngBoundsForCameraTarget(bounds);
        mMap.setBuildingsEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setMinZoomPreference(13);
        mMap.setMaxZoomPreference(20);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnCameraMoveListener(this);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
//        UiSettings settings = googleMap.getUiSettings();
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        Iterator<Runnable> iterator = runnables.iterator();
        while (iterator.hasNext()) {
            Runnable runnable = iterator.next();
            runnable.run();
        }
    }

    public void removeMarkers() {
        for(Marker marker : markerSightMap.keySet()) {
            marker.remove();
        }
    }

    public void addSight(final Sight sight) {
        if(!sights.contains(sight)) {
            sights.add(sight);
            if (mMap == null)
                runnables.add(new Runnable() {
                    @Override
                    public void run() {
                        addSightInternal(sight);
                    }
                });
            else {
                addSightInternal(sight);
            }

            Log.i(TAG, "addSight: ");
        }
    }

    private void addSightInternal(Sight sight) {
        CustomInfoWindowAdapter customInfoWindow = new CustomInfoWindowAdapter(getContext());
        mMap.setInfoWindowAdapter(customInfoWindow);

        MarkerOptions options = new MarkerOptions()
                .position(sight.getLatLng())
                .anchor(0.5f, 0.5f);

        Marker marker = mMap.addMarker(options);
        marker.setTag(sight);
        marker.showInfoWindow();

        markerSightMap.put(marker, sight);
    }


    public android.location.Location getGps() {
        LocationManager locationManager = (LocationManager) MainActivity.getInstance().getSystemService(LOCATION_SERVICE);

        // getting GPS status
        boolean isGPSEnabled = false;
        if (locationManager != null) {
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }

        if (isGPSEnabled) {
            if (location == null) {
                if (ActivityCompat.checkSelfPermission(MainActivity.getInstance(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.getInstance(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return null;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this, null);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        Log.i(TAG, "getGps: latitude: " + latitude + " longitude: " + longitude);
                        return location;
                    }
                }
            }
        }
        return null;
    }

    public Location getLocation() {
        return location;
    }

    private boolean checkPermission(String permission) {
        if (ActivityCompat.checkSelfPermission(getContext(), permission) == PackageManager.PERMISSION_GRANTED)
            return true;
        return false;
    }
    private void requestPermission(String permission, int code) {
        requestPermissions(new String[]{permission}, code);
    }

    public void drawPolyLineOnMap(List<LatLng> list) {
        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.color(Color.BLUE);
        polyOptions.width(5);
        polyOptions.addAll(list);

        mMap.clear();
        mMap.addPolyline(polyOptions);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : list) {
            builder.include(latLng);
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onCameraMove() {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Sight sight = (Sight) marker.getTag();
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("SIGHT_OBJECT", sight);
        startActivity(intent);
    }
}
