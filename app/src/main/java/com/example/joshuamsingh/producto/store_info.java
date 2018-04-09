package com.example.joshuamsingh.producto;

import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class store_info extends AppCompatActivity {
    EditText e1;
    Spinner s1;
    ListView mlist;
    ArrayList<String> cat;
    ArrayAdapter<String> arrayAdapter;
    Button b1;
    DatabaseReference mref;
    String lat,lng;
    List<Address> addresses;
    Calendar calendar;
    String currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_info);

      e1=(EditText) findViewById(R.id.t1);
        s1=(Spinner) findViewById(R.id.s1);
        mlist=(ListView) findViewById(R.id.l1);
        cat=new ArrayList<>();
        b1=(Button) findViewById(R.id.b1);


        //get lat and long
        lat=getIntent().getExtras().getString("latitude1");
        lng=getIntent().getExtras().getString("longitude1");


        //get current date
        calendar= Calendar.getInstance();
        currentDate= DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());




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


               double latitude = Double.parseDouble(lat);
               double longitude = Double.parseDouble(lng);


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
               String t1 =e1.getText().toString();//store name


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


                   finish();
               }
               else{
                   Toast.makeText(store_info.this,"please fill all info",Toast.LENGTH_LONG).show();

               }





           }
       });


    }



}
