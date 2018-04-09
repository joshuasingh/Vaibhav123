package com.example.joshuamsingh.producto;

import android.location.Address;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class test extends AppCompatActivity {

    TextView t1;
    ArrayList<String> category,count;
    String k;
    String s1;
    Button b1;
    int a;
    DatabaseReference ref1;
    String[] product_name;
    int[] frequency;
    arraypasser a1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

      b1=(Button) findViewById(R.id.b1);
        category=new ArrayList<>();
        count=new ArrayList<>();
        a1=new arraypasser();
        frequency=new int[30];

        Gson gson = new Gson();
        a1 = gson.fromJson(getIntent().getStringExtra("myjson"), arraypasser.class);

       /*  s1=getIntent().getExtras().getString("1");
        String s2=getIntent().getExtras().getString("2");
        String s3=getIntent().getExtras().getString("3");
        String s4=getIntent().getExtras().getString("4");
        String s5=getIntent().getExtras().getString("5");s
        String s6=getIntent().getExtras().getString("6");
        String s7=getIntent().getExtras().getString("7");
        String s8=getIntent().getExtras().getString("8");
        String s9=getIntent().getExtras().getString("9");
         a=Integer.parseInt(s9);
        count.add(s1);
        count.add(s2);
        count.add(s3);
        count.add(s4);
        count.add(s5);
        count.add(s6);
        count.add(s7);
        count.add(s8);
        */





        t1=(TextView) findViewById(R.id.t1);

        ref1 = FirebaseDatabase.getInstance().getReference()
                            .child("global demanded items").child("India").child("Uttar Pradesh");

                    ref1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                             showdata(dataSnapshot);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });






            }




            public void showdata(DataSnapshot ds)
            {
                info i1=new info();
               int g=a1.getlen();
                for(int i=0;i<g;i++) {
                    i1.setcategory(ds.child(a1.getar(i)).getValue(info.class).getcategory());
                    category.add(i1.getcategory());
                }



                product_name=getResources().getStringArray(R.array.category);

                for(int i=0;i<product_name.length;i++) {
                    String y = product_name[i];
                    frequency[i] = Collections.frequency(category, y);
                }
                    String y=a1.getar(0);
                     t1.setText(y+" ");
                    setupchart();
            }


            private void setupchart() {

        List<PieEntry> pieEntries=new ArrayList<>();
        for(int i=0;i<product_name.length;i++){
            pieEntries.add(new PieEntry(frequency[i],product_name[i]));
        }

        PieDataSet dataset=new PieDataSet(pieEntries,"rainfall");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data=new PieData(dataset);


        PieChart chart=(PieChart) findViewById(R.id.piechart);
        chart.setData(data);
        chart.invalidate();
    }



}
