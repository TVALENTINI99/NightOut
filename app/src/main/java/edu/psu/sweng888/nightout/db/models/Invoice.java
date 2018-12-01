package edu.psu.sweng888.nightout.db.models;

public class Invoice {
    public String Id;
    public String Name;
    public String LocationName;
    public String Date;
    public String Total;

    public Invoice(String id, String name, String locationName, String date, String total) {
        Id = id;
        Name = name;
        LocationName = locationName;
        Date = date;
        Total = total;
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

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }


}
