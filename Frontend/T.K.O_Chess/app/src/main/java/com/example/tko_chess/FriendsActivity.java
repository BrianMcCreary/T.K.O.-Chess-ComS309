package com.example.tko_chess;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.tko_chess.ultils.Const;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * @author Lex Somers
 */
public class FriendsActivity extends AppCompatActivity {

    //Button declarations
    ImageButton FriendsToMenu;
    LinearLayout FriendsListLayout;

    //Friends list GET request
    //Creating request argument
    SingletonUser currUser = SingletonUser.getInstance();

    //LayoutInflater used to populate friends list scrollview
    LayoutInflater inflater = getLayoutInflater();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        //Button Initializations
        FriendsToMenu = findViewById(R.id.FriendstoMenuBtn);

/*      //Delete this or comment out. Only for testing purposes when server is not up.
        View friendLayout = inflater.inflate(R.layout.friend_layout, FriendsList, true);
        TextView friendName = (TextView) friendLayout.findViewById(R.id.FriendEditText);
        try {
            friendName.setText(currUser.getUsername());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        //Delete this or comment out. Only for testing purposes when server is not up.*/

        //Create a Request Que for the JsonObjectRequest
        RequestQueue queue = Volley.newRequestQueue(FriendsActivity.this);
        //Create a string holding the username to concatenate to the URL
        String URLConcatenation = null;
        try {
            URLConcatenation = currUser.getUsername();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        //Checks to see if there is a user that matches the input username and login.
        JsonArrayRequest FriendsListReq = new JsonArrayRequest(Request.Method.GET, Const.URL_SERVER_FRIENDSLIST + URLConcatenation, currUser.getUserArray(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        createFriendList(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());

            }
        });
        queue.add(FriendsListReq);

        //Button to take user back to main menu
        FriendsToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FriendsActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });
    }

    private void createFriendList(JSONArray listOfFriends) {

        FriendsListLayout = findViewById(R.id.FriendsLinearLayout);

        //If request for Friends List was "success"
        if (listOfFriends.length() != 0) {
            //For each friend the user has, put that friend in the linear layout of activity_friends
            for (int i = 0; i < listOfFriends.length(); i++) {
                View friendLayout = inflater.inflate(R.layout.friend_layout, FriendsListLayout, true);
                TextView friendName = (TextView) friendLayout.findViewById(R.id.FriendEditText);
                try {
                    //Sets the friend's username in the text box next to the remove button
                    friendName.setText(listOfFriends.getString(i));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                //Set a Click Listener for the button on each friend
            }
        }
        //else, show error message
        else {
            //TODO
        }
    }

}
