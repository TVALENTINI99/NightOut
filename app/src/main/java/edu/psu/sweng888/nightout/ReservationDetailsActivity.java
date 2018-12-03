package edu.psu.sweng888.nightout;

import android.content.Intent;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.psu.sweng888.nightout.db.FirebaseAccess;
import edu.psu.sweng888.nightout.db.FirebaseCallbackInterface;
import edu.psu.sweng888.nightout.db.models.Reservation;

public class ReservationDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private String mName;
    private String mDate;
    private String mTime;
    private String mLocation;

    private TextView mTextViewName;
    private TextView mTextViewAddress;
    private TextView mTextViewDate;
    private TextView mTextViewTime;

    private Button mAddtoCal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_details);

        Intent detailIntent=getIntent();
        mName =detailIntent.getStringExtra("RES_NAME");
        mLocation=detailIntent.getStringExtra("RES_ADDRESS");
        mTime =detailIntent.getStringExtra("RES_TIME");
        mDate =detailIntent.getStringExtra("RES_DATE");

        mAddtoCal=findViewById(R.id.btn_add_reservation_to_cal);

        mTextViewName =findViewById(R.id.text_view_reservation_det_name);
        mTextViewAddress=findViewById(R.id.text_view_reservation_det_address);
        mTextViewDate =findViewById(R.id.textView_reservation_det_date_val);
        mTextViewTime =findViewById(R.id.textView_reservation_det_time_val);

        mTextViewName.setText(mName);
        mTextViewAddress.setText(mLocation);
        mTextViewDate.setText(mDate);
        mTextViewTime.setText(mTime);

        mAddtoCal.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_add_reservation_to_cal:
               Intent intent=new Intent(Intent.ACTION_INSERT);
               intent.setType("vnd.android.cursor.item/event");
                try {
                    Calendar calendar=Calendar.getInstance();
                    SimpleDateFormat dateFormat=new SimpleDateFormat("MM/dd/yyyy hh:mm a");
                    Date startDate=dateFormat.parse(mTextViewDate.getText().toString()+" "+mTextViewTime.getText().toString());
                    Log.d("onclick", "onClick: "+startDate.toString());
                    Date endDate= new Date(startDate.getTime()+(1*60*60*1000));
                    Log.d("onclick", "onClick: "+endDate.toString());
                    intent.putExtra("beginTime",startDate.getTime());
                    intent.putExtra("allDay",false);
                    intent.putExtra("endTime",endDate.getTime());
                    intent.putExtra("title","Reservation at "+mTextViewName.getText().toString());
                    intent.putExtra("eventLocation",mTextViewAddress.getText().toString());
                    startActivity(intent);
                    finish();
                }
                catch (Exception ex){
                    Log.e("Error Adding Event", "Error in adding event on calendar" + ex.getMessage());
                }
            default:
                break;
        }
    }
}
