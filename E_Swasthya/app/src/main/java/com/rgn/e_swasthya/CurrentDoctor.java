package com.rgn.e_swasthya;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class CurrentDoctor extends Fragment {
    private View view;
    private TextView name,dateandtime,type,rating;
    private DatabaseHelper databaseHelper;
    private static final String DATABASE_NAME="Doctor";
    private static final int DATABASE_VERSION=1;

    public CurrentDoctor() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_current_doctor, container, false);
        databaseHelper = new DatabaseHelper(getContext(),DATABASE_NAME,null,DATABASE_VERSION);
        GetterSetterAppointments getterSetterAppointments = databaseHelper.getCurrentDoctor();
        name = (TextView)view.findViewById(R.id.doctorname);
        dateandtime = (TextView)view.findViewById(R.id.doctorappdateandtime);
        type = (TextView)view.findViewById(R.id.doctortype);
        rating = (TextView)view.findViewById(R.id.doctorrating);
        name.setText(getterSetterAppointments.getDoc_name());
        dateandtime.setText(getterSetterAppointments.getDate()+"   "+getterSetterAppointments.getHr()+":"+getterSetterAppointments.getMin());
        type.setText(getterSetterAppointments.getType());
        rating.setText(getterSetterAppointments.getRating());
        return view;
    }

    public interface OnFragmentInteractionListener {
        void ScanCodeUpdate();
    }

    @Override
    public void onDestroyView() {
        Log.d("D ScanCode","");
        super.onDestroyView();
    }
}
