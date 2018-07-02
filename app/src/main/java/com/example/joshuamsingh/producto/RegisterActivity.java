package com.example.joshuamsingh.producto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {

    Button b1;
    EditText e1,e2,e3;
    FirebaseAuth mAuth;
    RadioButton seller,customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

      b1=(Button) findViewById(R.id.b1);
     e1=(EditText) findViewById(R.id.e1);
        e2=(EditText) findViewById(R.id.e2);
        e3=(EditText) findViewById(R.id.e3);
        seller=(RadioButton) findViewById(R.id.r1);
        customer=(RadioButton) findViewById(R.id.r2);



        mAuth=FirebaseAuth.getInstance();





        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = e2.getText().toString().trim();
                String password = e3.getText().toString().trim();



                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)||(!seller.isChecked() && !customer.isChecked()) ) {

                    Toast.makeText(RegisterActivity.this, "fields empty", Toast.LENGTH_LONG).show();

                } else {


                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "sign error", Toast.LENGTH_SHORT).show();


                            }
                            else{
                                Toast.makeText(RegisterActivity.this, "user registered", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "sign in problem", Toast.LENGTH_LONG).show();
                            }
                            else{
                                String uid=mAuth.getCurrentUser().getUid();
                                DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("user info").child(uid).child("status");
                                if(seller.isChecked()){

                                    ref.setValue("seller");

                                    SharedPreferences s1=getSharedPreferences("userinfo", Context.MODE_PRIVATE);//only this app can access this info
                                    SharedPreferences.Editor e1=s1.edit();
                                    e1.putString("username","seller");
                                    e1.apply();

                                    Intent launchNextActivity;
                                    launchNextActivity = new Intent(RegisterActivity.this,seller.class);
                                    launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(launchNextActivity);

                                }
                                if(customer.isChecked()){
                                    ref.setValue("customer");
                                    SharedPreferences s1=getSharedPreferences("userinfo", Context.MODE_PRIVATE);//only this app can access this info
                                    SharedPreferences.Editor e1=s1.edit();
                                    e1.putString("username","customer");

                                    e1.apply();
                                    String tok= FirebaseInstanceId.getInstance().getToken();
                                    DatabaseReference not = FirebaseDatabase.getInstance().getReference("notification").child(uid).child("latest_id");
                                    not.setValue(tok);
                                    Intent launchNextActivity;
                                    launchNextActivity = new Intent(RegisterActivity.this,select.class);
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

}
