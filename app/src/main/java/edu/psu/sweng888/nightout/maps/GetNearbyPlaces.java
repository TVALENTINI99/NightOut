package edu.psu.sweng888.nightout.maps;

import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GetNearbyPlaces extends AsyncTask<Object, String, String> {

    private GoogleMap mMap;
    private String placesData;
    private String url;
    private List<Marker> mMarkers;

    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap) objects[0];
        url = (String) objects[1];
        mMarkers = (List<Marker>) objects[2];

        RequestUrl requestUrl = new RequestUrl();
        try {
            placesData = requestUrl.readUrl(url);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return placesData;
    }

    @Override
    protected void onPostExecute(String s) {
        List<HashMap<String, String>> placesList;
        DataParser parser = new DataParser();
        placesList = parser.parse(s);
        showNearbyPlaces(placesList);
    }

    private void showNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {

        for (HashMap<String, String> place : nearbyPlacesList) {

            String name = place.get("place_name");
            String vicinity = place.get("vicinity");
            double latitude = Double.parseDouble(place.get("lat"));
            double longitude = Double.parseDouble(place.get("lng"));
            String id = place.get("place_id");

            LatLng location = new LatLng(latitude, longitude);
            Marker marker = mMap.addMarker(new MarkerOptions().position(location).title(name).snippet(vicinity)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            marker.setTag(id);
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

            mMarkers.add(marker);
        }
    }
}
