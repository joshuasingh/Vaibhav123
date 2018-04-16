package com.example.joshuamsingh.producto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class message extends AppCompatActivity {

    TextView t1;
    String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        t1=(TextView) findViewById(R.id.t1);
       s=getIntent().getExtras().getString("body");
        t1.setText(s+" ");

    }
}
