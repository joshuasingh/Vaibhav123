package com.example.joshuamsingh.producto;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joshuamsingh.producto.Model.MyResponse;
import com.example.joshuamsingh.producto.Model.Notification;
import com.example.joshuamsingh.producto.Model.Sender;
import com.example.joshuamsingh.producto.Remote.APIService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class seller extends AppCompatActivity {

    Button b1,b2,b3,b4,b5;
    TextView t1;
    APIService mservice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);

        Common.currentToken=FirebaseInstanceId.getInstance().getToken();

           mservice=Common.getFCMClient();


        b1=(Button) findViewById(R.id.b1);
        b3=(Button) findViewById(R.id.b3);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(seller.this,represented.class));

            }
        });

       b2=(Button) findViewById(R.id.b2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(seller.this,tag_a_store.class));

            }
        });


        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(seller.this,select.class));


            }
        });


        b4=(Button) findViewById(R.id.b4);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(seller.this,MainActivity.class));
                finish();


            }
        });


      b5=(Button) findViewById(R.id.b5);
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String q="hey";
                String w="body";
                Notification notification=new Notification(q,w);
                Sender sender=new Sender(Common.currentToken,notification);
                mservice.sendNotification(sender)
                        .enqueue(new Callback<MyResponse>() {
                            @Override
                            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                if(response.body().success==1){
                                Toast.makeText(seller.this,"success",Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(seller.this,"failure",Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<MyResponse> call, Throwable t) {
                                Log.e("ERROR",t.getMessage());

                            }
                        });



            }
        });

    }
}
