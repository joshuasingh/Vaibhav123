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
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class search_product extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    GoogleMap mgooglemap;
    Location mLastlocation;
    LatLng locationpicked;
    GoogleApiClient mGoogleApiClient;
    private Button b1,b2;
    private EditText e1;
    private LocationManager locationManager;

    private TextView t1;
    private String product,state,country;
     private double radius;
    public Circle circle;
    TextView text;
    NumberPicker np;
    String[] name;// Array Declared
   int i=0;
    List<Address> addresses;
    DataSnapshot ds;
    RecyclerView mrecycle;
    ArrayList<String> ar;
    ArrayList<LatLng> loc;
    int flag=0;
    ArrayAdapter<String> arrayAdapter;
    ListView mlist;
    ArrayList<Marker> m;
    private LinearLayout layoutListView;
    private LinearLayout.LayoutParams lpListView;
    double latitude1,longitude1;
    String s1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (googleapiavail()) {
            Toast.makeText(this, "working fine", Toast.LENGTH_LONG).show();
            setContentView(R.layout.activity_search_product);
            Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
                    myHandaling(paramThread, paramThrowable);
                }
            });

            initmap();
           /* layoutListView = (LinearLayout) findViewById(R.id.lay1);

            lpListView = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);

            lpListView.weight = 20;*/

            name = new String[20];
            b1 = (Button) findViewById(R.id.b1);
            e1 = (EditText) findViewById(R.id.e1);

            ar=new ArrayList<>();
            loc=new ArrayList<>();
            m=new ArrayList<>();
            mlist=(ListView) findViewById(R.id.l1);



        } else {
            //no google map

        }

        text = (TextView) findViewById(R.id.text);




        t1 = (TextView) findViewById(R.id.t1);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                product = e1.getText().toString();

                if(TextUtils.isEmpty(product)){
                    Toast.makeText(search_product.this,"enter a category you want to search for",Toast.LENGTH_LONG).show();
                }
               else {
                    getproduct();
                }

            }
        });


        b2 = (Button) findViewById(R.id.b2);

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentlocation();

            }
        });




        np=(NumberPicker) findViewById(R.id.np);




        np.setMinValue(1);
        //Specify the maximum value/number of NumberPicker
        np.setMaxValue(10);

        //Gets whether the selector wheel wraps when reaching the min/max value.
        np.setWrapSelectorWheel(true);


            //Set a value change listener for NumberPicker
            np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    //Display the newly selected number from picker
                    try{
                    circle.setRadius(newVal * 1000);
                    radius = newVal;}
                    catch (Exception e) {
                        Toast.makeText(search_product.this,"point of center not selected",Toast.LENGTH_LONG).show();


                    }

                }
            });



        DatabaseReference ref45= FirebaseDatabase.getInstance().getReference().child("global store info");

        ref45.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ds=dataSnapshot;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int y=m.size();
                if(y!=0){
                    for(int k=0;k<y;k++){
                        m.get(k).remove();
                    }
                }
                if(m1!=null){
                    m1.remove();
                }

                LatLng l1=loc.get(i);
                gotolocation(l1.latitude,l1.longitude,16);
                MarkerOptions opt = new MarkerOptions().title(ar.get(i)).position(new LatLng(l1.latitude,l1.longitude));
                m1=mgooglemap.addMarker(opt);
            }
        });
        String s=getIntent().getExtras().getString("body");
        s1=getIntent().getExtras().getString("notify");

        String[] latLng = s.split(",");
             latitude1 = Double.parseDouble(latLng[0]);
             longitude1 = Double.parseDouble(latLng[1]);






    }


    Marker m1;

    public void myHandaling(Thread paramThread, Throwable paramThrowable){
        Log.e("Alert","Lets See if it Works !!!" +"paramThread:::" +paramThread +"paramThrowable:::" +paramThrowable);
        Toast.makeText(search_product.this, "Alert uncaughtException111",Toast.LENGTH_LONG).show();
        Intent in =new Intent(search_product.this,search_product.class);
        startActivity(in);
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }










        private void getproduct(){
         try {
             try {
                 Geocoder geocoder = new Geocoder(search_product.this, Locale.getDefault());
                 addresses = geocoder.getFromLocation(mLastlocation.getLatitude(), mLastlocation.getLongitude(), 1);// Here 1 represent max location result to returned, by documents it recommended 1 to 5
             } catch (IOException e) {
                 e.printStackTrace();
             }

             String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
             String city = addresses.get(0).getLocality();
             state = addresses.get(0).getAdminArea();
             country = addresses.get(0).getCountryName();
             String postalCode = addresses.get(0).getPostalCode();
             String knownName = addresses.get(0).getFeatureName();


             //clear listview and marker
              if(arrayAdapter!=null) {
                  arrayAdapter.clear();
                  arrayAdapter.notifyDataSetChanged();
              }

             if(m.size()!=0){
                 int y=m.size();
                 if(y!=0){
                     for(int k=0;k<y;k++){
                         m.get(k).remove();
                     }
                 }

             }




             String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
             DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("global store info").child(country).child(state);
             GeoFire geofire = new GeoFire(ref);
             GeoQuery geoQuery = geofire.queryAtLocation(new GeoLocation(mLastlocation.getLatitude(), mLastlocation.getLongitude()), radius);
             geoQuery.removeAllListeners();

             geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {


                 @Override//when the product is found
                 public void onKeyEntered(String key, GeoLocation location) {

                     for (DataSnapshot datasnap : ds.child(country).child(state).child(key).child("category list").getChildren()) {

                         String name = datasnap.child("category").getValue(String.class);



                         if (name.equals(product)) {
                             String nam = ds.child(country).child(state).child(key).child("store_name").getValue(String.class);
                             double lat = ds.child(country).child(state).child(key).child("l").child("0").getValue(double.class);
                             double lng = ds.child(country).child(state).child(key).child("l").child("1").getValue(double.class);
                             LatLng l1 = new LatLng(lat, lng);


                             ar.add(nam);
                             loc.add(l1);
                             MarkerOptions opt = new MarkerOptions().position(new LatLng(location.latitude, location.longitude));
                             m.add(mgooglemap.addMarker(opt));


                         }
                     }
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

                     //}
                     flag = 1;
                     listgenerate();
                 }

                 @Override
                 public void onGeoQueryError(DatabaseError error) {

                 }
             });
         } catch (Exception e){
             Toast.makeText(search_product.this,"product not found",Toast.LENGTH_LONG).show();
         }

    }


    private void listgenerate() {
      /*  if (lpListView.weight == 0) {



            lpListView.weight = 20;




        } else {

            lpListView.weight = 0;


        }

        layoutListView.setLayoutParams(lpListView);*/

        arrayAdapter =
                new ArrayAdapter<String>(search_product.this,android.R.layout.simple_list_item_1,ar);
        mlist.setAdapter(arrayAdapter);

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


        mgooglemap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {




          //setting up main center point
            @Override
            public void onMapClick(LatLng point) {
                // TODO Auto-generated method stub

                if(locationpicked_marker!=null){
                    locationpicked_marker.remove();
                }
                if(circle!=null){
                    circle.remove();
                }
                MarkerOptions opt = new MarkerOptions().position(new LatLng(point.latitude,point.longitude));
                locationpicked_marker=mgooglemap.addMarker(opt);
                circle=drawcircle(point.latitude,point.longitude);



                //DatabaseReference ref1= FirebaseDatabase.getInstance().getReference("user").child("customer demands").child(uid).child("l");
                //ref.push().setValue(t1);

            }
        });

    }


    Marker locationpicked_marker;


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

    }

    public void putmarker(double lat, double lng) {
        MarkerOptions opt = new MarkerOptions().position(new LatLng(lat, lng));
        mgooglemap.addMarker(opt);

    }



    public void currentlocation() {
         if(mLastlocation!=null) {
             gotolocation(mLastlocation.getLatitude(), mLastlocation.getLongitude(), 15);
         }
         else{
             Toast.makeText(this,"location not found,Please click again",Toast.LENGTH_LONG).show();

         }

    }

    public  Circle drawcircle(double l11,double l12){

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
        if(s1.equals("start")){
            LatLng latlng = new LatLng( longitude1,latitude1);
            mgooglemap.moveCamera(CameraUpdateFactory.newLatLng(latlng));//camera moves with the user
            mgooglemap.animateCamera(CameraUpdateFactory.zoomTo(16));
            MarkerOptions opt = new MarkerOptions().position(new LatLng(longitude1,latitude1));
            mgooglemap.addMarker(opt);
        }

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
