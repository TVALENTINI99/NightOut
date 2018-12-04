package edu.psu.sweng888.nightout;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import edu.psu.sweng888.nightout.adapter.ReservationRecyclerViewAdapter;
import edu.psu.sweng888.nightout.db.FirebaseAccess;
import edu.psu.sweng888.nightout.db.FirebaseCallbackInterface;
import edu.psu.sweng888.nightout.db.models.Reservation;

public class CurrentReservationsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAccess mFirebaseAccess;
    private RecyclerView mRecyclerView;
    private ActionBar mActionBar;
    private ReservationRecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<Reservation> reservationDataList=new ArrayList<>();
    private static final String TAG = "CurrentReservationsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_reservations);

        mActionBar=getSupportActionBar();
        mActionBar.setTitle(R.string.current_reservations);

        mFirebaseAccess=new FirebaseAccess();
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null){
            Intent loginIntent = new Intent(CurrentReservationsActivity.this,LoginActivity.class);
            loginIntent.putExtra("class","CurrentReservationsActivity");
            startActivity(loginIntent);
            mAuth=FirebaseAuth.getInstance();
            user=mAuth.getCurrentUser();
        }
        buildResRecyclerView();

        mFirebaseAccess.getReservations(user.getUid(), new FirebaseCallbackInterface<Reservation>() {
            @Override
            public void onCallback(ArrayList<Reservation> value) {
                for(Reservation reservation:value) {
                    reservationDataList.add(reservation);
                }

                recyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }
    private void buildResRecyclerView(){
        this.mRecyclerView = findViewById(R.id.recycler_view_reservation);
        this.recyclerViewAdapter = new ReservationRecyclerViewAdapter(this, reservationDataList);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        this.mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        this.mRecyclerView.setAdapter(recyclerViewAdapter);

    }
}
