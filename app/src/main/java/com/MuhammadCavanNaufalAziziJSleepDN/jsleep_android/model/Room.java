package com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android.model;

import java.util.ArrayList;
import java.util.Date;

public class Room extends Serializable{
    public int accountId;
    public String name;
    public int size;
    public Price price;
    public ArrayList<Facility> facility;
    public String address;
    public BedType bedType;
    public City city;
    public ArrayList<Date> booked;

    public Room(int accountId, String name, int size, Price price, ArrayList<Facility> facility, City city, String address, BedType bedType) {
        this.accountId = accountId;
        this.name = name;
        this.size = size;
        this.price = price;
        this.facility.addAll(facility);
        this.bedType = bedType;
        this.city = city;
        this.address = address;
        this.booked = new ArrayList<>();
    }
}
