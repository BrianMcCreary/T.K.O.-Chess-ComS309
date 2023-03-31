package com.example.tko_chess;

import org.json.JSONObject;

public class SingletonUser {
    //ASK ALEX: How is this different from a global variable? Why does the getInstance() method return UserInstance,
    //          an instance of the entire class, instead of just User, the object within the class that holds the
    //          information we care about? Also, why is User public? Doesn't that mean that I could just change
    //          the variable value thereby making changes to the singleton?

    //          I think I answered my last question, but now I'm wondering how can I preset User to be the jsonObject I made
    //          in the login screen that contains the username and password the person entered? To do that I have to create
    //          an instance of the class first, but then the instance of the class I created wouldn't have the proper
    //          User object. And I don't think I'd be able to change it either because the instance of the class is static,
    //          So I can't change anything about it that instance, including the User Object variable.

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
