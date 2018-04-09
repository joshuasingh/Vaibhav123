package com.example.joshuamsingh.producto;

/**
 * Created by Joshua M Singh on 07-04-2018.
 */

public class searchclass {
    public String store_name;

    public searchclass(String store_name)
    {
        this.store_name=store_name;

    }

    public  searchclass(){

    }

    public String getstore()
    {
        return this.store_name;
    }

    public void setstore(String store_name){
        this.store_name=store_name;
    }


}
