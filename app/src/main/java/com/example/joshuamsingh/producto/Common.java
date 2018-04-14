package com.example.joshuamsingh.producto;

import com.example.joshuamsingh.producto.Remote.APIService;
import com.example.joshuamsingh.producto.Remote.RetrofitClient;

/**
 * Created by Joshua M Singh on 13-04-2018.
 */

public class Common {
    public static String currentToken="";

     private static String baseUrl="https://fcm.googleapis.com/";

    public static APIService getFCMClient(){
        return RetrofitClient.getClient(baseUrl).create(APIService.class);
    }

}
