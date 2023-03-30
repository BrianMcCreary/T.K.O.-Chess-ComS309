package com.example.tko_chess;

import org.json.JSONObject;

public class SingletonUser {
    // Static variable reference of CurrUser
    // of type SingletonUser
    private static SingletonUser UserInstance = null;

    //Declaring a variable of type JSONObject
    public JSONObject User;

    // Constructor
    // Here we will be creating private constructor
    // restricted to this class itself
    private SingletonUser()
    {
        User = null; //Replace it with the JSONObject
    }

    // Static method
    // Static method to create instance of Singleton class
    public static synchronized SingletonUser getInstance()
    {
        if (UserInstance == null) {
            UserInstance = new SingletonUser();
        }

        return UserInstance;
    }

}
