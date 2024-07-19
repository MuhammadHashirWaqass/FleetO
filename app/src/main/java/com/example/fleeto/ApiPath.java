package com.example.fleeto;

public class ApiPath {
    private static ApiPath instance;
    private String url;

    private ApiPath(){
//        this.url= "https://fleet-o-backend.vercel.app";
        this.url = "http://192.168.1.14:3000";
    };

    public static ApiPath getInstance() {
        if (instance == null){
            instance = new ApiPath();
        }
        return instance;
    }

    public String getUrl() {
        return url;
    }
}
