package com.example.tko_chess;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
    Button ViewPendingFriendsReq;
    Button ViewIncomingFriendReq;
    LinearLayout FriendsListLayout;

    //TextView Declarations;
    TextView ErrorMessage;

    //Friends list GET request
    //Creating request argument
    SingletonUser currUser = SingletonUser.getInstance();

    //LayoutInflater used to populate friends list scrollview
    LayoutInflater inflater = getLayoutInflater();

    //Request que used to send JSON requests
    RequestQueue queue = Volley.newRequestQueue(FriendsActivity.this);

    //Display Friends List
    //Create a string holding the username to concatenate to the URL
    String URLConcatenation = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        //Button Initializations
        FriendsToMenu = findViewById(R.id.FriendstoMenuBtn);
        ViewIncomingFriendReq = findViewById(R.id.IncomingFriendRequestBtn);
        ViewPendingFriendsReq = findViewById(R.id.PendingFriendRequestBtn);

        //TextView Initializations
        ErrorMessage = findViewById(R.id.ErrorTextView);

/*      //////////////////////////////////////////////////////////////////////////////
        //Delete this or comment out. Only for testing purposes when server is not up.
        View friendLayout = inflater.inflate(R.layout.friend_layout, FriendsList, true);
        TextView friendName = (TextView) friendLayout.findViewById(R.id.FriendEditText);
        try {
            friendName.setText(currUser.getUsername());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        //Delete this or comment out. Only for testing purposes when server is not up.
        //////////////////////////////////////////////////////////////////////////////*/

        //Display friends on screen upon initial loading of screen
        displayFriendsList(currUser.getListOfFriends());

        //Take user back to main menu
        FriendsToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FriendsActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });

        //Display incoming friend request menu
        ViewIncomingFriendReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                FriendsListLayout.removeAllViews();
            }
        });
    }



    private void displayFriendsList(JSONArray FriendsList) {

        FriendsListLayout.removeAllViews();
        FriendsListLayout = findViewById(R.id.FriendsLinearLayout);

        //For each friend the user has, put that friend in the linear layout of activity_friends
        for (int i = 0; i < FriendsList.length(); i++) {
            View friendLayout = inflater.inflate(R.layout.friend_layout, FriendsListLayout, true);
            TextView friendNameText = (TextView) friendLayout.findViewById(R.id.FriendEditText);
            Button removeFriendBtn = (Button) friendLayout.findViewById(R.id.RemoveFriendBtn);

            //Sets the friend's username in the text box next to the remove button
            try {
                friendNameText.setText(FriendsList.getString(i));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            //Remove friend button
            removeFriendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Get user's username in a string to concatenate onto URL path
                    URLConcatenation = currUser.getUsername() + "/";
                    URLConcatenation += friendNameText.getText().toString();

                    removeFriend();

                    //Update local list of friends and update display
                    currUser.updateUserObject(currUser.getUsername());
                    displayFriendsList(currUser.getListOfFriends());

                }
            });
        }
    }



    private void removeFriend() {
        JsonArrayRequest RemoveFriendReq = new JsonArrayRequest(Request.Method.PUT, Const.URL_SERVER_FRIENDSLIST + URLConcatenation, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                String temp;
                try {
                    temp = (String) response.get(1);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                //If removal succeeded, update friends List
                if (temp == "success") {
                    //Update local list of friends and update display
                    currUser.updateUserObject(currUser.getUsername());
                    displayFriendsList(currUser.getListOfFriends());
                } else {
                    ErrorMessage.setText("An error Occured");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorMessage.setText("An error Occured");
            }
        });
        queue.add(RemoveFriendReq);
    }
}