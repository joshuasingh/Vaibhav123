package com.example.joshuamsingh.producto;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.w3c.dom.Text;

import java.io.IOException;

public class demands extends AppCompatActivity implements OnMapReadyCallback,NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mrecycle;
    private DatabaseReference muserdatabase,mref1,mref2;
    private TextView t1;
    private Button b1;
    GoogleMap mgooglemap;
    String uid;
    Double longi,lati;
    DataSnapshot ds;
    private DrawerLayout mdrawer;
    private ActionBarDrawerToggle mtoggle;
    private NavigationView nav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_demands);
        initmap();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        muserdatabase=FirebaseDatabase.getInstance().getReference("customer").child(uid);
        mrecycle=(RecyclerView) findViewById(R.id.recycle);
        mrecycle.setHasFixedSize(true);
        mrecycle.setLayoutManager(new LinearLayoutManager(this));



        nav=(NavigationView) findViewById(R.id.nav);
        nav.setNavigationItemSelectedListener(this);
        mdrawer=(DrawerLayout) findViewById(R.id.drawer_layout);
        mtoggle=new ActionBarDrawerToggle(this,mdrawer,R.string.open,R.string.close);
        mdrawer.addDrawerListener(mtoggle);
        mtoggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        DatabaseReference ref45= FirebaseDatabase.getInstance().getReference().child("customer").child(uid);

        ref45.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ds=dataSnapshot;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

       demandsearch();



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mtoggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String getuid(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


    public void gotolocation(double lat, double lng, float zoom) {

        LatLng latlng = new LatLng(lat, lng);
        mgooglemap.moveCamera(CameraUpdateFactory.newLatLng(latlng));//camera moves with the user
        mgooglemap.animateCamera(CameraUpdateFactory.zoomTo(zoom));
        putmarker(lat, lng);
    }

    public void putmarker(double lat, double lng) {
        MarkerOptions opt = new MarkerOptions().position(new LatLng(lat, lng));
        mgooglemap.addMarker(opt);

    }



    private void initmap() {
        MapFragment mapfragment=(MapFragment) getFragmentManager().findFragmentById(R.id.fragment);
        mapfragment.getMapAsync( this);
    }

    private void demandsearch() {
        FirebaseRecyclerAdapter<users,UserViewHolder> firebaseRecycleradapter=new
                FirebaseRecyclerAdapter<users, UserViewHolder>
                        (users.class,R.layout.list_layout,UserViewHolder.class,muserdatabase) {
                    @Override
                    protected void populateViewHolder(UserViewHolder viewHolder, users model, int position) {



                        String key=getRef(position).getKey();


                      viewHolder.setDetails(model.getCategory(),model.getProduct(),model.getdate(),ds,mgooglemap);
                       viewHolder.getContext(getApplicationContext());
                       viewHolder.putkey(key);
                    }
                };

                mrecycle.setAdapter(firebaseRecycleradapter);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mgooglemap=googleMap;

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.tag){
            startActivity(new Intent(demands.this,customermap.class));
        }

        if(id==R.id.search){
            startActivity(new Intent(demands.this,search_product.class));
        }
        if(id==R.id.home){
            startActivity(new Intent(demands.this,select.class));
        }
        if(id==R.id.log){
            FirebaseAuth.getInstance().signOut();
            stopService(new Intent(demands.this,serviceclass.class));
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

            startActivity(new Intent(demands.this,MainActivity.class));
            finish();
        }

        mdrawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public  static class UserViewHolder extends RecyclerView.ViewHolder{

        View mview;
        TextView locate;
        Context ctx;
        String key;
        String category;
        Double longi,lati;
        demands d1;
        DataSnapshot ds;
        GoogleMap mgoogle;
        Marker m1;

        public UserViewHolder(View itemView) {
            super(itemView);
            mview=itemView;
            locate=(TextView) mview.findViewById(R.id.t4);
            d1= new demands();

           try {
               locate.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {

                       double lat = ds.child(key).child("l").child("0").getValue(double.class);
                       double lng = ds.child(key).child("l").child("1").getValue(double.class);
                       String r = Double.toString(lat);
                       LatLng l1 = new LatLng(lat, lng);
                       mgoogle.moveCamera(CameraUpdateFactory.newLatLng(l1));//camera moves with the user
                       mgoogle.animateCamera(CameraUpdateFactory.zoomTo(16));


                       MarkerOptions opt = new MarkerOptions().title(category).position(new LatLng(lat, lng));
                       m1 = mgoogle.addMarker(opt);

                   }
               });
           }
           catch (Exception e){
               Toast.makeText(ctx, "some error occured", Toast.LENGTH_SHORT).show();
           }

        }

        public  void getContext(Context ctx){
            this.ctx=ctx;
        }
        public  void putkey(String key){
            this.key=key;


        }

        public void  setDetails(String category,String product,String date,DataSnapshot ds,GoogleMap mgoogle){
            TextView t1=(TextView) mview.findViewById(R.id.t1);
            TextView t2=(TextView) mview.findViewById(R.id.t2);
            TextView t3=(TextView) mview.findViewById(R.id.t3);
            this.category=category;
            this.ds=ds;
            this.mgoogle=mgoogle;
            t1.setText(category);
            t2.setText(product);
            t3.setText(date);


        }


    }


}
