package com.rgn.e_swasthya;

/**
 * Created by root on 4/14/17.
 */

public class GetterSetterMedicines {
    public int med_id;
    public int doc_id;
    public String med_name;
    public int med_dose;
    public int med_timing;
    public String med_type;
    public int med_base_cost;

    public int getMed_id() {
        return med_id;
    }

    public void setMed_id(int med_id) {
        this.med_id = med_id;
    }

    public int getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(int doc_id) {
        this.doc_id = doc_id;
    }

    public String getMed_name() {
        return med_name;
    }

    public void setMed_name(String med_name) {
        this.med_name = med_name;
    }

    public int getMed_dose() {
        return med_dose;
    }

    public void setMed_dose(int med_dose) {
        this.med_dose = med_dose;
    }

    public int getMed_timing() {
        return med_timing;
    }

    public void setMed_timing(int med_timing) {
        this.med_timing = med_timing;
    }

    public String getMed_type() {
        return med_type;
    }

    public void setMed_type(String med_type) {
        this.med_type = med_type;
    }

    public int getMed_base_cost() {
        return med_base_cost;
    }

    public void setMed_base_cost(int med_base_cost) {
        this.med_base_cost = med_base_cost;
    }
}
