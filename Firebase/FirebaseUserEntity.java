package com.chen.hanlin.plank.Firebase;


import com.google.android.gms.maps.model.LatLng;

public class FirebaseUserEntity {

    public String uId;

    public String email;


    public String name;

    public String country;

    //private LatLng current;

    public FirebaseUserEntity(){
    }


    public FirebaseUserEntity(String uId, String email, String name,String country) {
        this.uId = uId;
        this.email = email;
        this.name = name;
        this.country = country;

    }

    public String getuId() {
        return uId;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }


}
