package edu.psu.sweng888.nightout.db;

import java.util.ArrayList;

import edu.psu.sweng888.nightout.db.models.Invoice;
import edu.psu.sweng888.nightout.db.models.Reservation;

public interface FirebaseCallbackInterface<T> {
    void onCallback(ArrayList<T> value);
}
