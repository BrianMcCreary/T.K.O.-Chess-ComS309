package com.example.tko_chess;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Lex Somers
 */
public class SingletonUser {

    // Static variable reference of CurrUser
    // of type SingletonUser
    private static SingletonUser UserInstance = null;

    // JSONObject that holds the currently logged in user's username and password
    private static JSONObject UserObject = null;

    // JSONArray that holds a JSONObject that contains the currently logged in user's username and password
    private static JSONArray UserArray = null;


    // Constructor
    // Here we will be creating private constructor
    // restricted to this class itself
    private SingletonUser(JSONObject user) throws JSONException {
        UserObject = user;

        JSONArray tempArray = new JSONArray();
        String username = UserObject.get("username").toString();
        String password = UserObject.get("password").toString();
        tempArray.put(username);
        tempArray.put(password);

        UserArray = tempArray;

    }

    // Static method
    // Static method to create instance of Singleton class or "login" user.
    // Needs to be provided a JSONObject
    public static SingletonUser login(JSONObject user) throws JSONException {
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
        UserObject = null;
        UserArray = null;
    }

    //Returns the username of the currently logged in user if there is one.
    public String getUsername() throws JSONException {
        return (String) UserObject.get("username");
    }

    //Returns the password of the currently logged in user if there is one.
    public String getPassword() throws JSONException {
        return (String) UserObject.get("password");
    }

    //Returns a JSONObject with the username and password of the currently logged in user if there is one
    public JSONObject getUserObject() {
        return UserObject;
    }

    //Returns a JSONArray with the username and password of the currently logged in user if there is one
    public JSONArray getUserArray() {
        return UserArray;
    }

}
