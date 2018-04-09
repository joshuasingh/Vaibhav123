package com.example.joshuamsingh.producto;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class represented extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    GoogleMap mgooglemap;
    Location mLastlocation, location;
    GoogleApiClient mGoogleApiClient;
    private Button b1,b2,gt,p;
    private EditText e1;
    private LocationManager locationManager;

    private TextView t1;
    private String product;
    private double radius;
    public Circle circle;
    TextView text;
    NumberPicker np;
    int i=0;
    ArrayList<String> iden,category;
    DatabaseReference ref1;
    String[] product_name;
    int[] frequency;
    arraypasser a1;
    DataSnapshot ds;
    List<Address> addresses;
    View b,d;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (googleapiavail()) {
            Toast.makeText(this, "working fine", Toast.LENGTH_LONG).show();
            setContentView(R.layout.activity_represented);
            iden=new ArrayList<>();
            category=new ArrayList<>();
            initmap();
            a1=new arraypasser();
            b1 = (Button) findViewById(R.id.b1);
             b = findViewById(R.id.b1);
            b.setVisibility(View.INVISIBLE);
            e1 = (EditText) findViewById(R.id.e1);
            frequency=new int[30];


        } else {
            //no google map

        }

        text = (TextView) findViewById(R.id.text);




        t1 = (TextView) findViewById(R.id.t1);



        p=(Button) findViewById(R.id.p);
        p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showdata();
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getproduct();

            }
        });


        b2 = (Button) findViewById(R.id.b2);

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentlocation();
                b.setVisibility(View.VISIBLE);
            }
        });





            np = (NumberPicker) findViewById(R.id.np);


            np.setMinValue(0);
            //Specify the maximum value/number of NumberPicker
            np.setMaxValue(10);

            //Gets whether the selector wheel wraps when reaching the min/max value.
            np.setWrapSelectorWheel(true);

            //Set a value change listener for NumberPicker
            np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    //Display the newly selected number from picker
                    circle.setRadius(newVal * 1000);
                    radius = newVal;
                }
            });



        ref1 = FirebaseDatabase.getInstance().getReference()
                .child("global demanded items").child("India").child("Uttar Pradesh");

        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
             ds=dataSnapshot;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }







    private void getproduct(){

        try {
            Geocoder geocoder = new Geocoder(represented.this, Locale.getDefault());
            addresses = geocoder.getFromLocation(mLastlocation.getLatitude(),mLastlocation.getLongitude(), 1);// Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref= FirebaseDatabase.getInstance().
                getReference().child("global demanded items").
                child(country).child(state);
        GeoFire geofire=new GeoFire(ref);
        GeoQuery geoQuery=geofire.queryAtLocation(new GeoLocation(mLastlocation.getLatitude(),mLastlocation.getLongitude()),radius);
        geoQuery.removeAllListeners();

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {




            @Override//when the product is found
            public void onKeyEntered(String key, GeoLocation location) {


                iden.add(key);
                MarkerOptions opt = new MarkerOptions().position(new LatLng(location.latitude, location.longitude));
                mgooglemap.addMarker(opt);

            }

            @Override
            public void onKeyExited(String key) {



            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override//when all has been searched for a radius
            public void onGeoQueryReady() {
                //if(!productfound) {
                // radius++;
                //getproduct();
                //}

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });



    }



    public void showdata()
    {
        info i1=new info();

        int g=iden.size();

       for(int i=0;i<g;i++) {
            i1.setcategory(ds.child(iden.get(i)).getValue(info.class).getcategory());
            category.add(i1.getcategory());
        }



        product_name=getResources().getStringArray(R.array.category);

        for(int i=0;i<product_name.length;i++) {
            String y = product_name[i];
            frequency[i] = Collections.frequency(category, y);
        }


        setupchart();
    }






    private void setupchart() {

        List<PieEntry> pieEntries=new ArrayList<>();
        for(int i=0;i<product_name.length;i++){
            pieEntries.add(new PieEntry(frequency[i],product_name[i]));
        }

        PieDataSet dataset=new PieDataSet(pieEntries,"rainfall");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data=new PieData(dataset);


        PieChart chart=(PieChart) findViewById(R.id.piechart);
        chart.setData(data);
        chart.invalidate();
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
            circle=drawcircle(mLastlocation.getLatitude(),mLastlocation.getLongitude());
        }
        else{
            Toast.makeText(this,"searching click one more time",Toast.LENGTH_LONG).show();

        }

    }

    public Circle drawcircle(double l11, double l12){

        CircleOptions options=new CircleOptions()
                .center(new LatLng(l11,l12))
                .radius(1000)
                .fillColor(0x33FF0000)
                .strokeColor(Color.BLUE)
                .strokeWidth(3);

        return mgooglemap.addCircle(options);


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
