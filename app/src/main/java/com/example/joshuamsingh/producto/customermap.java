package com.example.joshuamsingh.producto;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;

public class customermap extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    GoogleMap mgooglemap;
    Location mLastlocation, location;
    GoogleApiClient mGoogleApiClient;
    private Button b1, b2;
    private EditText e1;
    private LocationManager locationManager;
    Marker marker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (googleapiavail()) {
            Toast.makeText(this, "working fine", Toast.LENGTH_LONG).show();
            setContentView(R.layout.activity_customermap);
            initmap();
            b1 = (Button) findViewById(R.id.b1);
         b1.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 currentlocation();
             }
         });


        } else {
            //no google map

        }



    }


    private void initmap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapfragment);
        mapFragment.getMapAsync(this);

    }

    public boolean googleapiavail() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isavilable = api.isGooglePlayServicesAvailable(this);
        if (isavilable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isavilable)) {
            Dialog dialog = api.getErrorDialog(this, isavilable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "cant connect to play services", Toast.LENGTH_LONG).show();

        }
        return false;
    }


    public void geolocate(View view) throws IOException {
        e1 = (EditText) findViewById(R.id.e1);
        String location = e1.getText().toString();

        Geocoder gc = new Geocoder(this);//changes string to latitude and longitude
        List<Address> list = gc.getFromLocationName(location, 1);//get list of matching addresses
        Address address = list.get(0);
        String locality = address.getLocality();
        Toast.makeText(this, locality, Toast.LENGTH_LONG).show();
        double lat = address.getLatitude();
        double lng = address.getLongitude();
        gotolocation(lat, lng, 15);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mgooglemap = googleMap;



        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mgooglemap.setMyLocationEnabled(true);


        mGoogleApiClient = new GoogleApiClient.Builder(this).
                addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);




        //  tagging a product
          mgooglemap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                // TODO Auto-generated method stub
                if(marker!=null){
                    marker.remove();
                }

                MarkerOptions opt=new MarkerOptions().position(new LatLng(point.latitude,point.longitude));
                marker=mgooglemap.addMarker(opt);
                Intent i = new Intent(customermap.this,tag_info.class);
                double lat1=point.latitude;
                String lat2 = Double.toString(lat1);

                double long1=point.longitude;
                String long2 = Double.toString(long1);


                i.putExtra("latitude",lat2);
                i.putExtra("longitude",long2);
                startActivity(i);



                //DatabaseReference ref1= FirebaseDatabase.getInstance().getReference("user").child("customer demands").child(uid).child("l");
                //ref.push().setValue(t1);

            }
        });

    }


    //display menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //functionality of map menu

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.mapTypeNone:
                mgooglemap.setMapType(GoogleMap.MAP_TYPE_NONE);
                break;
            case R.id.mapTypeNormal:
                mgooglemap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.mapTypeSatellite:
                mgooglemap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.mapTypeTerrain:
                mgooglemap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.mapTypeHybrid:
                mgooglemap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void gotolocation(double lat, double lng, float zoom) {

        LatLng latlng = new LatLng(lat, lng);
        mgooglemap.moveCamera(CameraUpdateFactory.newLatLng(latlng));//camera moves with the user
        mgooglemap.animateCamera(CameraUpdateFactory.zoomTo(zoom));
        putmarker(lat, lng);
    }

    public void putmarker(double lat, double lng) {
        MarkerOptions opt = new MarkerOptions().position(new LatLng(lat, lng));
        mgooglemap.addMarker(opt);

    }



        public void currentlocation() {
            if(mLastlocation!=null) {
                gotolocation(mLastlocation.getLatitude(), mLastlocation.getLongitude(), 15);
                putmarker(mLastlocation.getLatitude(), mLastlocation.getLongitude());

            }
            else{
                Toast.makeText(this,"searching click one more time",Toast.LENGTH_LONG).show();

            }

        }



    @Override
    public void onLocationChanged(Location location) {

        mLastlocation =location;
    }

    LocationRequest mLocationrequest;

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationrequest = LocationRequest.create();
        mLocationrequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationrequest.setInterval(1000);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationrequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
