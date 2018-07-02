package com.example.joshuamsingh.producto;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.renderscript.Byte2;
import android.speech.tts.Voice;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

      EditText t1,t2;
      Button b1,b3;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    DataSnapshot ds;
    String uid;
    public boolean connect;
    boolean haveConnectedWifi = false;
    boolean haveConnectedMobile = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        t1 = (EditText) findViewById(R.id.t1);
        t2 = (EditText) findViewById(R.id.t2);

        b1 = (Button) findViewById(R.id.b1);
        b3 = (Button) findViewById(R.id.b3);
        mAuth = FirebaseAuth.getInstance();


        //check connectivity


        if(!connectivity()){
            Toast.makeText(MainActivity.this,"no internet",Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder =new AlertDialog.Builder(this);
            builder.setTitle("No internet Connection");
            builder.setMessage("Please turn on internet connection to continue");
            builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    startActivity(new Intent(MainActivity.this,MainActivity.class));
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }







            DatabaseReference ref45 = FirebaseDatabase.getInstance().getReference().child("user info");

            ref45.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ds = dataSnapshot;
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                uid = mAuth.getCurrentUser().getUid();
                // SystemClock.sleep(13000);
            }


            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    try {
                        if (user != null) {

                            //String name = ds.child(uid).child("status").getValue(String.class);
                            SharedPreferences s1 = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                            String name = s1.getString("username", " ");//second parameter gives the values if the value you're asking is not present

                            if (name.equals("seller")) {
                                Intent launchNextActivity;
                                launchNextActivity = new Intent(MainActivity.this, seller.class);
                                launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(launchNextActivity);
                            }
                            if (name.equals("customer")) {
                                Intent launchNextActivity;
                                launchNextActivity = new Intent(MainActivity.this, select.class);
                                launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(launchNextActivity);
                            }
                            // ...
                        }
                    } catch (Exception e) {

                    }
                }

            };


            b3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                    finish();
                }
            });


            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String email = t1.getText().toString().trim();
                    String password = t2.getText().toString();

                    if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {


                        Toast.makeText(MainActivity.this, "please fill all the info", Toast.LENGTH_LONG).show();

                    } else {
                        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "sign in problem", Toast.LENGTH_LONG).show();
                                } else {
                                    uid = mAuth.getCurrentUser().getUid();
                                    String name = ds.child(uid).child("status").getValue(String.class);
                                    if (name.equals("seller")) {
                                        SharedPreferences s1 = getSharedPreferences("userinfo", Context.MODE_PRIVATE);//only this app can access this info
                                        SharedPreferences.Editor e1 = s1.edit();
                                        e1.putString("username", "seller");
                                        e1.apply();
                                        Intent launchNextActivity;
                                        launchNextActivity = new Intent(MainActivity.this, seller.class);
                                        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        startActivity(launchNextActivity);
                                    }
                                    if (name.equals("customer")) {
                                        SharedPreferences s1 = getSharedPreferences("userinfo", Context.MODE_PRIVATE);//only this app can access this info
                                        SharedPreferences.Editor e1 = s1.edit();
                                        e1.putString("username", "customer");
                                        e1.apply();
                                        String tok=FirebaseInstanceId.getInstance().getToken();
                                        DatabaseReference not = FirebaseDatabase.getInstance().getReference("notification").child(uid).child("latest_id");
                                        not.setValue(tok);
                                        Intent launchNextActivity;
                                        launchNextActivity = new Intent(MainActivity.this, select.class);
                                        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        startActivity(launchNextActivity);
                                    }


                                }
                            }

                        });

                    }

                }
            });





    }


    private boolean connectivity() {


        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    @Override
    protected void onResume() {
        super.onResume();
        new AsyncTask<Void,Void,Void>()
        {
            @Override
            protected Void doInBackground(Void... params)
            {

                String token = FirebaseInstanceId.getInstance().getToken();
                while(token == null)//this is used to get firebase token until its null so it will save you from null pointer exeption
                {
                    token = FirebaseInstanceId.getInstance().getToken();
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void result)
            {

            }
        }.execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }



    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }





    //sign in method





}