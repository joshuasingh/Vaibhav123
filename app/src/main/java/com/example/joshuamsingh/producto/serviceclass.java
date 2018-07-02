package com.example.joshuamsingh.producto;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.joshuamsingh.producto.Model.MyResponse;
import com.example.joshuamsingh.producto.Model.Notification;
import com.example.joshuamsingh.producto.Model.Sender;
import com.example.joshuamsingh.producto.Remote.APIService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Joshua M Singh on 07-06-2018.
 */

public class serviceclass extends IntentService {
    DataSnapshot ds;
    APIService mservice;

    private static final String TAG="jjo";

    public serviceclass(){
        super("serviceclass");
        mservice=Common.getFCMClient();

    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref45 = FirebaseDatabase.getInstance().getReference().child("pending message").child(uid);
        ref45.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for  (DataSnapshot datasnap: dataSnapshot.getChildren()){

                    String name = datasnap.child("body").getValue(String.class);
                    String name1 = datasnap.child("title").getValue(String.class);
                    Notification notification = new Notification(name,name1);
                    Sender sender = new Sender(FirebaseInstanceId.getInstance().getToken(),notification);
                    mservice.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {

                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {

                                    if (response.body().success == 1) {
                                         int t=1;
                                    } else {

                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {
                                    Log.e("ERROR", t.getMessage());

                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Log.i(TAG,"the service has now started");
    }
}
