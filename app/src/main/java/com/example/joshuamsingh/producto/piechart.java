package com.example.joshuamsingh.producto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class piechart extends AppCompatActivity  {

    String[] product_name;
    int[] freq;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piechart);

        product_name=new String[8];
        freq=new int[8];

        //category
        product_name[0]=getIntent().getExtras().getString("cat1");
        product_name[1]=getIntent().getExtras().getString("cat2");
        product_name[2]=getIntent().getExtras().getString("cat3");
        product_name[3]=getIntent().getExtras().getString("cat4");
        product_name[4]=getIntent().getExtras().getString("cat5");
        product_name[5]=getIntent().getExtras().getString("cat6");
        product_name[6]=getIntent().getExtras().getString("cat7");
        product_name[7]=getIntent().getExtras().getString("cat8");


        freq[0]=Integer.parseInt(getIntent().getExtras().getString("num1"));
        freq[1]=Integer.parseInt(getIntent().getExtras().getString("num2"));
        freq[2]=Integer.parseInt(getIntent().getExtras().getString("num3"));
        freq[3]=Integer.parseInt(getIntent().getExtras().getString("num4"));
        freq[4]=Integer.parseInt(getIntent().getExtras().getString("num5"));
        freq[5]=Integer.parseInt(getIntent().getExtras().getString("num6"));
        freq[6]=Integer.parseInt(getIntent().getExtras().getString("num7"));
        freq[7]=Integer.parseInt(getIntent().getExtras().getString("num8"));






       setupchart();



    }

    private void setupchart() {

        List<PieEntry> pieEntries=new ArrayList<>();
        for(int i=0;i<product_name.length;i++){
            pieEntries.add(new PieEntry(freq[i],product_name[i]));
        }

        PieDataSet dataset=new PieDataSet(pieEntries,"rainfall");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data=new PieData(dataset);


        PieChart chart=(PieChart) findViewById(R.id.piechart);
        chart.setDrawSliceText(false);
        chart.setData(data);
        chart.invalidate();
        chart.setClickable(true);
        chart.setClickable(true);


    }


}
