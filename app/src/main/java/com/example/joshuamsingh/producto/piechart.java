package com.example.joshuamsingh.producto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class piechart extends AppCompatActivity {

    float rainfall[]={98.8f,123.8f,161.6f,52.3f};
    String month[]={"jan","feb","mar","apr"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piechart);

       setupchart();



    }

    private void setupchart() {

        List<PieEntry> pieEntries=new ArrayList<>();
        for(int i=0;i<rainfall.length;i++){
            pieEntries.add(new PieEntry(rainfall[i],month[i]));
        }

        PieDataSet dataset=new PieDataSet(pieEntries,"rainfall");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data=new PieData(dataset);


        PieChart chart=(PieChart) findViewById(R.id.piechart);
        chart.setData(data);
        chart.invalidate();
    }
}
