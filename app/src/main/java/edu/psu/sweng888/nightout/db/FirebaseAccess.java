package edu.psu.sweng888.nightout.db;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import edu.psu.sweng888.nightout.db.models.Invoice;
import edu.psu.sweng888.nightout.db.models.Reservation;

public class FirebaseAccess {

    private DatabaseReference db;
    private ArrayList<Reservation> reservations;
    private ArrayList<Invoice> invoices;

    public DatabaseReference getDb() {
        return db;
    }

    public ArrayList<Reservation> getReservations(String uid,FirebaseCallbackInterface callbackInterface) {
        readReservationsFromDB(uid,callbackInterface);
        return reservations;
    }

    public ArrayList<Invoice> getInvoices(String uid, FirebaseCallbackInterface callbackInterface) {
        readInvoicesFromDB(uid,callbackInterface);
        return invoices;
    }

    public FirebaseAccess() {
        this.db=FirebaseDatabase.getInstance().getReference();
    }

    public FirebaseAccess(DatabaseReference db) {
        this.db = db;
    }

    public void addReservationtoDB(Reservation reservation){
        String key = db.child("reservations").push().getKey();
        Map<String,Object> reservationVals =reservation.toMap();

        Map<String,Object> childUpdates = new HashMap<>();
        childUpdates.put("/reservations/"+key,reservationVals);
        db.updateChildren(childUpdates);

    }

    public void readReservationsFromDB(final String Uid, final FirebaseCallbackInterface firebaseCallbackInterface){

        DatabaseReference resRef=db.child("reservations");
        resRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Reservation>value=parseReservationData((Map<String,Object>)dataSnapshot.getValue(),Uid);
                firebaseCallbackInterface.onCallback(value);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void readInvoicesFromDB(final String Uid, final FirebaseCallbackInterface firebaseCallbackInterface){

        DatabaseReference resRef=db.child("invoices");
        resRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Invoice>value=parseInvoiceData((Map<String,Object>)dataSnapshot.getValue(),Uid);
                firebaseCallbackInterface.onCallback(value);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public ArrayList<Reservation> parseReservationData(Map<String,Object> values,String id){
        ArrayList<Reservation> UserReservations = new ArrayList<>();
        for (Map.Entry<String,Object> entry : values.entrySet()){
            Map singleReservation = (Map) entry.getValue();
            if(((String)singleReservation.get("Id")).equals(id.toString())){
                Reservation reservation = new Reservation((String)singleReservation.get("Id"),
                        (String)singleReservation.get("Name"),
                        (String)singleReservation.get("LocationName"),
                        (String)singleReservation.get("LocationAddress"),
                        (String)singleReservation.get("Date"),
                        (String)singleReservation.get("Time"));
                UserReservations.add(reservation);
            }
        }
        return UserReservations;
    }
    public ArrayList<Invoice> parseInvoiceData(Map<String,Object> values,String id){
        ArrayList<Invoice> UserInvoices = new ArrayList<>();
        for (Map.Entry<String,Object> entry : values.entrySet()){
            Map singleInvoice = (Map) entry.getValue();
            if(((String)singleInvoice.get("Id")).equals(id.toString())){
                Invoice invoice= new Invoice((String)singleInvoice.get("Id"),
                        (String)singleInvoice.get("Name"),
                        (String)singleInvoice.get("LocationName"),
                        (String)singleInvoice.get("Date"),
                        (String)singleInvoice.get("Total"));
                UserInvoices.add(invoice);
            }
        }
        return UserInvoices;
    }

}
