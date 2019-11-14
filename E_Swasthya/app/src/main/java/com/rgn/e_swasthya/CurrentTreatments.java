package com.rgn.e_swasthya;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.card.OnActionClickListener;
import com.dexafree.materialList.card.action.TextViewAction;
import com.dexafree.materialList.view.MaterialListView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;


public class CurrentTreatments extends Fragment {
    private Context mContext;
    private MaterialListView mListView;
    private OnFragmentInteractionListener mListener;
    private DatabaseHelper databaseHelper;
    private final int DATABASE_VERSION = 1;
    private final String DATABASE_NAME = "Doctor";
    private FirebaseAuth firebaseauth;
    private FirebaseUser firebaseuser;
    private FirebaseDatabase firebasedatabase;
    private DatabaseReference databasereference;

    public CurrentTreatments() {
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
        mListener.CurrentTreatmentUpdate();
        mContext = getContext();
        firebaseauth = FirebaseAuth.getInstance();
        firebaseuser = firebaseauth.getCurrentUser();
        if (firebaseuser != null) {
            firebasedatabase = FirebaseDatabase.getInstance();
            databasereference = firebasedatabase.getReference();
        }
        databaseHelper = new DatabaseHelper(getContext(), DATABASE_NAME, null, DATABASE_VERSION);
        insertCurrentTreatments();
        View view = inflater.inflate(R.layout.fragment_current_treatments, container, false);
        // Bind the MaterialListView to a variable
        mListView = (MaterialListView) view.findViewById(R.id.material_listview_currenttreatments);
        mListView.setItemAnimator(new SlideInLeftAnimator());
        mListView.getItemAnimator().setAddDuration(300);
        mListView.getItemAnimator().setRemoveDuration(300);
        showCards();
        return view;
    }

    private void showCards() {
        Cursor cursor = databaseHelper.getAllCT();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            addCard(cursor.getString(1), cursor.getInt(0));
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
        void CurrentTreatmentUpdate();
    }

    @Override
    public void onDestroyView() {
        Log.d("D CurrentTreatment", "");
        super.onDestroyView();
    }

    private void addCard(String title, int tag) {
        final GetterSetterCurrentTreatments getterSetterCurrentTreatments = databaseHelper.getDetailsCT(tag);
        mListView.getAdapter().add(new Card.Builder(mContext)
                .setTag(tag)
                .withProvider(new CardProvider())
                .setLayout(R.layout.current_treatments_card_view)
                .setTitle(title)
                .setDividerVisible(true)
                .addAction(R.id.CT_message, new TextViewAction(mContext)
                        .setListener(new OnActionClickListener() {
                            @Override
                            public void onActionClicked(View view, Card card) {
                                String pos = card.getTag().toString();
                                Log.d("String pos is ",pos);
                                Log.d("Integer pos is ",String.valueOf(Integer.parseInt(pos)));
                                Intent i = new Intent(getContext(),FriendlyChatManager.class);
                                i.putExtra("Doc_id",pos);
                                startActivity(i);
                            }
                        }))
                .addAction(R.id.CT_medicines, new TextViewAction(mContext)
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
                                al.setTitle("Medicines");
                                al.setMessage("Crocin 1 tablet 3 times a day\nHonitus 10 ml 2 times a day");
                                al.show();
                            }
                        }))
                .endConfig()
                .build());
    }

    private void onMedicinesClick(int pos, GetterSetterCurrentTreatments getterSetterCurrentTreatments) {

    }

    private void onMessageClick(int pos, GetterSetterCurrentTreatments getterSetterCurrentTreatments) {

    }
    private void insertCurrentTreatments() {
        GetterSetterCurrentTreatments getter = new GetterSetterCurrentTreatments();
        getter.setDoc_id(88797);
        getter.setDuration(10);
        getter.setDoc_name("Dr. Milind Dhale");
        getter.setStart_date("2017-04-28");
        getter.setFcm_key("yolo");
        databaseHelper.insertinCT(getter);
        GetterSetterCurrentTreatments getter1 = new GetterSetterCurrentTreatments();
        getter1.setDoc_id(88798);
        getter1.setDuration(9);
        getter1.setDoc_name("Dr. Tukaram Patil");
        getter1.setStart_date("2017-04-25");
        getter1.setFcm_key("yolo");
        databaseHelper.insertinCT(getter1);
    }
}
