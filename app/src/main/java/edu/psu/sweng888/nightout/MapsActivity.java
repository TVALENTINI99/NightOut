package edu.psu.sweng888.nightout;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import edu.psu.sweng888.nightout.maps.GetNearbyPlaces;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, PlaceSelectionListener, GoogleMap.OnInfoWindowClickListener {

    private static final String TAG = "MapsActivity";
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private static final int PROXIMITY_RADIUS = 2000;
    private static final String PLACES_API_KEY = "AIzaSyCY8bnxF3qpnrzoT-vnErkba0soGT0Jy8w";

    private boolean mLocationPermissionGranted;
    private GoogleMap mMap;
    private double latitude, longitude;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;

    private Marker currentLocationMarker;
    private SupportPlaceAutocompleteFragment mAutoCompleteFragment;

    private List<Marker> mMarkers = new ArrayList<Marker>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        mAutoCompleteFragment = (SupportPlaceAutocompleteFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_autocomplete_search);
        mAutoCompleteFragment.setOnPlaceSelectedListener(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Subscribe to Location updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(30000); // 30 second interval
        mLocationRequest.setFastestInterval(5000); // 5 seconds
        mLocationRequest.setSmallestDisplacement(50); // 50 meters
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    onLocationChanged(location);
                }
            }

            ;
        };

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        mMap.setOnInfoWindowClickListener(this);

        // Prompt the user for location permissions
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        displayCurrentLocation();

        startLocationUpdates();
    }

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null /* Looper */);
        } else {
            getLocationPermission();
        }
    }

    private void stopLocationUpdates() {
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void displayCurrentLocation() {

        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            Location location = (Location) task.getResult();
                            onLocationChanged(location);
                        } else {
                            Log.e(TAG, "Exception: %s", task.getException());
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        if (currentLocationMarker != null) {
            currentLocationMarker.remove();
        }

        LatLng currentLocation = new LatLng(latitude, longitude);
        currentLocationMarker = mMap.addMarker(new MarkerOptions().position(currentLocation).title("My Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    public void onNearbyRestaurantsClicked(View v) {
        Object data[] = new Object[3];
        GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();

        // Clear the map
        for (Marker marker : mMarkers) {
            marker.remove();
        }
        mMarkers.clear();

        String url = buildRequestUrl(latitude, longitude, "restaurant");
        data[0] = mMap;
        data[1] = url;
        data[2] = mMarkers;

        getNearbyPlaces.execute(data);
        Toast.makeText(this, R.string.showing_restaurants, Toast.LENGTH_SHORT).show();
    }

    private String buildRequestUrl(double latitude, double longitude, String type) {
        StringBuilder requestUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        requestUrl.append("location=" + latitude + "," + longitude);
        requestUrl.append("&radius=" + PROXIMITY_RADIUS);
        requestUrl.append("&types=" + type);
        requestUrl.append("&sensor=true");
        requestUrl.append("&key=" + PLACES_API_KEY);

        return requestUrl.toString();
    }

    @Override
    public void onPlaceSelected(Place place) {
        LatLng location = place.getLatLng();
        String name = place.getName().toString();
        String address = place.getAddress().toString();
        String id = place.getId();

        stopLocationUpdates();

        // Clear the map
        for (Marker marker : mMarkers) {
            marker.remove();
        }
        mMarkers.clear();

        Marker marker = mMap.addMarker(new MarkerOptions().position(location).title(name).snippet(address)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        marker.setTag(id);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        mMarkers.add(marker);
    }

    @Override
    public void onError(Status status) {
        Toast.makeText(getApplicationContext(), "" + status.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(MapsActivity.this, RestaurantActivity.class);
        intent.putExtra("PLACE_ID", marker.getTag().toString());

        startActivity(intent);
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mMap.getUiSettings().setCompassEnabled(true);
                mMap.getUiSettings().setZoomControlsEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e(TAG,"Exception: %s", e);
        }
    }
}