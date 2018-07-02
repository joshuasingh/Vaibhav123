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
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class represented extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    GoogleMap mgooglemap;
    Location mLastlocation, location;
    LatLng curloc;
    GoogleApiClient mGoogleApiClient;
    private Button b1,b2,gt,p;
    private EditText e1;
    private LocationManager locationManager;
    LatLng locationpicked;
    private TextView t1;
    private String product;
    private double radius;
    public Circle circle;
    TextView text;
    NumberPicker np;
    int i=0;
    String state,country;
    ArrayList<Marker> m;
    ArrayList<String> iden,category;
    DatabaseReference ref1;
    String[] product_name;
    int[] frequency;
    arraypasser a1;
    DataSnapshot ds;
    List<Address> addresses;
    View b,d;

    private DrawerLayout mdrawer;
    private ActionBarDrawerToggle mtoggle;
    private NavigationView nav;


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


        //navigation drawer
        nav=(NavigationView) findViewById(R.id.nav);
        nav.setNavigationItemSelectedListener(this);
        mdrawer=(DrawerLayout) findViewById(R.id.drawer_layout);
        mtoggle=new ActionBarDrawerToggle(this,mdrawer,R.string.open,R.string.close);
        mdrawer.addDrawerListener(mtoggle);
        mtoggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





        m = new ArrayList<>();
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

            }
        });





            np = (NumberPicker) findViewById(R.id.np);


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
                   try {
                       int g=1000*newVal;

                       circle.setRadius((double)g);
                       radius = (double)newVal;
                   }catch (Exception e) {
                        Toast.makeText(represented.this,"point of center not selected",Toast.LENGTH_SHORT).show();


                    }
                }
            });



        ref1 = FirebaseDatabase.getInstance().getReference()
                .child("global demanded items");

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
              try {
                  Geocoder geocoder = new Geocoder(represented.this, Locale.getDefault());
                  addresses = geocoder.getFromLocation(mLastlocation.getLatitude(), mLastlocation.getLongitude(), 1);// Here 1 represent max location result to returned, by documents it recommended 1 to 5
              } catch (IOException e) {
                  e.printStackTrace();
              }


              if (m.size() != 0) {
                  int y = m.size();
                  if (y != 0) {
                      for (int k = 0; k < y; k++) {
                          m.get(k).remove();
                      }
                  }

              }

               state = addresses.get(0).getAdminArea();
               country = addresses.get(0).getCountryName();
              String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
              DatabaseReference ref = FirebaseDatabase.getInstance().
                      getReference().child("global demanded items").
                      child(country).child(state);
              GeoFire geofire = new GeoFire(ref);
              GeoQuery geoQuery = geofire.queryAtLocation(new GeoLocation(locationpicked.latitude,locationpicked.longitude), radius);
              geoQuery.removeAllListeners();

              geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {


                  @Override//when the product is found
                  public void onKeyEntered(String key, GeoLocation location) {


                      iden.add(key);
                      MarkerOptions opt = new MarkerOptions().position(new LatLng(location.latitude, location.longitude));

                      m.add(mgooglemap.addMarker(opt));

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
          catch (Exception e){
              Toast.makeText(this,"failure, try again",Toast.LENGTH_LONG).show();
          }

    }



    public void showdata()
    {
        info i1=new info();

        int g=iden.size();

       for(int i=0;i<g;i++) {
            i1.setcategory(ds.child(country).child(state).child(iden.get(i)).getValue(info.class).getcategory());
            category.add(i1.getcategory());
        }



        product_name=getResources().getStringArray(R.array.category);

        for(int i=0;i<product_name.length-1;i++) {
            String y = product_name[i+1];
            frequency[i] = Collections.frequency(category, y);
        }


        for (int i = 0; i < product_name.length - 1; i++)
        {
            int index = i;
            for (int j = i + 1; j <product_name.length-1; j++){
                if (frequency[j] > frequency[index]){
                    index = j;//searching for lowest index
                }
            }
            int smallerNumber = frequency[index];
            frequency[index] = frequency[i];
            frequency[i] = smallerNumber;


            String msmallerNumber = product_name[index+1];
            product_name[index+1] = product_name[i+1];
            product_name[i+1] = msmallerNumber;
        }



        Intent i=new Intent(this,piechart.class);
        i.putExtra("cat1",product_name[1]);
        i.putExtra("cat2",product_name[2]);
        i.putExtra("cat3",product_name[3]);
        i.putExtra("cat4",product_name[4]);
        i.putExtra("cat5",product_name[5]);
        i.putExtra("cat6",product_name[6]);
        i.putExtra("cat7",product_name[7]);
        i.putExtra("cat8",product_name[8]);


        i.putExtra("num1",Integer.toString(frequency[0]));
        i.putExtra("num2",Integer.toString(frequency[1]));
        i.putExtra("num3",Integer.toString(frequency[2]));
        i.putExtra("num4",Integer.toString(frequency[3]));
        i.putExtra("num5",Integer.toString(frequency[4]));
        i.putExtra("num6",Integer.toString(frequency[5]));
        i.putExtra("num7",Integer.toString(frequency[6]));
        i.putExtra("num8",Integer.toString(frequency[7]));

        startActivity(i);



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
                locationpicked=new LatLng(point.latitude,point.longitude);
                curloc=point;
                if(locationpicked_marker!=null){
                    locationpicked_marker.remove();
                }
                if(circle!=null){
                    circle.remove();
                }
                MarkerOptions opt = new MarkerOptions().position(new LatLng(point.latitude,point.longitude));
                locationpicked_marker=mgooglemap.addMarker(opt);
                circle=drawcircle(point.latitude,point.longitude);





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

        if(mtoggle.onOptionsItemSelected(item)){
            return true;
        }



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

        if(mLastlocation!=null && i==0){
            i=1;
            gotolocation(mLastlocation.getLatitude(),mLastlocation.getLongitude(),15);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();

        if(id==R.id.home1){
            startActivity(new Intent(represented.this,seller.class));
        }

        if(id==R.id.store1){
            startActivity(new Intent(represented.this,tag_a_store.class));
        }
        if(id==R.id.log1){
            FirebaseAuth.getInstance().signOut();

            new AsyncTask<Void,Void,Void>()
            {
                @Override
                protected Void doInBackground(Void... params)
                {
                    {
                        try
                        {
                            FirebaseInstanceId.getInstance().deleteInstanceId();
                        } catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    return null;
                }
                @Override
                protected void onPostExecute(Void result)
                {
                    //call your activity where you want to land after log out
                }
            }.execute();
            startActivity(new Intent(represented.this,MainActivity.class));
            finish();
        }

        mdrawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
