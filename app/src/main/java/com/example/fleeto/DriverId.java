package com.example.fleeto;

public class DriverId {
    private static DriverId instance;
    private int driverId;

    private DriverId(){}

    public static DriverId getInstance() {
        if (instance == null){
            instance = new DriverId();
        }
        return instance;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }
}
