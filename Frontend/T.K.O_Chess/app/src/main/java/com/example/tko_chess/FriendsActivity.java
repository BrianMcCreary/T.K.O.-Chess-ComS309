package com.example.tko_chess;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tko_chess.ultils.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Lex Somers
 */
public class FriendsActivity extends AppCompatActivity {

    //Display Tracker. Used to track what method needs to be called to update the screen after sending a friend request.
    //1 = displaySentFriendReq(), 2 = displayPendingFriendReq(), others = displayFriendsList()
    int DisplayTracker = 0;

    //Button declarations
    ImageButton FriendsToMenu;
    Button SendFriendReq;
    Button ViewSentFriendReq;
    Button ViewPendingFriendReq;
    LinearLayout FriendsListLayout;

    //TextView Declarations
    TextView ErrorMessage;

    //EditText Declarations
    EditText FriendReqTo;

    //Friends list GET request
    //Creating request argument
    SingletonUser currUser = SingletonUser.getInstance();

    Context context = this;

    //LayoutInflater used to populate friends list scrollview
    //LayoutInflater inflater = getLayoutInflater();

    //Request que used to send JSON requests
    //RequestQueue queue = Volley.newRequestQueue(FriendsActivity.this);

    //Display Friends List
    //Create a string holding the username to concatenate to the URL
    String URLConcatenation = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        //Button Initializations
        FriendsToMenu = findViewById(R.id.FriendstoMenuBtn);
        SendFriendReq = findViewById(R.id.SendFriendRequestBtn);
        ViewPendingFriendReq = findViewById(R.id.PendingFriendRequestBtn);
        ViewSentFriendReq = findViewById(R.id.SentFriendRequestBtn);

        //TextView Initializations
        ErrorMessage = findViewById(R.id.ErrorTextView);

        //EditText Initializations
        FriendReqTo = findViewById(R.id.SendFriendRequestText);

        //LinearLayout Initializations
        FriendsListLayout = findViewById(R.id.FriendsLinearLayout);

        //LayoutInflater used to populate friends list scrollview
        LayoutInflater inflater = getLayoutInflater();

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

        //Send out a friend request
        SendFriendReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Puts sender's username and acceptor's username in a String to concatenate onto URL path
                URLConcatenation = currUser.getUsername() + "/";
                URLConcatenation += FriendReqTo.getText().toString();
                sendRequest();
            }
        });

        //Display incoming friend requests
        ViewPendingFriendReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayPendingFriendReq(currUser.getListOfPendingFriendReq());
            }
        });

        //Display pending friend requests
        ViewSentFriendReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displaySentFriendReq(currUser.getListOfSentFriendReq());
            }
        });
    }



    private void sendRequest() {
        //Request que used to send JSON requests
        RequestQueue queue = Volley.newRequestQueue(FriendsActivity.this);

        JsonObjectRequest SendFriendReq = new JsonObjectRequest(Request.Method.PUT, Const.URL_SERVER_SENDFRIENDREQUEST + URLConcatenation, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String temp;
                try {
                    temp = (String) response.get("message");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                //If request was sent successfully, update screen.
                if (temp == "success") {
                    if (DisplayTracker == 1) { //displaySentFriendReq()
                        //Update local list of sent requests and update display
                        currUser.updateUserObject(currUser.getUsername(), context);
                        displaySentFriendReq(currUser.getListOfPendingFriendReq());
                    } else
                    if (DisplayTracker == 2) { //displayPendingFriendReq()
                        //Update local list of pending requests and update display
                        currUser.updateUserObject(currUser.getUsername(), context);
                        displayPendingFriendReq(currUser.getListOfPendingFriendReq());
                    }
                    else { //displayFriendsList()
                        //Update local list of pending requests and update display
                        currUser.updateUserObject(currUser.getUsername(), context);
                        displayFriendsList(currUser.getListOfPendingFriendReq());
                    }
                } else {
                    ErrorMessage.setText("An error Occurred");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorMessage.setText("An error Occurred");
            }
        });
        queue.add(SendFriendReq);
    }



    private void displayPendingFriendReq(JSONArray PendingFriendReq) {
        //Clear scroll view
        FriendsListLayout.removeAllViews();
        FriendsListLayout = findViewById(R.id.FriendsLinearLayout);

        //LayoutInflater used to populate friends list scrollview
        LayoutInflater inflater = getLayoutInflater();

        //For each friend the user has, put that friend in the linear layout of activity_friends
        for (int i = 0; i < PendingFriendReq.length(); i++) {
            View friendLayout = inflater.inflate(R.layout.pending_friend_request_layout, FriendsListLayout, true);
            TextView IncomingFriendNameText = (TextView) friendLayout.findViewById(R.id.IncomingFriendNameEditText);
            Button acceptFriendBtn = (Button) friendLayout.findViewById(R.id.AcceptFriendBtn);
            Button denyFriendBtn = (Button) friendLayout.findViewById(R.id.DenyFriendBtn);

            //Sets the friend's username in the text box next to the remove button
            try {
                IncomingFriendNameText.setText(PendingFriendReq.getString(i));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            //Accept friend button
            acceptFriendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Puts sender's username and acceptor's username in a String to concatenate onto URL path
                    URLConcatenation += IncomingFriendNameText.getText().toString() + "/";
                    URLConcatenation = currUser.getUsername();

                    acceptFriendReq();
                }
            });

            denyFriendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Puts sender's username and acceptor's username in a String to concatenate onto URL path
                    URLConcatenation += IncomingFriendNameText.getText().toString() + "/";
                    URLConcatenation = currUser.getUsername();

                    denyFriendReq();
                }
            });
        }
    }



    private void acceptFriendReq() {
        //Request que used to send JSON requests
        RequestQueue queue = Volley.newRequestQueue(FriendsActivity.this);
        JsonObjectRequest AcceptFriendReq = new JsonObjectRequest(Request.Method.PUT, Const.URL_SERVER_ACCEPTFRIENDREQUEST + URLConcatenation, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String temp;
                try {
                    temp = (String) response.get("message");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                //If request cancel succeeded, update friends List
                if (temp == "success") {
                    //Update local list of friends and update display
                    currUser.updateUserObject(currUser.getUsername(), context);
                    displayPendingFriendReq(currUser.getListOfPendingFriendReq());
                } else {
                    ErrorMessage.setText("An error Occurred");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorMessage.setText("An error Occurred");
            }
        });
        queue.add(AcceptFriendReq);
    }



    private void denyFriendReq() {
        //Request que used to send JSON requests
        RequestQueue queue = Volley.newRequestQueue(FriendsActivity.this);
        JsonObjectRequest DenyFriendReq = new JsonObjectRequest(Request.Method.PUT, Const.URL_SERVER_DELETEFRIENDREQUEST + URLConcatenation, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String temp;
                try {
                    temp = (String) response.get("message");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                //If request cancel succeeded, update friends List
                if (temp == "success") {
                    //Update local list of friends and update display
                    currUser.updateUserObject(currUser.getUsername(), context);
                    displayPendingFriendReq(currUser.getListOfPendingFriendReq());
                } else {
                    ErrorMessage.setText("An error Occurred");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorMessage.setText("An error Occurred");
            }
        });
        queue.add(DenyFriendReq);
    }



    private void displaySentFriendReq(JSONArray SentFriendReq) {
        //Clear scroll view
        FriendsListLayout.removeAllViews();
        FriendsListLayout = findViewById(R.id.FriendsLinearLayout);

        //LayoutInflater used to populate friends list scrollview
        LayoutInflater inflater = getLayoutInflater();

        //For each friend the user has, put that friend in the linear layout of activity_friends
        for (int i = 0; i < SentFriendReq.length(); i++) {
            View friendLayout = inflater.inflate(R.layout.sent_friend_request_layout, FriendsListLayout, true);
            TextView requestedFriendNameText = (TextView) friendLayout.findViewById(R.id.RequestFriendNameEditText);
            Button cancelFriendBtn = (Button) friendLayout.findViewById(R.id.CancelFriendBtn);

            //Sets the friend's username in the text box next to the remove button
            try {
                requestedFriendNameText.setText(SentFriendReq.getString(i));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            //Remove friend button
            cancelFriendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Get user's username in a string to concatenate onto URL path
                    URLConcatenation = currUser.getUsername() + "/";
                    URLConcatenation += requestedFriendNameText.getText().toString();

                    cancelFriendReq();
                }
            });
        }
    }



    private void cancelFriendReq() {
        //Request que used to send JSON requests
        RequestQueue queue = Volley.newRequestQueue(FriendsActivity.this);
        JsonObjectRequest CancelFriendReq = new JsonObjectRequest(Request.Method.PUT, Const.URL_SERVER_DELETEFRIENDREQUEST + URLConcatenation, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String temp;
                try {
                    temp = (String) response.get("message");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                //If request cancel succeeded, update friends List
                if (temp == "success") {
                    //Update local list of friends and update display
                    currUser.updateUserObject(currUser.getUsername(), context);
                    displaySentFriendReq(currUser.getListOfSentFriendReq());
                } else {
                    ErrorMessage.setText("An error Occurred");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorMessage.setText("An error Occurred");
            }
        });
        queue.add(CancelFriendReq);
    }



    private void displayFriendsList(JSONArray FriendsList) {
        FriendsListLayout.removeAllViews();
        FriendsListLayout = findViewById(R.id.FriendsLinearLayout);

        //LayoutInflater used to populate friends list scrollview
        LayoutInflater inflater = getLayoutInflater();

        //For each friend the user has, put that friend in the linear layout of activity_friends
        for (int i = 0; i < FriendsList.length(); i++) {
            View friendLayout = inflater.inflate(R.layout.friend_layout, FriendsListLayout, true);
            TextView friendNameText = (TextView) friendLayout.findViewById(R.id.FriendNameEditText);
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
                    //Puts user's username and friend they are removing in a String to concatenate onto URL path
                    URLConcatenation = currUser.getUsername() + "/";
                    URLConcatenation += friendNameText.getText().toString();

                    removeFriend();
                }
            });
        }
    }



    private void removeFriend() {
        //Request que used to send JSON requests
        RequestQueue queue = Volley.newRequestQueue(FriendsActivity.this);
        JsonObjectRequest RemoveFriendReq = new JsonObjectRequest(Request.Method.PUT, Const.URL_SERVER_FRIENDSLIST + URLConcatenation, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String temp;
                try {
                    temp = (String) response.get("message");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                //If removal succeeded, update friends List
                if (temp == "success") {
                    //Update local list of friends and update display
                    currUser.updateUserObject(currUser.getUsername(), context);
                    displayFriendsList(currUser.getListOfFriends());
                } else {
                    ErrorMessage.setText("An error Occurred");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorMessage.setText("An error Occurred");
            }
        });
        queue.add(RemoveFriendReq);
    }
}