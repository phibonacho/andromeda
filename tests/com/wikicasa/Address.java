package com.wikicasa;

import com.annotation.validate.Validate;
import com.annotation.validate.types.DPositive;
import com.annotation.validate.types.NestedVal;
import com.annotation.validate.types.StringValue;

public class Address {
    private String street;
    private String streetNumber;
    private String zip;
    private String apartmentNumber;
    private String stair;
    private String floor;
    private City city;
    private double latitude;
    private double longitude;

    @Validate(with = StringValue.class, mandatory = true)
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Validate(with = StringValue.class, mandatory = true)
    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    @Validate(with = StringValue.class, mandatory = true)
    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    @Validate(with = StringValue.class)
    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    @Validate(with = StringValue.class)
    public String getStair() {
        return stair;
    }

    public void setStair(String stair) {
        this.stair = stair;
    }

    @Validate(with = StringValue.class)
    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    @Validate(with = NestedVal.class, mandatory = true)
    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Validate(with = DPositive.class, mandatory = true)
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Validate(with = DPositive.class, mandatory = true)
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
