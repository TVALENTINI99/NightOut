package edu.psu.sweng888.nightout.db.models;

import java.util.HashMap;
import java.util.Map;

public class Reservation {

    public String Id;
    public String Name;
    public String LocationName;
    public String LocationAddress;
    public String Date;
    public String Time;

    public Reservation(String id, String name, String locationName, String locationAddress, String date, String time) {
        Id = id;
        Name = name;
        LocationName = locationName;
        LocationAddress = locationAddress;
        Date = date;
        Time = time;
    }
    public Map<String,Object> toMap(){
        HashMap<String,Object> result= new HashMap<>();
        result.put("Id",Id);
        result.put("Name",Name);
        result.put("LocationName",LocationName);
        result.put("LocationAddress",LocationAddress);
        result.put("Date",Date);
        result.put("Time",Time);
        return result;

    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLocationName() {
        return LocationName;
    }

    public void setLocationName(String locationName) {
        LocationName = locationName;
    }

    public String getLocationAddress() {
        return LocationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        LocationAddress = locationAddress;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
