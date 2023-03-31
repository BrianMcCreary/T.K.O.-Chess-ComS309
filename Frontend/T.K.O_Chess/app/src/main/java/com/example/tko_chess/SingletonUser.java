package com.example.tko_chess;

import org.json.JSONException;
import org.json.JSONObject;

public class SingletonUser {

    // Static variable reference of CurrUser
    // of type SingletonUser
    private static SingletonUser UserInstance = null;

    // JSONObject that holds the currently logged in user's username and password
    private static JSONObject User = null;

    // Constructor
    // Here we will be creating private constructor
    // restricted to this class itself
    private SingletonUser(JSONObject user)
    {
        User = user;
    }

    // Static method
    // Static method to create instance of Singleton class or "login" user.
    // Needs to be provided a JSONObject
    public static SingletonUser login(JSONObject user)
    {
        if (UserInstance == null) {
            UserInstance = new SingletonUser(user);
        }

        return UserInstance;
    }

    // Static method
    // Static method to create instance of Singleton class
    public static synchronized SingletonUser getInstance()
    {
        return UserInstance;
    }

    // Static method
    // Static method to log user out by nulling the static instance of the class and JSONObject variable
    public static void logout()
    {
        UserInstance = null;
        User = null;
    }

    //Returns the username of the currently logged in user if there is one.
    public String getUsername() throws JSONException {
        return (String) User.get("username");
    }

    //Returns the password of the currently logged in user if there is one.
    public String getPassword() throws JSONException {
        return (String) User.get("password");
    }

}
