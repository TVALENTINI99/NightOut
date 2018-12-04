package edu.psu.sweng888.nightout;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import edu.psu.sweng888.nightout.adapter.InvoiceRecyclerViewAdapter;
import edu.psu.sweng888.nightout.adapter.ReservationRecyclerViewAdapter;
import edu.psu.sweng888.nightout.db.FirebaseAccess;
import edu.psu.sweng888.nightout.db.FirebaseCallbackInterface;
import edu.psu.sweng888.nightout.db.models.Invoice;
import edu.psu.sweng888.nightout.db.models.Reservation;

public class CurrentInvoicesActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAccess mFirebaseAccess;
    private RecyclerView mRecyclerView;
    private InvoiceRecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<Invoice> invoiceDataList=new ArrayList<>();
    private ActionBar mActionBar;
    private static final String TAG = "CurrentInvoicesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_invoices);

        mActionBar=getSupportActionBar();
        mActionBar.setTitle(R.string.current_invoices);

        mFirebaseAccess=new FirebaseAccess();
        mAuth=FirebaseAuth.getInstance();
        //TODO:In Real Life an API would be provided for businesses to do this, since we are not in production we are fake loading them
        //invoiceLoadingForTesting();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null){
            Intent loginIntent = new Intent(CurrentInvoicesActivity.this,LoginActivity.class);
            loginIntent.putExtra("class","CurrentInvoicesActivity");
            startActivity(loginIntent);
            mAuth=FirebaseAuth.getInstance();
            user=mAuth.getCurrentUser();
        }
        buildInvoiceRecyclerView();

        mFirebaseAccess.getInvoices(user.getUid(), new FirebaseCallbackInterface<Invoice>() {
            @Override
            public void onCallback(ArrayList<Invoice> value) {
                for(Invoice invoice:value) {
                    invoiceDataList.add(invoice);
                }

                recyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }
    private void buildInvoiceRecyclerView(){
        this.mRecyclerView = findViewById(R.id.recycler_view_invoices);
        this.recyclerViewAdapter = new InvoiceRecyclerViewAdapter(this, invoiceDataList);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        this.mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        this.mRecyclerView.setAdapter(recyclerViewAdapter);

    }
    private void invoiceLoadingForTesting(){
        FirebaseUser user =mAuth.getCurrentUser();
        Invoice invoice1=new Invoice(user.getUid(),user.getDisplayName(),"Burgatory","12/01/2018","30.00");
        Invoice invoice2=new Invoice(user.getUid(),user.getDisplayName(),"Harry's","12/02/2018","52.97");
        mFirebaseAccess.addInvoicetoDB(invoice1);
        mFirebaseAccess.addInvoicetoDB(invoice2);
    }

}
