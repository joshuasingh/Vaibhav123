package com.example.joshuamsingh.producto;

import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    public EditText e1,e2;
    public Button b1,b2,b3,b4;
    public TextView k1;

    public String email,password;

    private RadioButton seller,customer;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        e2=(EditText) findViewById(R.id.t2);
        e1=(EditText) findViewById(R.id.t1);
        b1=(Button) findViewById(R.id.b1);
        b2=(Button) findViewById(R.id.b2);
        k1=(TextView) findViewById(R.id.e1);
        b3=(Button) findViewById(R.id.b3);
        b4=(Button) findViewById(R.id.b4);
        // RadioButton configuration
        seller=(RadioButton) findViewById(R.id.r1);
        customer=(RadioButton) findViewById(R.id.r2);



        FirebaseDatabase data=FirebaseDatabase.getInstance();
        final DatabaseReference ref= data.getReference("joshua");




        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text=e1.getText().toString();
                DatabaseReference current_user_db= FirebaseDatabase.getInstance().getReference().child("user").child("seller").child(text);
                current_user_db.setValue(true);
            }
        });




        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase data=FirebaseDatabase.getInstance();
                final DatabaseReference ref= data.getReference("singh");

                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String s1=String.valueOf(dataSnapshot.getValue());
                        k1.setText(s1);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });




        firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuthListener=new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    startActivity(new Intent(MainActivity.this,seller.class));
                    finish();
                    return;

                }

            }
        };



//sign in button

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = e2.getText().toString().trim();
                password = e1.getText().toString().trim();



                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)||(!seller.isChecked() && !customer.isChecked()) ) {

                    Toast.makeText(MainActivity.this, "fields empty", Toast.LENGTH_LONG).show();

                } else {


                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "sign in problem", Toast.LENGTH_LONG).show();
                            }
                            else{
                                String uid=firebaseAuth.getCurrentUser().getUid();

                                if(seller.isChecked()){
                                    DatabaseReference current_user_db= FirebaseDatabase.getInstance().getReference().child("user").child("seller").child(uid);
                                    current_user_db.setValue(email);
                                }

                                else{ DatabaseReference current_user_db= FirebaseDatabase.getInstance().getReference().child("user").child("customer").child(uid);
                                    current_user_db.setValue(email);

                                }

                                startActivity(new Intent(MainActivity.this,seller.class));
                                //sdsad

                            }

                        }
                    });


                }


            }
        });















        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = e2.getText().toString().trim();
                String password = e1.getText().toString().trim();



                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)||(!seller.isChecked() && !customer.isChecked()) ) {

                    Toast.makeText(MainActivity.this, "fields empty", Toast.LENGTH_LONG).show();

                } else {


                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "sign error", Toast.LENGTH_SHORT).show();


                            }
                        }
                    });


                }
            }
        });







    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

}
