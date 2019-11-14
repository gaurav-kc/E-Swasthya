package com.rgn.doctor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener
,LocationListener{

    private static final int MY_PERMISSIONS_REQUEST_GET_LOCATION = 9001;
    private EditText name,address,dob,mobile,type;
    private String TAG="MainActivity";
    private SignInButton googlesignin;
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mFirebaseAuth;
    private LocationManager locationManager;
    private String lat="25",lon="65";
    private ProgressBar progressbar;
    private int docid;
    private DatabaseHelper databaseHelper;
    private RadioButton male,female;
    private final int DATABASE_VERSION = 1;
    private final String DATABASE_NAME="Doctor";

    private static final String SHAREDPREFERENCE = "SharedPref";
    private static final String NAME = "Name";
    private static final String ADDRESS ="Address";
    private static final String DOCID = "Doc_id";
    private static final String DOB = "dob";
    private static final String GENDER = "Gender";

    private String sname;
    private String sadd;
    private String smobile;
    private String sdob;
    private String stype;
    private SharedPreferences sharedPreferences=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        googlesignin = (SignInButton)findViewById(R.id.signinbutton);
        googlesignin.setOnClickListener(this);
        name = (EditText)findViewById(R.id.docname);
        address = (EditText)findViewById(R.id.docaddress);
        dob = (EditText)findViewById(R.id.docdob);
        mobile = (EditText)findViewById(R.id.docmob);
        type = (EditText)findViewById(R.id.doctype);
        male = (RadioButton)findViewById(R.id.radmale);
        female = (RadioButton)findViewById(R.id.radfemale);
        male.setOnClickListener(this);
        female.setOnClickListener(this);
        sharedPreferences = getSharedPreferences(SHAREDPREFERENCE,MODE_PRIVATE);
        databaseHelper = new DatabaseHelper(getApplicationContext(),DATABASE_NAME,null,DATABASE_VERSION);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_CONTACTS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_GET_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        // Initialize FirebaseAuth
        mFirebaseAuth = FirebaseAuth.getInstance();
        /*locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), false
        ));

        // Initialize the location fields
        if (location != null) {
            onLocationChanged(location);
        } else {
            Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
        }*/

    }
    private void registerUserOnServer(String name,String address,String mobile,String dob,
                                      String type,String email,String lat,String lon){
        String link="http://gauravkc.pe.hu/add_doctor.php?";
        link+="doctor_name="+name+"&";
        link+="doctor_address="+address+"&";
        link+="doctor_mobile="+mobile+"&";
        link+="doctor_dob="+dob+"&";
        link+="doctor_latitude="+lat+"&";
        link+="doctor_longitude="+lon+"&";
        link+="doctor_type="+type+"&";
        link+="doctor_email="+email+"&";
        link+="doctor_fcmid="+ FirebaseInstanceId.getInstance().getToken();
        new registerUser().execute(link);

        /*($_GET['doctor_name']) && isset($_GET['doctor_address']) && isset($_GET['doctor_mobile']) &&
  isset($_GET['doctor_dob']) && isset($_GET['doctor_latitude']) && isset($_GET['doctor_longitude']) &&
  isset($_GET['doctor_type']) && isset($_GET['doctor_email']) && isset($_GET['doctor_fcmid'])*/
    }

    @Override
    public void onLocationChanged(Location location) {
        /*lat = String.valueOf(location.getLatitude());
        lon = String.valueOf(location.getLongitude());*/
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private class registerUser extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                parse(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        private void parse(String s) throws JSONException {
            Log.d("Signin",s);
            JSONObject jsonObject = new JSONObject(s);
            docid = jsonObject.getInt("DOC_ID");
            performLocalRegistration(docid,sname,sadd,sdob);
        }
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.signinbutton: signIn();
                break;
        }
    }

    private void setThisAsHome() {
    }


    private void handleFirebaseAuthResult(AuthResult authResult) {
        if (authResult != null) {
            // Welcome the user
            FirebaseUser user = authResult.getUser();
            Toast.makeText(this, "Welcome " + user.getEmail(), Toast.LENGTH_SHORT).show();

            // Go back to the main activity
            startActivity(new Intent(this, MainActivity.class));
        }
    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed
                Log.e(TAG, "Google Sign In failed.");
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            startActivity(new Intent(SignInActivity.this, MainActivity.class));
                            String token = FirebaseInstanceId.getInstance().getToken();
                             sname = name.getText().toString();
                             sadd = address.getText().toString();
                             smobile = mobile.getText().toString();
                             sdob = dob.getText().toString();
                             stype = type.getText().toString();
                            if(!sname.equals("") && !sadd.equals("") && !smobile.equals("") &&
                                    !sdob.equals("") && !stype.equals("") && mFirebaseAuth.getCurrentUser()!=null && token!=null)
                            {
                                registerUserOnServer(sname,
                                        sadd,
                                        smobile,
                                        sdob,
                                        stype,
                                        mFirebaseAuth.getCurrentUser().getEmail(),
                                        lat,lon);
                            }
                            finish();
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }
    private void performLocalRegistration(int id,String name,String address,String dob){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(DOCID,id);
        editor.putString(NAME,name);
        editor.putString(ADDRESS,address);
        editor.putString(DOB,dob);
        if(male.isChecked())
            editor.putString(GENDER,"Male");
        else{
            if(female.isChecked())
            {
                editor.putString(GENDER,"Female");
            }
        }
        editor.apply();
        insertDefaultEntires();
        Log.d("docid",String.valueOf(sharedPreferences.getInt(DOCID,0)));
    }

    private void insertDefaultEntires(){
        insertAppointmentEntries();
        insertPendingAppointments();
        insertCurrentTreatments();
        insertHistory();
    }
    private void insertAppointmentEntries(){
        GetterSetterAppointments getterSetterAppointments = new GetterSetterAppointments();
        getterSetterAppointments.setPat_id(2000);
        getterSetterAppointments.setPat_name("Mr.Ayush Kharche");
        getterSetterAppointments.setPat_fcm("yolo");
        getterSetterAppointments.setPat_ss("000000000");
        getterSetterAppointments.setPat_app_date("2017-05-04");
        getterSetterAppointments.setPat_app_hr(10);
        getterSetterAppointments.setPat_app_min(20);
        GetterSetterAppointments getterSetterAppointments1 = new GetterSetterAppointments();
        getterSetterAppointments1.setPat_id(2001);
        getterSetterAppointments1.setPat_name("Mr.Param Patil");
        getterSetterAppointments1.setPat_fcm("yolo");
        getterSetterAppointments1.setPat_ss("0000000000");
        getterSetterAppointments1.setPat_app_date("2017-05-04");
        getterSetterAppointments1.setPat_app_hr(10);
        getterSetterAppointments1.setPat_app_min(30);
        GetterSetterAppointments getterSetterAppointments2 = new GetterSetterAppointments();
        getterSetterAppointments2.setPat_id(2002);
        getterSetterAppointments2.setPat_name("Mr.Udhvarit Jawale");
        getterSetterAppointments2.setPat_fcm("yolo");
        getterSetterAppointments2.setPat_ss("0000000000");
        getterSetterAppointments2.setPat_app_date("2017-05-25");
        getterSetterAppointments2.setPat_app_hr(15);
        getterSetterAppointments2.setPat_app_min(25);
        GetterSetterAppointments getterSetterAppointments3 = new GetterSetterAppointments();
        getterSetterAppointments3.setPat_id(2003);
        getterSetterAppointments3.setPat_name("Mr.Rudrani Jawale");
        getterSetterAppointments3.setPat_fcm("yolo");
        getterSetterAppointments3.setPat_ss("0000000000");
        getterSetterAppointments3.setPat_app_date("2017-06-25");
        getterSetterAppointments3.setPat_app_hr(8);
        getterSetterAppointments3.setPat_app_min(25);
        databaseHelper.insertInA(getterSetterAppointments1);
        databaseHelper.insertInA(getterSetterAppointments);
        databaseHelper.insertInA(getterSetterAppointments2);
        databaseHelper.insertInA(getterSetterAppointments3);
    }
    private void insertPendingAppointments(){
        databaseHelper.insertsomeentriesinPA();
    }
    private void insertCurrentTreatments(){
        databaseHelper.insertsomeentriesinCT();
    }
    private void insertHistory(){
        databaseHelper.insertsomeentriesinH();
    }
}
