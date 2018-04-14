package com.example.joshuamsingh.producto.Remote;

import com.example.joshuamsingh.producto.Model.MyResponse;
import com.example.joshuamsingh.producto.Model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Joshua M Singh on 13-04-2018.
 */

public interface  APIService {
   @Headers(
           {
                   "Content-Type:application/json",
                   "Authorization:Key=AAAAVKT_8CU:APA91bH9mP33K0N-4mOI7FNmHIX-aGzD9LuQiePPco2oYTwP7-dPx6hbP5GtqBjQ23doEao9sMUQOUlsKx79lqcMdomjnj0yO9_v_suOAjlcSoJkQvqcPUbCG3wDlvSumd3MACd5S-DM"
           }
   )
   @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);

}
