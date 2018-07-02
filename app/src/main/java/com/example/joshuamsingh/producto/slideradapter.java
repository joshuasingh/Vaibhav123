package com.example.joshuamsingh.producto;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by Joshua M Singh on 19-04-2018.
 */

public class slideradapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;


    public slideradapter(Context context) {
        this.context = context;
    }

    //Arrays
    public int[] slide_image={
            R.drawable.eat_icon,
            R.drawable.sleep_icon,
            R.drawable.code_icon
    };

    public String[] slide_heading={
        "TAG A PRODUCT"
            ,"SEARCH A PRODUCT"
            ,"VIEW PREVIOUS DEMANDS"
    };


    public String[] slide_desc={
                "           Put a Marker on map where\n" +
                "        you want your demanded product to\n" +
                "      be made available by prospective seller."
            ,   "         search a product in your vicinity\n" +
                "     Change the range of your search as  per \n" +
                "                your requirement."
            ,   "          view the products that you \n" +
                "       have tagged and locate them on  map\n"

    };


    public Class[] activity={
            customermap.class,
            search_product.class
            ,demands.class
    };






    @Override
    public int getCount() {
        return slide_heading.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==(RelativeLayout) object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        layoutInflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.slidelayout,container,false);

        ImageView i1=(ImageView) view.findViewById(R.id.i1);
        TextView t1=(TextView) view.findViewById(R.id.t1);
        TextView t2=(TextView) view.findViewById(R.id.t2);

        i1.setImageResource(slide_image[position]);
        t1.setText(slide_heading[position]);
        t2.setText(slide_desc[position]);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             context.startActivity(new Intent(context,activity[position]));
            }
        });


        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
      container.removeView((RelativeLayout)object);
    }
}
