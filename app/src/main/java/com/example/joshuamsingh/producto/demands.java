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



        t1=(TextView) findViewById(R.id.t1);
        b1=(Button) findViewById(R.id.b1);

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

                        //final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        //DatabaseReference mref1= database.getReference("customer").child(uid).child(key).child("l").child("0");




              /*        mref1 = FirebaseDatabase.getInstance().getReference("customer").child(uid).child(key).child("l").child("0");

                        mref2 = FirebaseDatabase.getInstance().getReference("customer").child(uid).child(key).child("l").child("1");

                       mref1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                             longi=dataSnapshot.getValue(Double.class);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                   mref2.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {
                           lati=dataSnapshot.getValue(Double.class);
                       }

                       @Override
                       public void onCancelled(DatabaseError databaseError) {

                       }
                   });

*/

                      viewHolder.setDetails(model.getCategory(),model.getProduct(),model.getdate());
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
        String uid;
        Double longi,lati;
        demands d1;
        DatabaseReference mref1,mref2;

        public UserViewHolder(View itemView) {
            super(itemView);
            mview=itemView;
            locate=(TextView) mview.findViewById(R.id.t4);
            d1=new demands();

            locate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(ctx,key+"  "+longi+" "+lati,Toast.LENGTH_LONG).show();
                    uid=d1.getuid();
                    mref1 = FirebaseDatabase.getInstance().getReference("customer").child(uid).child(key).child("l").child("0");

                    mref2 = FirebaseDatabase.getInstance().getReference("customer").child(uid).child(key).child("l").child("1");

                    mref1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            longi=dataSnapshot.getValue(Double.class);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    mref2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            lati=dataSnapshot.getValue(Double.class);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });




                    d1.gotolocation(longi,lati,21);
                }
            });

        }

        public  void getContext(Context ctx){
            this.ctx=ctx;
        }
        public  void putkey(String key){
            this.key=key;


        }

        public void  setDetails(String category,String product,String date){
            TextView t1=(TextView) mview.findViewById(R.id.t1);
            TextView t2=(TextView) mview.findViewById(R.id.t2);
            TextView t3=(TextView) mview.findViewById(R.id.t3);

            t1.setText(category);
            t2.setText(product);
            t3.setText(date);


        }


    }


}
