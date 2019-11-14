package com.rgn.doctor;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dexafree.materialList.card.Action;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.card.OnActionClickListener;
import com.dexafree.materialList.card.action.TextViewAction;
import com.dexafree.materialList.view.MaterialListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

import static android.content.Context.MODE_PRIVATE;

public class Appointments extends Fragment{
    private Context mContext;
    private MaterialListView mListView;
    private OnFragmentInteractionListener mListener;
    private DatabaseHelper db;
    private final int DATABASE_VERSION = 1;
    private final String DATABASE_NAME="Doctor";
    private static final String SHAREDPREFERENCE = "SharedPref";
    private SharedPreferences sharedPreferences;
    private int docid;
    private String docname;
    public Appointments() {
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
        mListener.AppointmentsUpdate();
        mContext = getContext();
        db = new DatabaseHelper(getContext(),DATABASE_NAME, null, DATABASE_VERSION);
        View view = inflater.inflate(R.layout.fragment_appointments, container, false);
        // Bind the MaterialListView to a variable
        mListView = (MaterialListView) view.findViewById(R.id.material_listview_appointments);
        mListView.setItemAnimator(new SlideInLeftAnimator());
        mListView.getItemAnimator().setAddDuration(300);
        mListView.getItemAnimator().setRemoveDuration(300);
        sharedPreferences = getActivity().getSharedPreferences(SHAREDPREFERENCE,MODE_PRIVATE);
        docid = sharedPreferences.getInt("Doc_id",0);
        docname = sharedPreferences.getString("Name",null);
        showCards();
        return view;
    }


    private void showCards(){
        Cursor cursor = db.getAllA();
        Log.d("TAG app","in show cards");
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            addCard(cursor.getString(1),cursor.getInt(0));
            cursor.moveToNext();
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void AppointmentsUpdate();
    }

    @Override
    public void onDestroyView() {
        Log.d("D Appointment","");
        super.onDestroyView();
    }
    private void addCard(String title,int tag) {
        mListView.getAdapter().add(new Card.Builder(mContext)
                .setTag(tag)
                .withProvider(new CardProvider())
                .setLayout(R.layout.appointments_card_view)
                .setTitle(title)
                .setDividerVisible(true)
                .addAction(R.id.A_app_date, new Action(getContext()) {
                    @Override
                    protected void onRender(@NonNull View view, @NonNull Card card) {
                        final int currentpos = Integer.parseInt(card.getTag().toString());
                        GetterSetterAppointments getterSetterAppointments =
                                db.getDetailsA(currentpos);
                        l(getterSetterAppointments.getPat_app_date()+"   "+getterSetterAppointments.getPat_app_hr()+"  "
                                +getterSetterAppointments.getPat_app_min());
                        final Button button = (Button)view.findViewById(R.id.A_app_date);
                        button.setText(getterSetterAppointments.getPat_app_date());

                    }
                })
                .addAction(R.id.A_app_time, new Action(getContext()) {
                    @Override
                    protected void onRender(@NonNull View view, @NonNull Card card) {
                        final int currentpos = Integer.parseInt(card.getTag().toString());
                        GetterSetterAppointments getterSetterAppointments =
                                db.getDetailsA(currentpos);
                        l(getterSetterAppointments.getPat_app_date()+"   "+getterSetterAppointments.getPat_app_hr()+"  "
                                +getterSetterAppointments.getPat_app_min());
                        final Button button = (Button)view.findViewById(R.id.A_app_time);
                        button.setText(getterSetterAppointments.getPat_app_hr()+":"+getterSetterAppointments.getPat_app_min());
                    }
                })
                .addAction(R.id.A_cancel_app, new TextViewAction(mContext)
                        .setListener(new OnActionClickListener() {
                            @Override
                            public void onActionClicked(View view, Card card) {
                                int pos = Integer.parseInt(card.getTag().toString());
                                card.setDismissible(true);
                                card.dismiss();
                            }
                        }))
                .addAction(R.id.A_info,new TextViewAction(mContext)
                        .setListener(new OnActionClickListener() {
                            @Override
                            public void onActionClicked(View view, Card card) {
                                int pos = Integer.parseInt(card.getTag().toString());
                                AlertDialog.Builder al = new AlertDialog.Builder(getContext());
                                al.
                                        setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                al.setTitle("Some Information");
                                al.setMessage("Symptoms Sent:\nRashes\nInflammation\nJoint pain");
                                al.show();
                            }
                        })
                )
                .addAction(R.id.A_medicines, new TextViewAction(mContext)
                        .setListener(new OnActionClickListener() {
                            @Override
                            public void onActionClicked(View view, Card card) {
                                final int currentpos = Integer.parseInt(card.getTag().toString());
                                Intent i = new Intent(Appointments.this.getContext(),MedicinesActivity.class);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("temppatient",currentpos);
                                editor.apply();
                                startActivity(i);
                                db.moveFromAppointmentToCurrentTreatment(currentpos);
                                card.setDismissible(true);
                                card.dismiss();
                            }
                        })
                )
                .endConfig()
                .build());
    }


    private void l(String message){
        Log.d("Appointments ",message);
    }

    private void sendAppointmentCancellation(int pat_id){
        String link = "http://gauravkc.pe.hu/appointment_canceled_by_doctor.php?"+
                "pat_id="+pat_id+"&"+
                "doc_id="+docid+"&"+
                "doc_name="+docname;
        new DownloadRawData().execute(link);
    }


    private class DownloadRawData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String link = params[0];
            try {
                URL url = new URL(link);
                InputStream is = url.openConnection().getInputStream();
                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String res) {

        }
    }
}
