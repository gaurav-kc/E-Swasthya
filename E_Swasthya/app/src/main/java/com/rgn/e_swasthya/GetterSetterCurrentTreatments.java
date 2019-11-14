package com.rgn.e_swasthya;

/**
 * Created by root on 4/14/17.
 */

public class GetterSetterCurrentTreatments {
    public int Doc_id;
    public String Doc_name;
    public String fcm_key;
    public String start_date;
    public int duration;

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

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }


}
