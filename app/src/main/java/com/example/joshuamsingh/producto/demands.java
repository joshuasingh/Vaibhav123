package com.example.joshuamsingh.producto;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import org.w3c.dom.Text;

public class demands extends AppCompatActivity implements OnMapReadyCallback {

    private RecyclerView mrecycle;
    private DatabaseReference muserdatabase,mref1,mref2;
    private TextView t1;
    private Button b1;
    GoogleMap mgooglemap;
    String uid;
    Double longi,lati;
     DataSnapshot ds;


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
            d1=new demands();

            locate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    double lat = ds.child(key).child("l").child("0").getValue(double.class);
                    double lng =ds.child(key).child("l").child("1").getValue(double.class);
                    String r=Double.toString(lat);
                    LatLng l1 = new LatLng(lat, lng);
                    mgoogle.moveCamera(CameraUpdateFactory.newLatLng(l1));//camera moves with the user
                    mgoogle.animateCamera(CameraUpdateFactory.zoomTo(16));


                        MarkerOptions opt = new MarkerOptions().title(category).position(new LatLng(lat, lng));
                        m1 = mgoogle.addMarker(opt);

                }
            });

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
