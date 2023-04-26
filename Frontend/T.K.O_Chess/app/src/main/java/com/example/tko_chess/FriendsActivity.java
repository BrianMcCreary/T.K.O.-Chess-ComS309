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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tko_chess.ultils.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Lex Somers
 *
 * Friends page where users can view, remove, accept, and deny friends/friend requests.
 */
public class FriendsActivity extends AppCompatActivity {

    /**
     * Display Tracker. Used to track what method needs to be called to
     *      update the screen after sending a friend request.
     *
     *       1 = displaySentFriendReq()
     *       2 = displayPendingFriendReq()
     *       others = displayFriendsList()
     */
    int DisplayTracker = 0;

    //Button declarations
    /**
     * Takes user back to main menu
     */
    ImageButton FriendsToMenu;

    /**
     * Sends friend request to the user specified in the FriendReqTo EditText.
     */
    Button SendFriendReq;

    /**
     * Displays the user's sent friend requests.
     */
    Button ViewSentFriendReq;

    /**
     * Displays the user's pending friend requests.
     */
    Button ViewPendingFriendReq;

    /**
     * Displays the user's friends.
     */
    Button ViewFriendsReq;

    /**
     * Friends list container that holds all of the user's friends
     *      represented by smaller linear layout displays.
     */
    LinearLayout FriendsListLayout;

    //TextView Declarations
    /**
     * Displays any error messages.
     */
    TextView ErrorMessage;

    //EditText Declarations
    /**
     * Specifies which other user the current user wants to send a friend request to.
     */
    EditText FriendReqTo;

    /**
     * Stores the currently logged in user.
     */
    SingletonUser currUser = SingletonUser.getInstance();

    //Friends list GET request
    //Creating request argument
    /**
     * Context for the volley requests.
     */

    Context context = this;

    /**
     * Holds the end of the URL path mapping for any requests made.
     */
    String URLConcatenation = "";


    /**
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     * Loads friends screen onto device. Displays user's friends list by default.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        //Updating user info
        currUser.updateUserObject(currUser.getUsername(), context);

        //Button Initializations
        FriendsToMenu = findViewById(R.id.FriendstoMenuBtn);
        SendFriendReq = findViewById(R.id.SendFriendRequestBtn);
        ViewPendingFriendReq = findViewById(R.id.PendingFriendRequestBtn);
        ViewSentFriendReq = findViewById(R.id.SentFriendRequestBtn);
        ViewFriendsReq = findViewById(R.id.ViewFriendsBtn);

        //TextView Initializations
        ErrorMessage = findViewById(R.id.ErrorTextView);

        //EditText Initializations
        FriendReqTo = findViewById(R.id.SendFriendRequestText);

        //LinearLayout Initializations
        FriendsListLayout = findViewById(R.id.FriendsLinearLayout);

        //LayoutInflater used to populate friends list scrollview
        LayoutInflater inflater = getLayoutInflater();

        //Display friends on screen upon initial loading of screen
        displayFriendsList(currUser.getListOfFriends());

        /**
         * Takes user back to main menu.
         */
        FriendsToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //currUser.UpdateUserObject(currUser.getUsername());

                Intent intent = new Intent(FriendsActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });

        /**
         * Sends out a friend request.
         */
        SendFriendReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Puts sender's username and acceptor's username in a String to concatenate onto URL path
                URLConcatenation = currUser.getUsername() + "/";
                URLConcatenation += FriendReqTo.getText().toString();
                sendRequest();
            }
        });

        /**
         * Displays incoming friend requests.
         */
        ViewPendingFriendReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayPendingFriendReq(currUser.getListOfPendingFriendReq());
            }
        });

        /**
         * Displays pending friend requests.
         */
        ViewSentFriendReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displaySentFriendReq(currUser.getListOfSentFriendReq());
            }
        });

        /**
         * Displays the user's friends.
         */
        ViewFriendsReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayFriendsList(currUser.getListOfFriends());
            }
        });
    }


    /**
     * Sends a POST request to the backend containing a friend request
     *      to the user specified in the FriendReqTo EditText.
     */
    private void sendRequest() {
        //Request que used to send JSON requests
        RequestQueue queue = Volley.newRequestQueue(FriendsActivity.this);

        JsonObjectRequest SendFriendReq = new JsonObjectRequest(Request.Method.POST, Const.URL_SERVER_SENDFRIENDREQUEST + URLConcatenation, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String temp;
                try {
                    temp = (String) response.get("message");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                //If request was sent successfully, update screen.
                if (temp.equals("success")) {
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
                    ErrorMessage.setText("Friend request unable to be sent.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorMessage.setText("An error occurred.");
            }
        });
        queue.add(SendFriendReq);

        URLConcatenation = "";
    }


    /**
     * Displays all others that have sent a friend request to the user.
     * @param PendingFriendReq List of other users who have sent the user a friend request
     */
    private void displayPendingFriendReq(JSONArray PendingFriendReq) {
        //Clear scroll view
        FriendsListLayout.removeAllViews();
        FriendsListLayout = findViewById(R.id.FriendsLinearLayout);

        //For each friend the user has, put that friend in the linear layout of activity_friends
        for (int i = 0; i < PendingFriendReq.length(); i++) {
            View inflatedLayout = getLayoutInflater().inflate(R.layout.pending_friend_request_layout, null, false);
            TextView IncomingFriendNameText = (TextView) inflatedLayout.findViewById(R.id.IncomingFriendNameTextView);
            Button acceptFriendBtn = (Button) inflatedLayout.findViewById(R.id.AcceptFriendBtn);
            Button denyFriendBtn = (Button) inflatedLayout.findViewById(R.id.DenyFriendBtn);

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
                    URLConcatenation = IncomingFriendNameText.getText().toString() + "/";
                    URLConcatenation += currUser.getUsername();

                    acceptFriendReq();
                }
            });

            denyFriendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Puts sender's username and acceptor's username in a String to concatenate onto URL path
                    URLConcatenation = IncomingFriendNameText.getText().toString() + "/";
                    URLConcatenation += currUser.getUsername();

                    denyFriendReq();
                }
            });

            FriendsListLayout.addView(inflatedLayout);
        }
        //Tracks that we are currently on the pending friends requests screen
        DisplayTracker = 2;
    }


    /**
     * Sends POST request to backend accepting one of the user's incoming friend requests.
     */
    private void acceptFriendReq() {
        //Request que used to send JSON requests
        RequestQueue queue = Volley.newRequestQueue(FriendsActivity.this);
        JsonObjectRequest AcceptFriendReq = new JsonObjectRequest(Request.Method.POST, Const.URL_SERVER_ACCEPTFRIENDREQUEST + URLConcatenation, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String temp;
                try {
                    temp = (String) response.get("message");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                //If request cancel succeeded, update friends List
                if (temp.equals("success")) {
                    //Update local list of friends and update display
                    currUser.updateUserObject(currUser.getUsername(), context);
                    displayPendingFriendReq(currUser.getListOfPendingFriendReq());
                } else {
                    ErrorMessage.setText("Could not accept request.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorMessage.setText("An error occurred");
            }
        });
        queue.add(AcceptFriendReq);

        URLConcatenation = "";
    }



    /**
     * Sends POST request to backend denying one of the user's incoming friend requests.
     */
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
                if (temp.equals("success")) {
                    //Update local list of friends and update display
                    currUser.updateUserObject(currUser.getUsername(), context);
                    displayPendingFriendReq(currUser.getListOfPendingFriendReq());
                } else {
                    ErrorMessage.setText("Could not deny request.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorMessage.setText("An error occurred");
            }
        });
        queue.add(DenyFriendReq);

        URLConcatenation = "";
    }



    /**
     * Displays all others that the user has sent a friend request to.
     * @param SentFriendReq List of others the user has sent a friend request to.
     */
    private void displaySentFriendReq(JSONArray SentFriendReq) {
        //Clear scroll view
        FriendsListLayout.removeAllViews();
        FriendsListLayout = findViewById(R.id.FriendsLinearLayout);

        //For each friend the user has, put that friend in the linear layout of activity_friends
        for (int i = 0; i < SentFriendReq.length(); i++) {
            View inflatedLayout = getLayoutInflater().inflate(R.layout.sent_friend_request_layout, null, false);
            TextView requestedFriendNameText = (TextView) inflatedLayout.findViewById(R.id.RequestFriendNameTextView);
            Button cancelFriendBtn = (Button) inflatedLayout.findViewById(R.id.CancelFriendBtn);

            //Sets the friend's username in the text box next to the remove button
            try {
                requestedFriendNameText.setText(SentFriendReq.getString(i));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            FriendsListLayout.addView(inflatedLayout);

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
        //Tracks that we are currently on the sent friends requests screen
        DisplayTracker = 1;
    }



    /**
     * Sends POST request to backend canceling one of the user's outgoing friend requests.
     */
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
                if (temp.equals("success")) {
                    //Update local list of friends and update display
                    currUser.updateUserObject(currUser.getUsername(), context);
                    displaySentFriendReq(currUser.getListOfSentFriendReq());
                } else {
                    ErrorMessage.setText("Could not cancel request.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorMessage.setText("An error occurred");
            }
        });
        queue.add(CancelFriendReq);

        URLConcatenation = "";
    }


    /**
     * Displays all others the user is currently friends with.
     * @param FriendsList List of others the user is friends with.
     */
    private void displayFriendsList(JSONArray FriendsList) {
        FriendsListLayout.removeAllViews();
        FriendsListLayout = findViewById(R.id.FriendsLinearLayout);

        //For each friend the user has, put that friend in the linear layout of activity_friends
        for (int i = 0; i < FriendsList.length(); i++) {

            View inflatedLayout = getLayoutInflater().inflate(R.layout.friend_layout, null, false);
            TextView friendNameText = (TextView) inflatedLayout.findViewById(R.id.FriendNameTextView);
            Button removeFriendBtn = (Button) inflatedLayout.findViewById(R.id.RemoveFriendBtn);

            //Sets the friend's username in the text box next to the remove button
            try {
                friendNameText.setText(FriendsList.getString(i));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            FriendsListLayout.addView(inflatedLayout);

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

        //Tracks that we are currently on the view friends screen
        DisplayTracker = 0;
    }


    /**
     * Sends POST request to backend removing the selected friend from the user's friends list.
     */
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
                if (temp.equals("success")) {
                    //Update local list of friends and update display
                    currUser.updateUserObject(currUser.getUsername(), context);
                    displayFriendsList(currUser.getListOfFriends());
                } else {
                    ErrorMessage.setText("Could not remove friend.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorMessage.setText("An error occurred");
            }
        });
        queue.add(RemoveFriendReq);

        URLConcatenation = "";
    }
}