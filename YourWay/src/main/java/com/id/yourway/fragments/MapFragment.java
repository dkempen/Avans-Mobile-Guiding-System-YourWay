package com.id.yourway.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.id.yourway.R;
import com.id.yourway.activities.MainActivity;
import com.id.yourway.entities.Sight;
import com.id.yourway.providers.MovieCastDirectionsProvider;
import com.id.yourway.providers.interfaces.DirectionsProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import static android.content.Context.LOCATION_SERVICE;
import static java.security.AccessController.checkPermission;


public class MapFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraMoveListener, LocationListener {
    private static final String TAG = MapFragment.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_ID = 1;
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

        if (!checkPermission(Manifest.permission.ACCESS_FINE_LOCATION))
            requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_PERMISSIONS_ID);
        else
            mMap.setMyLocationEnabled(true);

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                .target(new LatLng(51.5719149, 4.768323000000009))
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

    public void addRide(final Sight sight) {
        if(!sights.contains(sight)) {
            sights.add(sight);
            if (mMap == null)
                runnables.add(() -> addRideInternal(sight));
            else {
                addRideInternal(sight);
            }

            Log.i(TAG, "addRide: ");
        }
    }

    private void addRideInternal(Sight sight) {
        Bitmap bitmap = createBitMap(sight);
        Marker marker = mMap.addMarker(new MarkerOptions().position(sight.getLatLng())
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                .anchor(0.5f, 0.5f));
        markerSightMap.put(marker, sight);
    }

    private Bitmap createBitMap(Sight sight){
        Paint marketPaint = new Paint();
        marketPaint.setColor(Color.RED);

        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);
//        textPaint.setTextSize(40);

        int px = getResources().getDisplayMetrics().widthPixels / 12;
//        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());

        Bitmap bitmap = Bitmap.createBitmap(px, px, Bitmap.Config.ARGB_8888);
        bitmap.prepareToDraw();
        Canvas canvas = new Canvas(bitmap);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, marketPaint);

        int xPos = (canvas.getWidth() / 2);
        int yPos = (int) ((canvas.getHeight() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2)) ;
        return bitmap;
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
                    // T0D0: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
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
}
