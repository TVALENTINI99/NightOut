package edu.psu.sweng888.nightout.db;

import java.util.ArrayList;

import edu.psu.sweng888.nightout.db.models.Reservation;

public interface FirebaseCallbackInterface {
    void onCallback(ArrayList<Reservation> value);
}
