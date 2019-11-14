package com.rgn.doctor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class CurrentPatient extends Fragment {
    View view;
    DatabaseHelper db;
    GetterSetterAppointments getterSetterAppointments;
    private final int DATABASE_VERSION = 1;
    private final String DATABASE_NAME="Doctor";
    TextView name,appdatentime,patientsymptoms;
    private String symptomstring;

    public CurrentPatient() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_current_patient,container,false);
        db = new DatabaseHelper(getContext(),DATABASE_NAME,null,DATABASE_VERSION);
        name = (TextView) view.findViewById(R.id.patientname);
        appdatentime = (TextView) view.findViewById(R.id.patientappdatetime);
        patientsymptoms = (TextView) view.findViewById(R.id.patientsymptoms);
        GetterSetterAppointments getterSetterAppointments = db.getCurrentPatient();
        if(getterSetterAppointments!=null){
            name.setText(getterSetterAppointments.getPat_name());
            String datentime = getterSetterAppointments.getPat_app_date()+"  "+getterSetterAppointments.getPat_app_hr()+":"+getterSetterAppointments.getPat_app_min();
            appdatentime.setText(datentime);
            symptomstring = getterSetterAppointments.getPat_ss();
        }
        patientsymptoms.setText(getSymptomstring(symptomstring));
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private String getSymptomstring(String symptomstring){
        List<String> symptomslist = new ArrayList<String>();
        //0
        symptomslist.add("Headache");
        symptomslist.add("Back Pain");
        symptomslist.add("Neck Pain");
        symptomslist.add("Muscular Pain");
        symptomslist.add("Pelvic Pain");
        symptomslist.add("Stomach ache");
        symptomslist.add("Chest Pain");
        symptomslist.add("Joint Pain");
        symptomslist.add("Abdominal Pain");
        //9
        symptomslist.add("Arm Pits");
        symptomslist.add("Neck Skin Inflammation");
        //11
        symptomslist.add("Dry Coughing");
        symptomslist.add("Wet Coughing");
        //13
        symptomslist.add("Skin Burns");
        symptomslist.add("Bleeding/Cuts");
        symptomslist.add("Bruises");
        //16
        symptomslist.add("High fever");
        symptomslist.add("Medium fever");
        symptomslist.add("Low fever");
        symptomslist.add("Rising fever");
        symptomslist.add("Night fever");
        //21
        symptomslist.add("Face Rashes");
        symptomslist.add("Skin to Skin friction Rashes");
        symptomslist.add("Rashes with bumps");
        //24
        symptomslist.add("Projectile Vomiting");
        symptomslist.add("Blood Stained Vomiting");
        symptomslist.add("Severe Vomiting");
        //27
        symptomslist.add("Weakness");
        symptomslist.add("Extreme Tiredness");
        symptomslist.add("Sleepy");
        //30
        symptomslist.add("Medium Shivering");
        symptomslist.add("Cold defensive Shivering");
        symptomslist.add("Severe Shivering");
        //33
        symptomslist.add("Diahorrea type loose motion");
        symptomslist.add("Extreme loose motion");

        String symptoms="";
        int a[] = new int[]{0,9,11,13,16,21,24,27,30,33};
        for(int i=0;i<10;i++){
            if(Character.getNumericValue(symptomstring.charAt(i))!=9)
            {
                symptoms = symptoms+symptomslist.get(a[i]+Character.getNumericValue(symptomstring.charAt(i)))+"\n";
            }
        }
        return symptoms;
    }
}
