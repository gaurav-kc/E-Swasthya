package com.rgn.e_swasthya;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import Modules.DirectionFinder;
import Modules.DirectionFinderListener;
import Modules.Route;


public class ShowDoctorsOnMap extends FragmentActivity implements OnMapReadyCallback,
        LocationListener,GoogleMap.OnMarkerClickListener {

    private static final int MY_PERMISSIONS_REQUEST_GET_LOCATION = 9001;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private double lat, lng;
    protected ProgressDialog pDialog;
    private String CURRENT_LOCATION="Current Location";
    private String symptomString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_maps_find_doctors);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent i = getIntent();
        symptomString = i.getStringExtra("Symptoms");
        // Define the criteria how to select the locatioin provider -> use
        // default
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_GET_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        doLocationStuff();
        new getLocations().execute();
    }

    private void doLocationStuff() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), false));

        // Initialize the location fields
        if (location != null) {
            onLocationChanged(location);
        } else {
            Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.setOnMarkerClickListener(this);
        showCurrentLocation(mMap);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }
   @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_GET_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permissions are granted", Toast.LENGTH_SHORT).show();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    doLocationStuff();

                } else {
                    Toast.makeText(this, "Permission Declined", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = (location.getLatitude());
        lng = (location.getLongitude());

    }
    private void showCurrentLocation(GoogleMap mMap){
        CameraPosition camera = new CameraPosition.Builder()
                .target(new LatLng(18.968535,72.831019))
                .zoom(14)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(18.968535,72.831019))
                .title("Current Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
        );
        marker.setTag(CURRENT_LOCATION);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String tag = marker.getTag().toString();
        if(tag.equals(CURRENT_LOCATION))
            return false;
        getDetailsFromTag getdetails = new getDetailsFromTag(tag);
        final int id = getdetails.getIdfromtag();
        final String name=getdetails.getNamefromtag();
        final int rating=getdetails.getRating();
        final String type = getdetails.getTypefromtag();
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld
                .setTitle("Send an appointment Request?")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent  i = new Intent(ShowDoctorsOnMap.this,sendNotification.class);
                        i.putExtra("Doc_id",id);
                        i.putExtra("Symptoms",symptomString);
                        startActivity(i);
                        //Sending notification code here
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                })
                .setMessage("Dr."+name+" \n"+type+"\n"+Integer.toString(rating)+" Stars");
        ;
        alt_bld.show();
        return false;
    }

    class getLocations extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String link = "http://gauravkc.pe.hu/get_nearby_doctors_using_preference_number.php?number=3&patient_longitude=73.130539&patient_latitude=19.240330";
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
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ShowDoctorsOnMap.this);
            pDialog.setMessage("Loading products. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                parseJSon(s);
                pDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        private void parseJSon(String data) throws JSONException {
            if(data==null)
                return;
            /*
            {"SUCCESS":1,"DATA":[{"DOC_ID":"5","DOC_LAT":"20.01174200","DOC_LON":"73.77319300",
            "DOC_NAME":"Tushar","DOC_RATING":"0","DOC_TYPE":"kl","DISTANCE":"109.03225003713236"},
            {"DOC_ID":"4","DOC_LAT":"20.91751100","DOC_LON":"74.78393600","DOC_NAME":"Sagar",
            "DOC_RATING":"0","DOC_TYPE":"kl","DISTANCE":"254.15248469882582"},
            {"DOC_ID":"3","DOC_LAT":"20.98932900","DOC_LON":"75.55297900","DOC_NAME":"Gaurav",
            "DOC_RATING":"0","DOC_TYPE":"kl","DISTANCE":"319.04476953137595"}]}
            */
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("DATA");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject j1 = jsonArray.getJSONObject(i);
                Locations locations = new Locations();
                locations.DISTANCE=j1.getDouble("DISTANCE");
                locations.DOC_ID=j1.getInt("DOC_ID");
                locations.DOC_LAT=j1.getDouble("DOC_LAT");
                locations.DOC_LON=j1.getDouble("DOC_LON");
                locations.DOC_RATING=j1.getInt("DOC_RATING");
                locations.DOC_NAME=j1.getString("DOC_NAME");
                locations.TYPE=j1.getString("DOC_TYPE");
                makeMarker(locations);
            }
        }
    }

    private void makeMarker(Locations locations) {
        Marker marker = mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.defaultMarker())
                .title(locations.DOC_NAME)
                .snippet(Integer.toString(locations.DOC_RATING)+" Stars")
                .position(new LatLng(locations.DOC_LAT,locations.DOC_LON))
        );
        marker.setTag(locations.DOC_ID+"::"+locations.DOC_NAME+"::"+locations.DOC_RATING+"::"+locations.TYPE);

    }

}
 class getDetailsFromTag{
    private String tag;
    private int id;
    private String name;
    private int rating;
     private String type;
    public getDetailsFromTag(String tag) {
        this.tag=tag;
        processtag();
    }
    public int getIdfromtag(){
        return id;
    }
    public String getNamefromtag(){
        return name;
    }
    public int getRating(){
        return rating;
    }
    public String getTypefromtag(){
        return type;
    }
    private void processtag(){
        String delimiter = "::";
        int temp=tag.indexOf(delimiter);
        String tid = tag.substring(0,temp);
        id = Integer.parseInt(tid);
        int temp2=tag.indexOf(delimiter,temp+2);
        name=tag.substring(temp+2,temp2);
        int temp3=tag.indexOf(delimiter,temp2+2);
        rating = Integer.parseInt(tag.substring(temp2+2,temp3));
        type=tag.substring(temp3+2,tag.length());
    }
}