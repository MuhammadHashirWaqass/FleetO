package com.example.fleeto;

public class Owner {

    public String OwnerID, Ownerage, Ownername,Ownerpass;
    public Owner(){

    }

    public Owner(String Ownerid,String Ownername,String Ownerage,String Ownerpass){

        this.OwnerID = Ownerid;
        this.Ownername = Ownername;
        this.Ownerage = Ownerage;
        this.Ownerpass = Ownerpass;
    }
    public String getOwnerID() {
        return OwnerID;
    }

    public String getOwnerPASS() {
        return Ownerpass;
    }



}
