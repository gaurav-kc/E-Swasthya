package com.rgn.doctor;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.multidex.MultiDex;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements CurrentPatient.OnFragmentInteractionListener,History.OnFragmentInteractionListener,
PendingAppointments.OnFragmentInteractionListener,CurrentTreatments.OnFragmentInteractionListener,
Appointments.OnFragmentInteractionListener
{
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.child("Yolo").child("Yoloooo").child("YOlloooo").setValue(62);
        if(firebaseUser==null)
        {
            Intent i = new Intent(this,SignInActivity.class);
            startActivity(i);
            finish();
        }
        String token = FirebaseInstanceId.getInstance().getToken();
        if(token!=null) {
            Log.d("FCM Token", token);
        }
        final FrameLayout framelayout = (FrameLayout)findViewById(R.id.framelayout);
        final BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        final Fragment currentPatient = new CurrentPatient();
        final Fragment pendingAppointments = new PendingAppointments();
        final Fragment prescribeMedicines = new CurrentTreatments();
        final Fragment appointment = new Appointments();
        final Fragment history = new History();
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                int pos = bottomBar.findPositionForTabWithId(tabId);
                Toast.makeText(getApplicationContext(),Integer.toString(pos),Toast.LENGTH_SHORT).show();
                switch (pos)
                {
                    case 0:
                    {
                        FragmentManager fragmentManager=getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.framelayout,pendingAppointments).commit();
                    }
                    break;
                    case 1:
                    {
                        FragmentManager fragmentManager=getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.framelayout,currentPatient).commit();

                    }
                    break;
                    case 2:
                    {
                        FragmentManager fragmentManager=getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.framelayout,prescribeMedicines).commit();
                    }
                    break;
                    case 3:
                    {
                        FragmentManager fragmentManager=getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.framelayout,appointment).commit();
                    }
                    break;
                    case 4:
                    {
                        FragmentManager fragmentManager=getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.framelayout,history).commit();
                    }
                    break;
                }

            }
        });

        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void AppointmentsUpdate() {

    }

}
