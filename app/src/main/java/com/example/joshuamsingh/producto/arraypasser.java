package com.example.joshuamsingh.producto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Joshua M Singh on 06-04-2018.
 */

public class arraypasser  {
    ArrayList<String> a;

    public arraypasser()
    {
        a=new ArrayList<>();
    }

    public void putar(String str)
    {
        a.add(str);
    }

    public String getar(int i)
    {
        return a.get(i);
    }

    public int getlen()
    {
        return a.size();
    }





}
