package com.example.joshuamsingh.producto.Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.joshuamsingh.producto.MainActivity;
import com.example.joshuamsingh.producto.R;
import com.example.joshuamsingh.producto.message;
import com.example.joshuamsingh.producto.search_product;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;
import java.util.UUID;

/**
 * Created by Joshua M Singh on 13-04-2018.
 */

public class MyFirebaseMessaging  extends FirebaseMessagingService{

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        showNotification(remoteMessage.getNotification());
    }

    private void showNotification(RemoteMessage.Notification notification) {

        Random rand = new Random();
        Intent intent=new Intent(this,search_product.class);

        String[] latLng = notification.getTitle().split(",");
       double latitude1 = Double.parseDouble(latLng[0]);
        double longitude1 = Double.parseDouble(latLng[1]);

        intent.putExtra("lat",latitude1);
        intent.putExtra("lng",longitude1);
        intent.putExtra("notify","start");

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingintent=PendingIntent.getActivity(this,0,intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        NotificationCompat.Builder builder=new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("STORE OPENED")
                .setContentText(notification.getBody())
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setContentIntent(pendingintent);
        int rand_int1 = rand.nextInt(1000);

        NotificationManager notificationManager=(NotificationManager) getSystemService
                                       (NOTIFICATION_SERVICE);
        notificationManager.notify(rand_int1,builder.build());

     }
}
