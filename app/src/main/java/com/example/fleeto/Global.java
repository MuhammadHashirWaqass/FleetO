package com.example.fleeto;

public class Global {
    private static Global instance;
    private int driverId;

    private Global(){}

    public static Global getInstance() {
        if (instance == null){
            instance = new Global();
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
