package com.rgn.doctor;

/**
 * Created by root on 4/14/17.
 */

public class GetterSetterPendingAppointments {
    public int Patient_id;
    public String Patient_name;
    public String Patient_fcm_key;
    public String Patient_app_date;
    public int Patient_start_hr;
    public int Patient_start_min;
    public String Patient_Symptom_string;
    public String Patient_gender;

    public void setAllValues(int patient_id,String patient_name,String patient_fcm_key,String patient_app_date,int patient_start_hr,
                             int patient_start_min,String patient_Symptom_string,String patient_gender)
    {
        this.setPatient_id(patient_id);
        this.setPatient_name(patient_name);
        this.setPatient_fcm_key(patient_fcm_key);
        this.setPatient_app_date(patient_app_date);
        this.setPatient_start_hr(patient_start_hr);
        this.setPatient_start_min(patient_start_min);
        this.setPatient_Symptom_string(patient_Symptom_string);
        this.setPatient_gender(patient_gender);
    }
    public int getPatient_id() {
        return Patient_id;
    }

    public void setPatient_id(int patient_id) {
        Patient_id = patient_id;
    }

    public String getPatient_name() {
        return Patient_name;
    }

    public void setPatient_name(String patient_name) {
        Patient_name = patient_name;
    }

    public String getPatient_fcm_key() {
        return Patient_fcm_key;
    }

    public void setPatient_fcm_key(String patient_fcm_key) {
        Patient_fcm_key = patient_fcm_key;
    }

    public String getPatient_app_date() {
        return Patient_app_date;
    }

    public void setPatient_app_date(String patient_app_date) {
        Patient_app_date = patient_app_date;
    }

    public int getPatient_start_hr() {
        return Patient_start_hr;
    }

    public void setPatient_start_hr(int patient_start_hr) {
        Patient_start_hr = patient_start_hr;
    }

    public int getPatient_start_min() {
        return Patient_start_min;
    }

    public void setPatient_start_min(int patient_start_min) {
        Patient_start_min = patient_start_min;
    }

    public String getPatient_Symptom_string() {
        return Patient_Symptom_string;
    }

    public void setPatient_Symptom_string(String patient_Symptom_string) {
        Patient_Symptom_string = patient_Symptom_string;
    }

    public String getPatient_gender() {
        return Patient_gender;
    }

    public void setPatient_gender(String patient_gender) {
        Patient_gender = patient_gender;
    }
}
