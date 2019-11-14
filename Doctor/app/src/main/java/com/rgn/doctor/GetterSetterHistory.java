package com.rgn.doctor;

/**
 * Created by root on 4/14/17.
 */

public class GetterSetterHistory {
    public int History_id;
    public String Patient_name;
    public String Treatment_start_date;
    public int Treatment_Duration;
    public String Patient_ss;

    public void setAllValues( int History_id,
             String Patient_name,
             String Treatment_start_date,
             int Treatment_Duration,
             String Patient_ss){
        this.setHistory_id(History_id);
        this.setPatient_name(Patient_name);
        this.setTreatment_start_date(Treatment_start_date);
        this.setTreatment_Duration(Treatment_Duration);
        this.setPatient_ss(Patient_ss);
    }
    public int getHistory_id() {
        return History_id;
    }

    public void setHistory_id(int history_id) {
        History_id = history_id;
    }

    public String getPatient_name() {
        return Patient_name;
    }

    public void setPatient_name(String patient_name) {
        Patient_name = patient_name;
    }

    public String getTreatment_start_date() {
        return Treatment_start_date;
    }

    public void setTreatment_start_date(String treatment_start_date) {
        Treatment_start_date = treatment_start_date;
    }

    public int getTreatment_Duration() {
        return Treatment_Duration;
    }

    public void setTreatment_Duration(int treatment_Duration) {
        Treatment_Duration = treatment_Duration;
    }

    public String getPatient_ss() {
        return Patient_ss;
    }

    public void setPatient_ss(String patient_ss) {
        Patient_ss = patient_ss;
    }
}
