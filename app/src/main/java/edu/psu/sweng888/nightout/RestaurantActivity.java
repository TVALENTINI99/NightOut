package edu.psu.sweng888.nightout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import edu.psu.sweng888.nightout.adapter.PlaceRecyclerViewAdapter;

public class RestaurantActivity extends AppCompatActivity {

    private static final String TAG = "RestaurantActivity";

    private GeoDataClient mGeoDataClient;
    private TextView mPlaceNameTextView;
    private TextView mPlaceAddressTextView;
    private Button mReservationBtn;

    private ArrayList<Bitmap> photoList = new ArrayList<>();
    private PlaceRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        mGeoDataClient = Places.getGeoDataClient(this);

        mPlaceNameTextView = findViewById(R.id.text_view_place_name);
        mPlaceAddressTextView = findViewById(R.id.text_view_place_address);
        mReservationBtn = findViewById(R.id.btn_reserve);

        final Intent intent = getIntent();
        String placeId = intent.getStringExtra("PLACE_ID");

        mGeoDataClient.getPlaceById(placeId).addOnCompleteListener(new OnCompleteListener<PlaceBufferResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlaceBufferResponse> task) {
                if (task.isSuccessful()) {
                    PlaceBufferResponse placeBuffer = task.getResult();
                    Place place = placeBuffer.get(0);
                    getPhotos(place.getId());
                    onPlaceFound(place);
                    placeBuffer.release();
                }
                else {
                    Log.e(TAG, "Exception: %s", task.getException());
                }
            }
        });

        RecyclerView recyclerView = findViewById(R.id.horizontal_recycler_view);
        adapter = new PlaceRecyclerViewAdapter(this, photoList);
//        adapter.notifyDataSetChanged();

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

        mReservationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reservationIntent =new Intent(RestaurantActivity.this,ReservationActivity.class);
                reservationIntent.putExtra("RES_NAME",mPlaceNameTextView.getText().toString());
                reservationIntent.putExtra("RES_ADDR",mPlaceAddressTextView.getText().toString());
                startActivity(reservationIntent);
            }
        });
    }
    //TODO: Add more place details, add multiple scrollable photos
    private void onPlaceFound(Place place) {

//        getPhotos(place.getId());

        mPlaceNameTextView.setText(place.getName());
        mPlaceAddressTextView.setText(place.getAddress());

    }

    private void getPhotos(String placeId) {

        mGeoDataClient.getPlacePhotos(placeId).addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
                // Get the list of photos.
                PlacePhotoMetadataResponse photos = task.getResult();
                // Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
                PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
                // Get the first photo in the list.
//                PlacePhotoMetadata photoMetadata = photoMetadataBuffer.get(0);
//                // Get the attribution text.
//                CharSequence attribution = photoMetadata.getAttributions();
//                // Get a full-size bitmap for the photo.
//                Task<PlacePhotoResponse> photoResponse = mGeoDataClient.getPhoto(photoMetadata);
//                photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
//                    @Override
//                    public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
//                        PlacePhotoResponse photo = task.getResult();
//                        Bitmap bitmap = photo.getBitmap();
//                        photoList.add(bitmap);
//                        adapter.notifyDataSetChanged();
//                    }
//                });

                for (int i = 0; i < photoMetadataBuffer.getCount(); i++) {

                    PlacePhotoMetadata photoMetadata = photoMetadataBuffer.get(i);
                    Task<PlacePhotoResponse> photoResponse = mGeoDataClient.getPhoto(photoMetadata);
                    photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
                        @Override
                        public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
                            PlacePhotoResponse photo = task.getResult();
                            Bitmap bitmap = photo.getBitmap();
                            photoList.add(bitmap);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }
}
