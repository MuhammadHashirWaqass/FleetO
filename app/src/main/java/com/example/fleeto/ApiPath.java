package com.example.fleeto;

public class ApiPath {
    private static ApiPath instance;
    private String url;

    private ApiPath(){
        this.url= "http://10.224.72.32:3000";
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
