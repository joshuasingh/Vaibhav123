package com.example.joshuamsingh.producto.Service;

import com.example.joshuamsingh.producto.Common;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Joshua M Singh on 13-04-2018.
 */

public class MyFirebaseServices extends FirebaseInstanceIdService{
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshToken= FirebaseInstanceId.getInstance().getToken();
        Common.currentToken=refreshToken;
    }
}
