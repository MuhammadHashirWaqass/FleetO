package com.example.fleeto;

// Singleton class to ensure only one instance of user is there
public class User {
    private static User userInstance;

    int userId;
    String name;
    String email;
    String password;

    private  User(){}

    public static User getUserInstance() {
        if (userInstance == null){
            userInstance = new User();
        }
        return userInstance;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
