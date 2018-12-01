package edu.psu.sweng888.nightout;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import edu.psu.sweng888.nightout.date_picker.DatePickerFragment;
import edu.psu.sweng888.nightout.date_picker.DatePickerInteractionInterface;
import edu.psu.sweng888.nightout.db.FirebaseAccess;
import edu.psu.sweng888.nightout.db.models.Reservation;
import edu.psu.sweng888.nightout.time_picker.TimePickerFragment;
import edu.psu.sweng888.nightout.time_picker.TimePickerInteractionInterface;

public class ReservationActivity extends AppCompatActivity implements TimePickerInteractionInterface,DatePickerInteractionInterface {

    private Button mReservationButton;
    private EditText mDateEditText;
    private EditText mTimeEditText;
    private TextView mPlaceNameTextView;
    private TextView mPlaceAddressTextView;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        final Intent intent = getIntent();

        mAuth = FirebaseAuth.getInstance();

        mReservationButton= findViewById(R.id.button_make_reservation);
        mDateEditText=findViewById(R.id.editText_reservation_date);
        mTimeEditText=findViewById(R.id.editText_reservation_time);
        mPlaceAddressTextView=findViewById(R.id.text_view_reservation_res_address);
        mPlaceNameTextView=findViewById(R.id.text_view_reservation_res_name);

        mPlaceNameTextView.setText(intent.getStringExtra("RES_NAME"));
        mPlaceAddressTextView.setText(intent.getStringExtra("RES_ADDR"));

        mTimeEditText.setKeyListener(null);
        mDateEditText.setKeyListener(null);

        mTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);
            }
        });
        mDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });
        mReservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = mAuth.getCurrentUser();
                if(user == null){
                    Intent loginIntent = new Intent(ReservationActivity.this,LoginActivity.class);
                    loginIntent.putExtra("class","ReservationActivity");
                    startActivity(loginIntent);
                    mAuth=FirebaseAuth.getInstance();
                    user=mAuth.getCurrentUser();
                }
                FirebaseAccess mFirebaseHelper = new FirebaseAccess();
                Reservation reservation = new Reservation(user.getUid(),
                        user.getDisplayName(),
                        mPlaceNameTextView.getText().toString(),
                        mPlaceAddressTextView.getText().toString(),
                        mDateEditText.getText().toString(),
                        mTimeEditText.getText().toString());
                mFirebaseHelper.addReservationtoDB(reservation);
                finish();
            }
        });

    }
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    public void showTimePickerDialog(View V) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }
    public void onDateComplete(String date) {
        // After the dialog fragment completes, it calls this callback.
        // use the string here
        mDateEditText.setText(date);
    }
    public void onTimeComplete(String time) {
        // After the dialog fragment completes, it calls this callback.
        // use the string here
        mTimeEditText.setText(time);
    }
}
