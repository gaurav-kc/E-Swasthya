package com.rgn.doctor;

/**
 * Created by root on 4/14/17.
 */

public class GetterSetterCurrentTreatments {
    public int Patient_id;
    public String Patient_name;
    public String Patient_fcm_key;
    public String Patient_start_date;
    public String Patient_symptom_string;

    public void setAllValues( int Patient_id,
             String Patient_name,
             String Patient_fcm_key,
             String Patient_start_date,
             String Patient_symptom_string){
        this.setPatient_id(Patient_id);
        this.setPatient_name(Patient_name);
        this.setPatient_fcm_key(Patient_fcm_key);
        this.setPatient_start_date(Patient_start_date);
        this.setPatient_symptom_string(Patient_symptom_string);
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


    public String getPatient_start_date() {
        return Patient_start_date;
    }

    public void setPatient_start_date(String patient_start_date) {
        Patient_start_date = patient_start_date;
    }

    public String getPatient_symptom_string() {
        return Patient_symptom_string;
    }

    public void setPatient_symptom_string(String patient_symptom_string) {
        Patient_symptom_string = patient_symptom_string;
    }
}
