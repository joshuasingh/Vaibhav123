package com.example.joshuamsingh.producto;

import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.joshuamsingh.producto.Model.MyResponse;
import com.example.joshuamsingh.producto.Model.Notification;
import com.example.joshuamsingh.producto.Model.Sender;
import com.example.joshuamsingh.producto.Remote.APIService;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class store_info extends AppCompatActivity {
    EditText e1;
    Spinner s1;
    ListView mlist;
    ArrayList<String> cat,tokenlist;
    ArrayAdapter<String> arrayAdapter;
    Button b1;
    DatabaseReference mref;
    String lat,lng;
    List<Address> addresses;
    Calendar calendar;
    String currentDate,t1;
    DataSnapshot ds;
    double latitude,longitude;
    String state,country;
    Button b2;
    APIService mservice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_info);

      e1=(EditText) findViewById(R.id.t1);
        s1=(Spinner) findViewById(R.id.s1);
        mlist=(ListView) findViewById(R.id.l1);
        cat=new ArrayList<>();
        tokenlist=new ArrayList<>();
        b1=(Button) findViewById(R.id.b1);
        b2=(Button) findViewById(R.id.b2);


        //getting client
        mservice=Common.getFCMClient();


        //get lat and long
        lat=getIntent().getExtras().getString("latitude1");
        lng=getIntent().getExtras().getString("longitude1");


        //get current date
        calendar= Calendar.getInstance();
        currentDate= DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

       //datasnapshot

        DatabaseReference ref45= FirebaseDatabase.getInstance().getReference().child("global demanded items");

        ref45.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ds=dataSnapshot;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




       b2.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               arrayAdapter =
                       new ArrayAdapter<String>(store_info.this,android.R.layout.simple_list_item_1,tokenlist);
               mlist.setAdapter(arrayAdapter);
                mess();
           }
       });





        ArrayAdapter<String> myadapter=new ArrayAdapter<String>(store_info.this,android
                .R.layout.simple_list_item_1,getResources().getStringArray(R.array.category));
        myadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(myadapter);


        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {



                cat.add(s1.getSelectedItem().toString());
                 arrayAdapter =
                        new ArrayAdapter<String>(store_info.this,android.R.layout.simple_list_item_1,cat);
                mlist.setAdapter(arrayAdapter);




            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here


            }

        });

       b1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               //String a=mlist.getItemAtPosition(1).toString();//get item from listview


                latitude = Double.parseDouble(lat);
                longitude = Double.parseDouble(lng);


               try {
                   Geocoder geocoder = new Geocoder(store_info.this, Locale.getDefault());
                   addresses = geocoder.getFromLocation(latitude, longitude, 1);// Here 1 represent max location result to returned, by documents it recommended 1 to 5
               } catch (IOException e) {
                   e.printStackTrace();
               }

               String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
               String city = addresses.get(0).getLocality();
               String state = addresses.get(0).getAdminArea();
               String country = addresses.get(0).getCountryName();
               String postalCode = addresses.get(0).getPostalCode();
               String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL


               String uniqueID = UUID.randomUUID().toString();

               LatLng l1 = new LatLng(latitude,longitude);
               String t = l1.toString().trim();
              t1 =e1.getText().toString();//store name


               //put the global list
               String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


               if(state!=null && !TextUtils.isEmpty(t1)) {
                   DatabaseReference ref = FirebaseDatabase.getInstance().getReference("global store info").child(country).child(state);
                   GeoFire geofire = new GeoFire(ref);
                   geofire.setLocation(uniqueID, new GeoLocation(latitude, longitude));
                   DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("global store info").child(country).child(state).child(uniqueID).child("store_name");
                   ref2.setValue(t1);
                   DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference("global store info").child(country).child(state).child(uniqueID).child("uid");
                   ref3.setValue(uid);

                     for(int i=0;i<mlist.getCount();i++) {
                         String uniqueID1 = UUID.randomUUID().toString();
                         DatabaseReference ref4 = FirebaseDatabase.getInstance().getReference("global store info").child(country)
                                 .child(state).child(uniqueID).child("category list").child(uniqueID1).child("category");
                         ref4.setValue(mlist.getItemAtPosition(i).toString());

                     }
                   //put the personal info

                   DatabaseReference ref11 = FirebaseDatabase.getInstance().getReference("seller").child(uid);
                   GeoFire geofire11 = new GeoFire(ref11);
                   geofire11.setLocation(uniqueID, new GeoLocation(latitude, longitude));
                   DatabaseReference ref13 = FirebaseDatabase.getInstance().getReference("seller").child(uid).child(uniqueID).child("store_name");
                   ref13.setValue(t1);
                   DatabaseReference ref14= FirebaseDatabase.getInstance().getReference("seller").child(uid).child(uniqueID).child("date");
                   ref14.setValue(currentDate);

                   for(int i=0;i<mlist.getCount();i++) {
                       String uniqueID1 = UUID.randomUUID().toString();
                       DatabaseReference ref4 = FirebaseDatabase.getInstance().getReference("seller").child(uid)
                               .child(uniqueID).child("category list").child(uniqueID1).child("category");
                       ref4.setValue(mlist.getItemAtPosition(i).toString());
                   }


               }
               else{
                   Toast.makeText(store_info.this,"please fill all info",Toast.LENGTH_LONG).show();

               }



           givenotification();

           }
       });


    }

    private void mess() {
        for (int i=0; i < tokenlist.size(); i++) {
            Notification notification = new Notification(t1+"opened in your vicinity and provides your demanded product", "dfsdfd");
            Sender sender = new Sender(tokenlist.get(i), notification);
            mservice.sendNotification(sender)
                    .enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                            if (response.body().success == 1) {
                                Toast.makeText(store_info.this, "success", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(store_info.this, "failure", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {
                            Log.e("ERROR", t.getMessage());

                        }
                    });
        }
    }
    private void givenotification() {


            try {
                Geocoder geocoder = new Geocoder(store_info.this, Locale.getDefault());
                addresses = geocoder.getFromLocation(latitude,longitude, 1);// Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
            }

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            state = addresses.get(0).getAdminArea();
            country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();



            String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("global demanded items").child(country).child(state);
            GeoFire geofire=new GeoFire(ref);
            GeoQuery geoQuery=geofire.queryAtLocation(new GeoLocation(latitude,longitude),3);
            geoQuery.removeAllListeners();

            geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {




                @Override//when the product is found
                public void onKeyEntered(String key, GeoLocation location) {



                        String name = ds.child(country).child(state).child(key).child("category").getValue(String.class);

                      for(int i=0;i<cat.size();i++) {
                          if (name.equals(cat.get(i))) {
                              String tok = ds.child(country).child(state).child(key).child("token").getValue(String.class);
                              tokenlist.add(tok);
                              Toast.makeText(store_info.this,name,Toast.LENGTH_LONG).show();




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

                }

                @Override
                public void onGeoQueryError(DatabaseError error) {

                }
            });




    }


}
