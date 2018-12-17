package com.id.yourway.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.id.yourway.activities.DetailActivity;
import com.id.yourway.adapters.CustomInfoWindowAdapter;
import com.id.yourway.entities.Sight;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import static android.content.Context.LOCATION_SERVICE;

public class MapFragment extends SupportMapFragment implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraMoveListener, LocationListener,
        GoogleMap.OnInfoWindowClickListener {

    private static final String TAG = MapFragment.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_ID = 1;
    public static final List<PatternItem> PATTERN_POLYGON_ALPHA =
            Arrays.asList(new Gap(20), new Dash(20));

    private GoogleMap mMap;
    private List<Sight> sights;
    private Map<Marker, Sight> markerSightMap;
    private Queue<Runnable> runnables;
    private android.location.Location location;
    private Polyline track;

    public MapFragment() {
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

        if (!checkPermission())
            requestPermission();
        else
            mMap.setMyLocationEnabled(true);

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                .target(new LatLng(51.59144918270287, 4.775340557098389))
                .bearing(0)
                .zoom(15)
                .build()));
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(new LatLng(51.58651785207713, 4.708317190123125))
                .include(new LatLng(51.61099091944258, 4.770882942675826))
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
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.color(Color.GRAY);
        polyOptions.width(5);
        polyOptions.pattern(PATTERN_POLYGON_ALPHA);
        track = mMap.addPolyline(polyOptions);

        for (Runnable runnable : runnables)
            runnable.run();
    }

    public void removeMarkers() {
        for (Marker marker : markerSightMap.keySet())
            marker.remove();
    }

    public void addSight(final Sight sight) {
        if (!sights.contains(sight)) {
            sights.add(sight);
            if (mMap == null)
                runnables.add(() -> addSightInternal(sight));
            else
                addSightInternal(sight);

            Log.i(TAG, "addSight: ");
        }
    }

    private void addSightInternal(Sight sight) {
        CustomInfoWindowAdapter customInfoWindow = new CustomInfoWindowAdapter(getContext());
        mMap.setInfoWindowAdapter(customInfoWindow);
        MarkerOptions options = null;

        if (sight.getType().equals("VVV")) {
            options = new MarkerOptions()
                    .position(sight.getLatLng())
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        } else if (sight.getType().equals("Blindwall")) {
            options = new MarkerOptions()
                    .position(sight.getLatLng())
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }

        Marker marker = mMap.addMarker(options);
        marker.setTag(sight);
        marker.showInfoWindow();

        markerSightMap.put(marker, sight);
    }


    public android.location.Location getGps(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

        // getting GPS status
        boolean isGPSEnabled = false;
        if (locationManager != null) {
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }

        if (isGPSEnabled) {
            if (location == null) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        context, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
                    return null;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        0, 0, this, null);
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    Log.i(TAG, "getGps: latitude: " + latitude + " longitude: " + longitude);
                    return location;
                }
            }
        }
        return null;
    }

    public Location getLocation() {
        return location;
    }

    private boolean checkPermission() {
        return ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MapFragment.REQUEST_PERMISSIONS_ID);
    }

    public void drawPolyLineOnMap(List<LatLng> list) {
        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.color(Color.BLUE);
        polyOptions.width(5);
        polyOptions.addAll(list);

        mMap.clear();
        mMap.addPolyline(polyOptions);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : list)
            builder.include(latLng);
    }

    public void drawPolyLineOnMap(LatLng latLng) {
        if (track == null)
            return;
        List<LatLng> points = track.getPoints();
        points.add(latLng);
        track.setPoints(points);
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        drawPolyLineOnMap(new LatLng(location.getLatitude(), location.getLongitude()));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onCameraMove() {}

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