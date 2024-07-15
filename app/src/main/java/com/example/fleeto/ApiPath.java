package com.example.fleeto;

public class ApiPath {
    private static ApiPath instance;
    private String url;

    private ApiPath(){
        this.url= "http://192.168.1.7:3000";
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
