package com.example.tko_chess;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tko_chess.ultils.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Lex Somers
 *
 * Singleton class made to store the currently logged in user and relevant information.
 */
public class SingletonUser extends AppCompatActivity {

    /**
     * Static instance of the class that is returned when getInstance() is called.
     */
    private static SingletonUser UserInstance = null;

    /**
     * JSONObject that holds the currently logged in user's information.
     */
    private static JSONObject UserObject = null;



    /**
     * Gets an instance of the currently logged in user.
     * @return an instance of this singleton class.
     */
    public static synchronized SingletonUser getInstance()
    {
        if (UserInstance == null) {
            UserInstance = new SingletonUser();
        }
        return UserInstance;
    }



    /**
     * Constructor.
     */
    private SingletonUser() {
        UserObject = null;
        //Instantiate UserObject
    }



    /**
     * Instantiates the UserObejct variable with a JSONObject representation of the user trying to login.
     * @param user String containing logged in user's username.
     * @param context Context for volley requests.
     */
    public void updateUserObject(String user, Context context) {

        //Get user object from backend
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest GetUserReq = new JsonObjectRequest(Request.Method.GET, Const.URL_SERVER_GETUSER + user, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                UserObject = response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
            }
        });
        queue.add(GetUserReq);
    }



    /**
     *  Static method to log user out by nulling the static instance of the class and JSONObject variable.
     */
    public static void logout()
    {
        UserInstance = null;
        UserObject = null;
    }



    /**
     * Gets the username of the currently logged in user.
     * @return String with the username of the currently logged in user.
     */
    public String getUsername()  {
        try {
            return (String) UserObject.get("username");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }



    /**
     * Gets the password of the currently logged in user.
     * @return String with the password of the currently logged in user.
     * @throws JSONException if JSONObejct is null.
     */
    public String getPassword() {
        try {
            return (String) UserObject.get("password");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }



    /**
     * Gets the friends of the currently logged in user.
     * @return JSONArray with Strings of the friends of the currently logged in user.
     */
    public JSONArray getListOfFriends() {
        try {
            return (JSONArray) UserObject.get("friends");
        } catch (JSONException e) {
            throw new RuntimeException(e);

        }
    }



    /**
     * Gets the pending friend requests of the currently logged in user.
     * @return JSONArray with Strings of the incoming friend requests of the currently logged in user.
     */
    public JSONArray getListOfPendingFriendReq() {
        try {
            return (JSONArray) UserObject.get("incomingRequests");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }



    /**
     * Gets the sent friend requests of the currently logged in user.
     * @return JSONArray with Strings of the out going friend requests of the currently logged in user.
     */
    public JSONArray getListOfSentFriendReq() {
        try {
            return (JSONArray) UserObject.get("outgoingRequests");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
