package com.rgn.e_swasthya;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import eu.long1.spacetablayout.SpaceTabLayout;

public class MainActivity extends AppCompatActivity implements Appointments.OnFragmentInteractionListener,
        CurrentTreatments.OnFragmentInteractionListener,FindDoctor.OnFragmentInteractionListener,
        History.OnFragmentInteractionListener,CurrentDoctor.OnFragmentInteractionListener{
    SpaceTabLayout tabLayout;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser==null)
        {
            Intent i = new Intent(this,SignInActivity.class);
            startActivity(i);
            finish();
        }
        if(firebaseUser!=null) {
            Log.d("FCM Token", FirebaseInstanceId.getInstance().getToken());

        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new CurrentTreatments());
        fragmentList.add(new Appointments());
        fragmentList.add(new FindDoctor());
        fragmentList.add(new CurrentDoctor());
        fragmentList.add(new History());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (SpaceTabLayout) findViewById(R.id.spaceTabLayout);

        //we need the savedInstanceState to retrieve the position
        tabLayout.initialize(viewPager, getSupportFragmentManager(), fragmentList, savedInstanceState);
    }

    @Override
    public void HistoryUpdate() {
        Log.d("History","Yolo");
    }

    @Override
    public void ScanCodeUpdate() {
        Log.d("ScanCode","Yolo");
    }

    @Override
    public void FindDoctorUpdate() {
        Log.d("FindDoctor","Yolo");
    }

    @Override
    public void AppointmentsUpdate() {
        Log.d("Appointment","Yolo");
    }

    @Override
    public void CurrentTreatmentUpdate() {
        Log.d("CurrentTreatment","Yolo");
    }


}
