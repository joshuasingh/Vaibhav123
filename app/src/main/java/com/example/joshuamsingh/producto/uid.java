package com.example.joshuamsingh.producto;

/**
 * Created by Joshua M Singh on 19-04-2018.
 */

public class uid {

    private String uid1;
    private String status;


    public uid() {

    }

    public uid(String uid1, String status) {
        this.uid1 = uid1;
        this.status = status;
    }

    public String getUid1() {
        return uid1;
    }

    public void setUid1(String uid1) {
        this.uid1 = uid1;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
