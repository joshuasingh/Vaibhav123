package com.example.joshuamsingh.producto;


import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joshuamsingh.producto.Model.MyResponse;
import com.example.joshuamsingh.producto.Model.Notification;
import com.example.joshuamsingh.producto.Model.Sender;
import com.example.joshuamsingh.producto.Remote.APIService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class seller extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    APIService mservice;

    private ViewPager v1;
    private LinearLayout l1;
    private slideradapter1 msliderAdapter;
    private TextView[] mdots;

    private DrawerLayout mdrawer;
    private ActionBarDrawerToggle mtoggle;
    private NavigationView nav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);

        Common.currentToken=FirebaseInstanceId.getInstance().getToken();

           mservice=Common.getFCMClient();

        v1=(ViewPager) findViewById(R.id.v1);
        l1=(LinearLayout) findViewById(R.id.l1);

        msliderAdapter = new slideradapter1(seller.this);
        v1.setAdapter(msliderAdapter);
        addDotIndicator(0);

        v1.addOnPageChangeListener(viewlistener);

        nav=(NavigationView) findViewById(R.id.nav);
        nav.setNavigationItemSelectedListener(this);
        mdrawer=(DrawerLayout) findViewById(R.id.drawer_layout);
        mtoggle=new ActionBarDrawerToggle(this,mdrawer,R.string.open,R.string.close);
        mdrawer.addDrawerListener(mtoggle);
        mtoggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





    }

    ViewPager.OnPageChangeListener viewlistener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotIndicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    public  void addDotIndicator(int pos){
        mdots=new TextView[2];
        l1.removeAllViews();
        for(int i=0;i<mdots.length;i++){
            mdots[i]=new TextView(this);
            mdots[i].setText(Html.fromHtml("&#8226;"));
            mdots[i].setTextSize(35);
            mdots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));
            l1.addView(mdots[i]);
        }


        if(mdots.length>0){
            mdots[pos].setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mtoggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();

        if(id==R.id.view1){
            startActivity(new Intent(seller.this,represented.class));
        }

        if(id==R.id.store1){
            startActivity(new Intent(seller.this,tag_a_store.class));
        }
        if(id==R.id.log1){
            FirebaseAuth.getInstance().signOut();
            new AsyncTask<Void,Void,Void>()
            {
                @Override
                protected Void doInBackground(Void... params)
                {
                    {
                        try
                        {
                            FirebaseInstanceId.getInstance().deleteInstanceId();
                        } catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    return null;
                }
                @Override
                protected void onPostExecute(Void result)
                {
                    //call your activity where you want to land after log out
                }
            }.execute();

            startActivity(new Intent(seller.this,MainActivity.class));
            finish();
        }

        mdrawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;



        }

    }





}
