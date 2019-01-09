package com.id.yourway.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

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
import com.id.yourway.R;
import com.id.yourway.activities.AppContext;
import com.id.yourway.activities.DetailActivity;
import com.id.yourway.activities.MainActivity;
import com.id.yourway.activities.listeners.RouteReadyListener;
import com.id.yourway.adapters.CustomInfoWindowAdapter;
import com.id.yourway.business.RouteManager;
import com.id.yourway.business.listeners.DirectionsListener;
import com.id.yourway.entities.Route;
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
        GoogleMap.OnMarkerClickListener, LocationListener,
        GoogleMap.OnInfoWindowClickListener, RouteReadyListener,
        GoogleMap.OnCameraIdleListener {

    private static final String TAG = MapFragment.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_ID = 1;
    public static final List<PatternItem> PATTERN_POLYGON_ALPHA =
            Arrays.asList(new Gap(20), new Dash(20));

    private GoogleMap mMap;
    private LocationManager locationManager;
    private List<Sight> sights;
    private Polyline polyline;
    private Map<Marker, Sight> markerSightMap;
    private Queue<Runnable> runnables;
    private android.location.Location location;
    private Polyline track;

    private Route route;
    private Route lastRoute;
    private int currentRouteIndex;
    private Map.Entry<Marker, Sight> nextSight;
    private static final int SIGHT_TRIGGER_RADIUS = 20;

    private CardView arrow;
    private float lastCameraBearing;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMapAsync(this);
        sights = new ArrayList<>();
        markerSightMap = new HashMap<>();
        runnables = new LinkedBlockingQueue<>();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View mainView = super.onCreateView(layoutInflater, viewGroup, bundle);
        if (arrow != null)
            return mainView;
        View addedView = layoutInflater.inflate(R.layout.fragment_map, viewGroup, false);

        FrameLayout mainChild = (FrameLayout) ((ViewGroup) mainView).getChildAt(0);
        mainChild.addView(addedView);
        arrow = addedView.findViewById(R.id.mapArrowCardView);

        return mainView;
    }

    @Override
    public void onStart() {
        super.onStart();
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setToolbarTitle("Your Way");
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
        mMap.setOnCameraIdleListener(this);
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
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setRouteReadyListener(this);
    }

    public void removeMarkers() {
        for (Marker marker : markerSightMap.keySet())
            marker.remove();
        markerSightMap.clear();
    }

    public void resetPolyLineWithNewLocation(){
        track.remove();
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

    public void addSightInternal(Sight sight) {
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
        markerSightMap.put(marker, sight);
    }

    public void setRoute(Route route) {
        this.route = route;
        RouteManager routeManager = AppContext.getInstance(getContext()).getRouteManager();
        currentRouteIndex = routeManager.getRouteProgression(route.getName());
        nextSight = getMapEntry(route.getSight(currentRouteIndex));
        setMarkerColors();
    }

    private void setMarkerColors() {
        for (int i = 0; i < currentRouteIndex; i++) {
            Marker marker = getMapEntry(route.getSight(i)).getKey();
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        }
        nextSight.getKey().setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
    }

    public Route getRoute() {
        return route;
    }

    public void deleteRoute() {
        this.route = null;
    }

    public Map.Entry<Marker, Sight> getMapEntry(Sight sight) {
        for (Map.Entry<Marker, Sight> entry : markerSightMap.entrySet())
            if (entry.getValue().equals(sight))
                return entry;
        return null;
    }

    public android.location.Location getGps(Context context) {
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

        // getting GPS status
        boolean isGPSEnabled = false;
        if (locationManager != null)
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

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
        polyline = mMap.addPolyline(polyOptions);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : list)
            builder.include(latLng);
    }

    public void deletePolyLinesOnMap() {
        if (polyline != null) {
            polyline.remove();
            track.setPoints(new ArrayList<>());
        }
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
        checkForNearSight(location);
        updateDirection();
    }

    private void checkForNearSight(Location location) {
        if (nextSight == null)
            return;

        Marker marker = nextSight.getKey();
        float[] results = new float[1];
        Location.distanceBetween(location.getLatitude(), location.getLongitude(),
                marker.getPosition().latitude, marker.getPosition().longitude, results);
        int metersToSight = (int) results[0];
        if (metersToSight > SIGHT_TRIGGER_RADIUS)
            return;

        // Sight is discovered
        onSightDiscovered(marker);
    }

    private void onSightDiscovered(Marker marker) {
        marker.showInfoWindow();
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
        AppContext.getInstance(getContext()).getFeedbackManager().onNearbySight(getContext());

        currentRouteIndex++;
        AppContext.getInstance(getContext()).getRouteManager().
                storeRouteProgression(route.getName(), currentRouteIndex);

        if (route.getSights().size() == currentRouteIndex) {
            routeIsFinished();
            return;
        }

        nextSight = getMapEntry(route.getSight(currentRouteIndex));
        nextSight.getKey().setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
    }

    private void routeIsFinished() {
        route = null;
        nextSight = null;
        currentRouteIndex = 0;

        AppContext.getInstance(getContext()).getFeedbackManager().onRouteFinished(getContext());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
        List<Sight> sights = lastRoute.getSights();
        for (Sight sight : sights) {
            addSightInternal(sight);
        }
        setRoute(lastRoute);
        RouteReady(route);
    }

    @Override
    public void onProviderDisabled(String provider) {
        AppContext.getInstance(getContext()).getFeedbackManager().onGPSLost(getContext());
        lastRoute = route;
        deletePolyLinesOnMap();
        removeMarkers();
        deleteRoute();
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

    @Override
    public void RouteReady(Route route) {
        if (checkPermission()) {
            if (location == null) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        0, 0, this, null);
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
            AppContext.getInstance(getContext()).getRouteManager().getDirections(new LatLng(location.getLatitude(), location.getLongitude()), route, new DirectionsListener() {
                @Override
                public void onReceivedDirections(List<LatLng> directionList) {
                    drawPolyLineOnMap(directionList);
                }

                @Override
                public void onError(Error error) {
                    Log.e("FUCKM", "oh no");
                }
            });
        } else {
            requestPermission();
            Log.e("FUCKM", "oh not this");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS_ID) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                AppContext.getInstance(getContext())
                        .getFeedbackManager()
                        .onError(getContext(), String.valueOf("GPS not enabled!"));
            }
        }
    }

    private void updateDirection() {
        if (nextSight == null) {
            arrow.setVisibility(View.INVISIBLE);
            return;
        } else if (arrow.getVisibility() == View.INVISIBLE)
            arrow.setVisibility(View.VISIBLE);

        Sight sight = nextSight.getValue();
        if (location == null || sight == null)
            return;
        Location sightLocation = new Location("");
        sightLocation.setLatitude(sight.getLatitude());
        sightLocation.setLongitude(sight.getLongitude());

        float bearing = location.bearingTo(sightLocation) - mMap.getCameraPosition().bearing;
        arrow.setRotation(bearing);
    }

    @Override
    public void onCameraIdle() {
        float bearing = mMap.getCameraPosition().bearing;
        if (areEqualFloat(bearing, lastCameraBearing))
            return;
        lastCameraBearing = bearing;
        updateDirection();
    }

    private boolean areEqualFloat(float a, float b) {
        return (Math.abs((a / b) - 1.0) < 0.001);
    }
}
