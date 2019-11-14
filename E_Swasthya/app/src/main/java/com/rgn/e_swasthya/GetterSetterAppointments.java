package com.rgn.e_swasthya;

/**
 * Created by root on 4/14/17.
 */

public class GetterSetterAppointments {
    public int Doc_id;
    public String Doc_name;
    public String fcm_key;
    public double Latitude;
    public double longitude;
    public String date;
    public String type;
    public String rating;
    public int hr;
    public int min;

    public void setAllValues(int doc_id,String doc_name,String fcm_key,double latitude,double longitude,
                             String date,String type,String rating,int hr,int min){
        this.Doc_id = doc_id;
        this.Doc_name = doc_name;
        this.fcm_key = fcm_key;
        this.Latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.type = type;
        this.rating = rating;
        this.hr = hr;
        this.min = min;
    }

    public int getDoc_id() {
        return Doc_id;
    }

    public void setDoc_id(int doc_id) {
        Doc_id = doc_id;
    }

    public String getDoc_name() {
        return Doc_name;
    }

    public void setDoc_name(String doc_name) {
        Doc_name = doc_name;
    }

    public String getFcm_key() {
        return fcm_key;
    }

    public void setFcm_key(String fcm_key) {
        this.fcm_key = fcm_key;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public int getHr() {
        return hr;
    }

    public void setHr(int hr) {
        this.hr = hr;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }
}
