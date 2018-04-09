package com.example.joshuamsingh.producto;

/**
 * Created by Joshua M Singh on 02-04-2018.
 */

public class users {

    public String category,product,date;

    public users(String category,String product,String date)
    {
        this.category=category;
        this.product=product;
        this.date=date;

    }

    public  users(){

    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category){
        this.category=category;
    }

    public String getProduct()
    {
        return product;
    }

    public void setProduct(String product){
        this.product=product;
    }

    public String getdate()
    {
        return date;
    }

    public void setdate(String date){
        this.date=date;
    }


}
