package com.rgn.doctor;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.dexafree.materialList.card.Action;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.card.OnActionClickListener;
import com.dexafree.materialList.card.action.TextViewAction;
import com.dexafree.materialList.view.MaterialListView;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

import static android.content.Context.MODE_PRIVATE;

public class PendingAppointments extends Fragment implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private DatabaseHelper db;
    private final int DATABASE_VERSION = 1;
    private final String DATABASE_NAME="Doctor";
    private Context mContext;
    private MaterialListView mListView;
    private OnFragmentInteractionListener mListener;
    private String newdate,newhr,newmin,datetime;
    private int currentpos;
    private Button button;
    private static final String SHAREDPREFERENCE = "SharedPref";
    private int docid;
    private SharedPreferences sharedPreferences;
    public PendingAppointments() {
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
        mContext = getContext();
        View view = inflater.inflate(R.layout.fragment_pending_appointments, container, false);
        // Bind the MaterialListView to a variable
        mListView = (MaterialListView) view.findViewById(R.id.material_listview_pending_appointments);
        mListView.setItemAnimator(new SlideInLeftAnimator());
        mListView.getItemAnimator().setAddDuration(300);
        mListView.getItemAnimator().setRemoveDuration(300);
        sharedPreferences = getActivity().getSharedPreferences(SHAREDPREFERENCE,MODE_PRIVATE);
        docid = sharedPreferences.getInt("Doc_id",0);
        db = new DatabaseHelper(getContext(),DATABASE_NAME, null, DATABASE_VERSION);
        showCards();
        return view;
    }

    private void showCards(){
        Cursor cursor = db.getAllPA();
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

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        newdate = String.valueOf(dayOfMonth)+"-"+
                String.valueOf(month)+"-"+
                String.valueOf(year);
        l("in ondateset, currentpos is "+getCurrentpos()+" newdate is "+newdate);
        db.updatedateinPA(newdate,getCurrentpos());
        getButton().setText(newdate);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        newhr = String.valueOf(hourOfDay);
        newmin = String.valueOf(minute);
        l("in ontimeset, currentpos is "+getCurrentpos()+" newhr is "+newhr+" newmin is "+newmin);
        db.updatehrinPA(hourOfDay,getCurrentpos());
        db.updatemininPA(minute,getCurrentpos());
        getButton().setText(newhr+":"+newmin);
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private void addCard(String title,int tag) {
        mListView.getAdapter().add(new Card.Builder(mContext)
                .setTag(tag)
                .withProvider(new CardProvider())
                .setLayout(R.layout.pending_appointments_card_view)
                .setTitle(title)
                .setDividerVisible(true)
                .addAction(R.id.PA_app_date, new Action(getContext()) {
                    @Override
                    protected void onRender(@NonNull View view, @NonNull Card card) {
                        final int currentpos = Integer.parseInt(card.getTag().toString());
                        GetterSetterPendingAppointments getterSetterPendingAppointments =
                                db.getDetailsPA(currentpos);
                        l(getterSetterPendingAppointments.getPatient_app_date()+"   "+getterSetterPendingAppointments.getPatient_start_hr()+"  "
                                +getterSetterPendingAppointments.getPatient_start_min());
                        final Button button = (Button)view.findViewById(R.id.PA_app_date);
                        button.setText(getterSetterPendingAppointments.getPatient_app_date());
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Calendar calendar = Calendar.getInstance();
                                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),PendingAppointments.this,
                                        calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                                datePickerDialog.show();
                                l("here");
                                setCurrentpos(currentpos);
                                setButton(button);
                            }
                        });
                    }
                })
                .addAction(R.id.PA_app_time, new Action(getContext()) {
                    @Override
                    protected void onRender(@NonNull View view, @NonNull Card card) {
                        final int currentpos = Integer.parseInt(card.getTag().toString());
                        GetterSetterPendingAppointments getterSetterPendingAppointments =
                                db.getDetailsPA(currentpos);
                        l(getterSetterPendingAppointments.getPatient_app_date()+"   "+getterSetterPendingAppointments.getPatient_start_hr()+"  "
                        +getterSetterPendingAppointments.getPatient_start_min());
                        final Button button = (Button)view.findViewById(R.id.PA_app_time);
                        button.setText(getterSetterPendingAppointments.getPatient_start_hr()+":"+getterSetterPendingAppointments.getPatient_start_min());
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Calendar calendar = Calendar.getInstance();
                                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),PendingAppointments.this,
                                        calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false);
                                timePickerDialog.show();
                                l("here");
                                setCurrentpos(currentpos);
                                setButton(button);
                            }
                        });
                    }
                })
                .addAction(R.id.PA_info_button, new TextViewAction(mContext)
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
                                al.setMessage("Symptoms Sent:\nWet Cough\nTiredness\nChest Pain");
                                al.show();
                            }
                        }))
                .addAction(R.id.PA_cancel_app, new TextViewAction(mContext)
                        .setListener(new OnActionClickListener() {
                            @Override
                            public void onActionClicked(View view, Card card) {
                                int pos = Integer.parseInt(card.getTag().toString());
                                card.setDismissible(true);
                                card.dismiss();
                            }
                        }))
                .addAction(R.id.PA_confirm_app,new TextViewAction(mContext)
                        .setListener(new OnActionClickListener() {
                            @Override
                            public void onActionClicked(View view, Card card) {
                                int pos = Integer.parseInt(card.getTag().toString());

                                card.setDismissible(true);
                                card.dismiss();
                            }
                        }))
                .endConfig()
                .build());
    }

    private void sendNotificationOfRejection(int pos,GetterSetterAppointments getter) {
        String link = "http://gauravkc.pe.hu/send_appointment_result_to_patient.php?"+
                "pat_id="+pos+"&"+
                "result=0&"+
                "doc_id="+docid+"&"+
                "hr="+getter.getPat_app_hr()+"&"+
                "min="+getter.getPat_app_min()+"&"+
                "date="+getter.getPat_app_date();
        new DownloadRawData().execute(link);

    }

    private void sendNotificationOfConfirmation(int pos, GetterSetterAppointments getter) {
        String link = "http://gauravkc.pe.hu/send_appointment_result_to_patient.php?"+
                "pat_id="+pos+"&"+
                "result=1&"+
                "doc_id="+docid+"&"+
                "hr="+getter.getPat_app_hr()+"&"+
                "min="+getter.getPat_app_min()+"&"+
                "date="+getter.getPat_app_date();
        new DownloadRawData().execute(link);
    }

    private void l(String message){
        Log.d("Pending appointments ",message);
    }

    public int getCurrentpos() {
        return currentpos;
    }

    public void setCurrentpos(int currentpos) {
        this.currentpos = currentpos;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
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
