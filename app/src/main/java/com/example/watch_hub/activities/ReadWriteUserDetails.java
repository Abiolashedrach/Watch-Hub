package com.example.watch_hub.activities;

public class ReadWriteUserDetails {
    public String lastName,gender,mobile, firstName;
    public ReadWriteUserDetails(){}

    public ReadWriteUserDetails( String txtFirst ,String txtLast,String txtGender, String txtMobile){
        this.firstName = txtFirst;
        this.lastName = txtLast;
        this.gender = txtGender;
        this.mobile = txtMobile;

    }
}
