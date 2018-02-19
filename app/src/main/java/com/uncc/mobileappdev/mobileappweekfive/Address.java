package com.uncc.mobileappdev.mobileappweekfive;

/**
 * Created by Stephen Weber on 2/19/2018.
 */

public class Address {
    private String line1;
    private String city;
    private String state;
    private String zip;

    public Address() {

    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
