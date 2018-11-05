package edu.psu.sweng888.nightout;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.psu.sweng888.nightout.maps.GetNearbyPlaces;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final int REQUEST_LOCATION_CODE = 99;
    private static final int PROXIMITY_RADIUS = 5000;
    private static final String TAG = "CurrentLocation";

    private GoogleMap mMap;
    protected LocationManager locationManager;
    private double latitude, longitude;

    private Marker currentLocationMarker;
    private Button mNearbyRestaurantButton = null;

    @Override    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isGooglePlayServicesAvailable()) {
            return;
        }
        setContentView(R.layout.activity_maps);

        mNearbyRestaurantButton = findViewById(R.id.btn_restaurants);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**     * Manipulates the map once available.
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

        displayCurrentLocation(mMap);
    }

    private void displayCurrentLocation(GoogleMap mMap) {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
        }

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(bestProvider);

        if (location != null) {
            onLocationChanged(location);

        }

        locationManager.requestLocationUpdates(bestProvider, 20000, 100, this);

    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        if (currentLocationMarker != null) {
            currentLocationMarker.remove();
        }

        LatLng currentLocation = new LatLng(latitude, longitude);
        currentLocationMarker = mMap.addMarker(new MarkerOptions().position(currentLocation).title("My Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
    }

    public void onNearbyRestaurantsClicked(View v) {
        Object data[] = new Object[2];
        GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();
//        mMap.clear();

        String url = buildRequestUrl(latitude, longitude, "restaurant");
        data[0] = mMap;
        data[1] = url;

        getNearbyPlaces.execute(data);
        Toast.makeText(this, R.string.showing_restaurants, Toast.LENGTH_SHORT).show();
    }

    private String buildRequestUrl(double latitude, double longitude, String type) {
        StringBuilder requestUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        requestUrl.append("location=" + latitude + "," + longitude);
        requestUrl.append("&radius=" + PROXIMITY_RADIUS);
        requestUrl.append("&types=" + type);
        requestUrl.append("&sensor=true");
        requestUrl.append("&key=");
        requestUrl.append("AIzaSyCY8bnxF3qpnrzoT-vnErkba0soGT0Jy8w");

        return requestUrl.toString();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}