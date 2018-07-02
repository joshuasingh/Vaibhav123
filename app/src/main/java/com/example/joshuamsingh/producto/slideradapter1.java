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

/**
 * Created by Joshua M Singh on 23-04-2018.
 */

public class slideradapter1 extends PagerAdapter{
    Context context;
    LayoutInflater layoutInflater;


    public slideradapter1(Context context) {
        this.context = context;
    }

    //Arrays
    public int[] slide_image={
            R.drawable.eat_icon,
            R.drawable.sleep_icon

    };

    public String[] slide_heading={
            "VIEW DEMANDS"
            ,"TAG YOUR STORE"

    };


    public String[] slide_desc={
            "    Check the demands for various product    \n"  +
            "  in any selected area and view the result in \n" +
            "    form of PIECHART to better understand the \n"+
            "                market                           "
            ,"          Tag your store according to what    \n" +
            "       your perceived from the demand of the    \n" +
            "    market. Fill all the category that you'd     \n"+
            "            introduce in your store               "


    };


    public Class[] activity={
            represented.class,
            tag_a_store.class

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
