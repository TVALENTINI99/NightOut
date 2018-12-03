package edu.psu.sweng888.nightout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Button mBtnSearch = null;
    private Button mBtnLogin = null;
    private Button mBtnReserverations=null;
    private Button mBtnInvoices=null;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        if(user == null){
            Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
            loginIntent.putExtra("class","ReservationActivity");
            startActivity(loginIntent);
            mAuth=FirebaseAuth.getInstance();
            user=mAuth.getCurrentUser();
        }

        mBtnSearch = (Button) findViewById(R.id.btn_search);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnReserverations = (Button) findViewById(R.id.btn_view_reservations);
        mBtnInvoices = (Button) findViewById(R.id.btn_view_invoices);

        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
            }
        });
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
        mBtnReserverations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CurrentReservationsActivity.class));
            }
        });
        mBtnInvoices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CurrentInvoicesActivity.class));
            }
        });
    }
}
