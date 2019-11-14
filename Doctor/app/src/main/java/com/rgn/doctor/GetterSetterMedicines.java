package com.rgn.doctor;

/**
 * Created by root on 4/14/17.
 */

public class GetterSetterMedicines {
    public int Medicine_id;
    public String Medicine_name;
    public String Medicine_type;
    public int Medicine_dose;
    public int Medicine_timing;
    public int Medicine_base_cost;


    public void setAllValues( int Medicine_id,
             String Medicine_name,
             String Medicine_type,
             int Medicine_dose,int Medicine_timing,int Medicine_base_cost
                              ){
        this.setMedicine_id(Medicine_id);
        this.setMedicine_name(Medicine_name);
        this.setMedicine_type(Medicine_type);
        this.setMedicine_dose(Medicine_dose);
        this.setMedicine_timing(Medicine_timing);
        this.setMedicine_base_cost(Medicine_base_cost);
    }
    public int getMedicine_id() {
        return Medicine_id;
    }

    public void setMedicine_id(int medicine_id) {
        Medicine_id = medicine_id;
    }

    public String getMedicine_name() {
        return Medicine_name;
    }

    public void setMedicine_name(String medicine_name) {
        Medicine_name = medicine_name;
    }

    public String getMedicine_type() {
        return Medicine_type;
    }

    public void setMedicine_type(String medicine_type) {
        Medicine_type = medicine_type;
    }

    public int getMedicine_dose() {
        return Medicine_dose;
    }

    public void setMedicine_dose(int medicine_dose) {
        Medicine_dose = medicine_dose;
    }

    public int getMedicine_timing() {
        return Medicine_timing;
    }

    public void setMedicine_timing(int medicine_timing) {
        Medicine_timing = medicine_timing;
    }

    public int getMedicine_base_cost() {
        return Medicine_base_cost;
    }

    public void setMedicine_base_cost(int medicine_base_cost) {
        Medicine_base_cost = medicine_base_cost;
    }
}
