package com.example.fleeto;

public class Driver {

    public String driverID, driverage, drivername,driverpass;
        public Driver(){

    }

    public Driver(String driverid,String drivername,String driverage,String driverpass){

        this.driverID = driverid;
        this.drivername = drivername;
        this.driverage = driverage;
        this.driverpass = driverpass;
    }
    public String getDriverID() {
        return driverID;
    }

    public String getDRIVERPASS() {
        return driverpass;
    }



}
