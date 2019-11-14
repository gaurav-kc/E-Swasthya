package com.rgn.e_swasthya;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import Modules.DirectionFinder;
import Modules.DirectionFinderListener;
import Modules.Route;

public class ShowPathBetweenPoints extends FragmentActivity implements OnMapReadyCallback, DirectionFinderListener,
        android.location.LocationListener,GoogleMap.OnMarkerClickListener{

    private GoogleMap mMap;
    private Button btnFindPath;
    private EditText etOrigin;
    private EditText etDestination;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    private String CURRENT_LOCATION="Current Location";
    private LocationManager locationManager;
    private double lat, lng;
    double ReceivedLat,ReceivedLon;
    String Receivedname,Receivedtype,Receivedrating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_path);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        ReceivedLat = intent.getDoubleExtra("lat",18.974927);
        ReceivedLon = intent.getDoubleExtra("lon",72.835708);
        Receivedname = intent.getStringExtra("doc_name");
        Receivedtype = intent.getStringExtra("doc_type");
        Receivedrating = intent.getStringExtra("doc_rating");

        /*Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), false));

        // Initialize the location fields
        if (location != null) {
            onLocationChanged(location);
        } else {
            Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
        }*/
        sendRequest();
    }

    private void sendRequest() {
        String origin = "18.968190,72.831309";
        String destination = String.valueOf(ReceivedLat)+","+String.valueOf(ReceivedLon);

        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng hcmus = new LatLng(18.968190,72.831309);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hcmus, 18));
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.setOnMarkerClickListener(this);
        makeMarker();
        originMarkers.add(mMap.addMarker(new MarkerOptions()
                .title("Current Location")
                .position(hcmus)));
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
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
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            ((TextView) findViewById(R.id.tvDuration)).setText(route.distance.text);
            ((TextView) findViewById(R.id.tvDistance)).setText(route.duration.text);

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker())
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = (location.getLatitude());
        lng = (location.getLongitude());
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
    private void makeMarker() {
        Marker marker = mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.defaultMarker())
                .title(Receivedname)
                .snippet(Receivedrating+" Stars")
                .position(new LatLng(ReceivedLat,ReceivedLon))
        );
        marker.setTag(00+"::"+Receivedname+"::"+Receivedtype+"::"+Receivedrating);
    }
    @Override
    public boolean onMarkerClick(Marker marker) {

        String tag = marker.getTag().toString();
        if(tag.equals(CURRENT_LOCATION))
            return false;
        getDetailsFromTags getdetails = new getDetailsFromTags(tag);
        final String name=getdetails.getNamefromtag();
        final String rating=getdetails.getRating();
        final String type = getdetails.getTypefromtag();
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld
                .setTitle("Doctor Details")
                .setCancelable(true)
                .setMessage("Dr."+name+" \nType: "+type+"\nRating:"+rating+" Stars");
        ;
        alt_bld.show();
        return false;
    }
    private void showCurrentLocation(GoogleMap mMap){
        CameraPosition camera = new CameraPosition.Builder()
                .target(new LatLng(lat,lng))
                .zoom(14)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat,lng))
                .title("Current Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
        );
        marker.setTag(CURRENT_LOCATION);
    }
    class getDetailsFromTags{
        private String tag;
        private int id;
        private String name;
        private String rating;
        private String type;
        public getDetailsFromTags(String tag) {
            this.tag=tag;
            processtag();
        }
        public String getNamefromtag(){
            return name;
        }
        public String getRating(){
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
            type = tag.substring(temp2+2,temp3);
            rating=tag.substring(temp3+2,tag.length());
        }
    }
}