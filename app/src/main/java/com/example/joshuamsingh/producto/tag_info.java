package com.example.joshuamsingh.producto;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static java.util.Locale.PRC;
import static java.util.Locale.getDefault;

public class tag_info extends AppCompatActivity implements AdapterView.OnItemSelectedListener,OnFailureListener {

    private Button b1;
    private EditText e1;
    private Spinner e2;
   public String lat;
    public String lng;
    public String category;
    AutoCompleteTextView autocomplete;
    String[] product_name;
    Calendar calendar;
    String currentDate;


    List<Address> addresses;




    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_info);
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
                myHandaling(paramThread, paramThrowable);
            }
        });


        calendar=Calendar.getInstance();
         currentDate= DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());



        autocomplete=(AutoCompleteTextView) findViewById(R.id.product);
        product_name=getResources().getStringArray(R.array.product);
       ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,product_name);
        autocomplete.setAdapter(adapter);


        lat=getIntent().getExtras().getString("latitude");
        lng=getIntent().getExtras().getString("longitude");
        e1=(EditText) findViewById(R.id.e1);
        e2=(Spinner)findViewById(R.id.e2);

       b1=(Button) findViewById(R.id.b1);


        ArrayAdapter<String>myadapter=new ArrayAdapter<String>(tag_info.this,android
        .R.layout.simple_list_item_1,getResources().getStringArray(R.array.category));
        myadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         e2.setAdapter(myadapter);




            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    double latitude = Double.parseDouble(lat);
                    double longitude = Double.parseDouble(lng);
                    String lat = Double.toString(latitude);
                    String lng = Double.toString(longitude);


                    try {
                        Geocoder geocoder = new Geocoder(tag_info.this, Locale.getDefault());
                        addresses = geocoder.getFromLocation(latitude, longitude, 1);// Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try{
                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL


                    String uniqueID = UUID.randomUUID().toString();

                    LatLng l1 = new LatLng(latitude, longitude);
                    String t = l1.toString().trim();
                    String t1 = autocomplete.getText().toString();//product
                    category = e2.getSelectedItem().toString();//category

                    //put the global list
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Toast.makeText(tag_info.this, country, Toast.LENGTH_SHORT).show();

                    if (state != null) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("global demanded items").child(country).child(state);
                        GeoFire geofire = new GeoFire(ref);
                        geofire.setLocation(uniqueID, new GeoLocation(latitude, longitude));
                        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("global demanded items").child(country).child(state).child(uniqueID).child("category");
                        ref1.setValue(category);
                        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("global demanded items").child(country).child(state).child(uniqueID).child("product");
                        ref2.setValue(t1);
                        DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference("global demanded items").child(country).child(state).child(uniqueID).child("uid");
                        ref3.setValue(uid);
                        DatabaseReference ref4 = FirebaseDatabase.getInstance().getReference("global demanded items").child(country).child(state).child(uniqueID).child("token");
                        ref4.setValue(FirebaseInstanceId.getInstance().getToken());


                        //put the personal info

                        DatabaseReference ref11 = FirebaseDatabase.getInstance().getReference("customer").child(uid);
                        GeoFire geofire11 = new GeoFire(ref11);
                        geofire11.setLocation(uniqueID, new GeoLocation(latitude, longitude));
                        DatabaseReference ref12 = FirebaseDatabase.getInstance().getReference("customer").child(uid).child(uniqueID).child("category");
                        ref12.setValue(category);
                        DatabaseReference ref13 = FirebaseDatabase.getInstance().getReference("customer").child(uid).child(uniqueID).child("product");
                        ref13.setValue(t1);
                        DatabaseReference ref14 = FirebaseDatabase.getInstance().getReference("customer").child(uid).child(uniqueID).child("date");
                        ref14.setValue(currentDate);


                        finish();
                    } else {
                        Toast.makeText(tag_info.this, "unable get your location", Toast.LENGTH_LONG).show();

                    }
                    }
                    catch (Exception e) {
                        Log.e("Alert","Try n catch:::");
                        Toast.makeText(tag_info.this, "some problem occured try again ",Toast.LENGTH_LONG).show();


                    }

                }
            });

    }

    public void myHandaling(Thread paramThread, Throwable paramThrowable){
        Log.e("Alert","Lets See if it Works !!!" +"paramThread:::" +paramThread +"paramThrowable:::" +paramThrowable);
        Toast.makeText(tag_info.this, "Alert uncaughtException111",Toast.LENGTH_LONG).show();
      startActivity(new Intent(this,tag_info.class));

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
       category=adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onFailure(@NonNull Exception e) {
        Toast.makeText(this,"some problem occured",Toast.LENGTH_LONG).show();
    }
}
