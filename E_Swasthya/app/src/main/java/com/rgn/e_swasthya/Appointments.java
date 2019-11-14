package com.rgn.e_swasthya;

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
import android.widget.TextView;
import android.widget.Toast;

import com.dexafree.materialList.card.Action;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.card.OnActionClickListener;
import com.dexafree.materialList.card.action.TextViewAction;
import com.dexafree.materialList.view.MaterialListView;
import com.google.android.gms.vision.text.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

import static android.content.Context.MODE_PRIVATE;

public class Appointments extends Fragment {
    private Context mContext;
    private MaterialListView mListView;
    private OnFragmentInteractionListener mListener;
    private DatabaseHelper databaseHelper;
    private final int DATABASE_VERSION = 1;
    private final String DATABASE_NAME="Doctor";
    private static final String SHAREDPREFERENCE = "SharedPref";
    private SharedPreferences sharedPreferences;
    private int patid;
    private String patname;
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
        View view = inflater.inflate(R.layout.fragment_appointments, container, false);
        // Bind the MaterialListView to a variable
        databaseHelper = new DatabaseHelper(getContext(),DATABASE_NAME,null,DATABASE_VERSION);
        insertAppointments();
        mListView = (MaterialListView) view.findViewById(R.id.material_listview_appointments);
        mListView.setItemAnimator(new SlideInLeftAnimator());
        mListView.getItemAnimator().setAddDuration(300);
        mListView.getItemAnimator().setRemoveDuration(300);
        sharedPreferences = getActivity().getSharedPreferences(SHAREDPREFERENCE,MODE_PRIVATE);
        patid = sharedPreferences.getInt("Pat_id",0);
        patname = sharedPreferences.getString("Name",null);
        showCards();
        return view;
    }


    private void showCards(){
        Cursor cursor = databaseHelper.getAllA();
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
        final GetterSetterAppointments gettersetter = databaseHelper.getDetailsA(tag);
        mListView.getAdapter().add(new Card.Builder(mContext)
                .setTag(tag)
                .withProvider(new CardProvider())
                .setLayout(R.layout.appointments_card_view)
                .setTitle(title)
                .setDividerVisible(true)
                .addAction(R.id.A_cancel_app, new TextViewAction(mContext)
                        .setListener(new OnActionClickListener() {
                            @Override
                            public void onActionClicked(View view, Card card) {
                                int pos = Integer.parseInt(card.getTag().toString());
                                onCancelAppointment(pos,gettersetter,card);
                            }
                        }))
                .addAction(R.id.A_show_app_time, new Action(mContext) {
                    @Override
                    protected void onRender(@NonNull View view, @NonNull Card card) {
                        int pos=Integer.parseInt(card.getTag().toString());
                        final TextView textView = (TextView)view.findViewById(R.id.A_show_app_time);
                        textView.setText(gettersetter.getDate()+"  "+gettersetter.getHr()+":"+gettersetter.getMin());
                    }
                })
                .addAction(R.id.A_show_map, new TextViewAction(mContext)
                        .setListener(new OnActionClickListener() {
                            @Override
                            public void onActionClicked(View view, Card card) {
                                int pos = Integer.parseInt(card.getTag().toString());
                                onShowMap(pos,gettersetter);
                            }
                        }))
                .addAction(R.id.A_Show_info, new TextViewAction(mContext)
                        .setListener(new OnActionClickListener() {
                            @Override
                            public void onActionClicked(View view, Card card) {
                                int pos = Integer.parseInt(card.getTag().toString());
                                onShowInfo(pos,gettersetter);
                            }
                        }))
                .endConfig()
                .build());
    }

    private void onShowInfo(int pos,GetterSetterAppointments gettersetter) {
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(getContext());
        alt_bld.setTitle("Additional Information")
                .setMessage("Doctor type : "+gettersetter.getType()+" \nDoctor Rating : "+gettersetter.getRating())
                .setCancelable(true);
        alt_bld.show();
    }


    private void onShowMap(int pos,GetterSetterAppointments gettersetter) {
        Intent i = new Intent(getContext(),ShowPathBetweenPoints.class);
        i.putExtra("lat",gettersetter.getLatitude());
        i.putExtra("lon",gettersetter.getLongitude());
        i.putExtra("doc_name", gettersetter.getDoc_name());
        i.putExtra("doc_type",gettersetter.getType());
        i.putExtra("doc_rating", gettersetter.getRating());
        startActivity(i);
    }


    private void onCancelAppointment(final int pos, GetterSetterAppointments gettersetter, final Card card){
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(getContext());
        alt_bld.setTitle("Cancel Appointment?")
                .setMessage("The scheduled appointment will be cancelled and doctor will be notified")
                .setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String link = "http://gauravkc.pe.hu/appointment_canceled_by_patient.php?"+
                                "pat_id="+patid+"&"+
                                "doc_id="+pos+"&"+
                                "pat_name="+patname;
                        card.setDismissible(true);
                        card.dismiss();
                        new DownloadRawData().execute(link);
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
        ;
        alt_bld.show();
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
    private void insertAppointments() {
        GetterSetterAppointments getter = new GetterSetterAppointments();
        getter.setDoc_id(5);
        getter.setDoc_name("Masina Hospital\nDr. Sayali Kulkarni");
        getter.setLatitude(18.974927);
        getter.setFcm_key("hbfhbhjdhahdghasd");
        getter.setLongitude(72.835708);
        getter.setRating("4");
        getter.setType("Pediatrician");
        getter.setDate("2017-05-03");
        getter.setHr(18);
        getter.setMin(15);
        databaseHelper.insertinA(getter);
        GetterSetterAppointments getter1 = new GetterSetterAppointments();
        getter1.setDoc_id(6);
        getter1.setDoc_name("Kasturba Hospital\nDr. Meena Khatele");
        getter1.setLatitude(18.984241);
        getter1.setFcm_key("hbfhbhjdhahdghasd");
        getter1.setLongitude(72.830021);
        getter1.setRating("3.5");
        getter1.setType("Gynaecologist");
        getter1.setDate("2017-05-13");
        getter1.setHr(10);
        getter1.setMin(30);
        databaseHelper.insertinA(getter1);
        GetterSetterAppointments getter2 = new GetterSetterAppointments();
        getter2.setDoc_id(7);
        getter2.setDoc_name("Mamta Clinic\nDr. Mamta Mayekar");
        getter2.setLatitude(18.957591);
        getter2.setFcm_key("hbfhbhjdhahdghasd");
        getter2.setLongitude(72.829227);
        getter2.setRating("4.5");
        getter2.setType("General Physician");
        getter2.setDate("2017-05-19");
        getter2.setHr(11);
        getter2.setMin(45);
        databaseHelper.insertinA(getter2);
    }
}
