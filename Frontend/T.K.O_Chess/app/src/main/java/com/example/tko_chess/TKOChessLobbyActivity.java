package com.example.tko_chess;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tko_chess.ultils.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TKOChessLobbyActivity extends AppCompatActivity {

    Button startBtn;
    ImageButton backBtn;
    SingletonUser currUser = SingletonUser.getInstance();
    TextView ErrorMessage;
    TextView LobbyKeyText;
    String lobbyKey = "";
    String URLConcatenation = "";
    Context context = this;
    LinearLayout joinedUserLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tko_chess_game_lobby);

        LobbyKeyText = findViewById(R.id.LobbyKey);

        //Sets the textview object on lobby screen to the lobby key used to access the lobby
        URLConcatenation = currUser.getUsername();
        RequestQueue queue = Volley.newRequestQueue(TKOChessLobbyActivity.this);
        StringRequest createLobby = new StringRequest(Request.Method.POST, Const.URL_SERVER_LOBBY + URLConcatenation, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("----------------------------------------------------------------------------------------------------" + response);
                lobbyKey = response;
                LobbyKeyText.setText(lobbyKey);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
            }
        });
        queue.add(createLobby);

        backBtn = findViewById(R.id.backBtn2);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TKOChessLobbyActivity.this, TKOChessHostOrJoinActivity.class);
                startActivity(intent);
            }
        });

        startBtn = findViewById(R.id.startBtn2);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TKOChessLobbyActivity.this, ChessActivity.class);
                startActivity(intent);
            }
        });
    }
    private void displayUsersList(JSONArray UsersList){
        joinedUserLayout.removeAllViews();
        joinedUserLayout = findViewById(R.id.JoinedUsersLayout);

        LayoutInflater inflater = getLayoutInflater();

        for(int i = 0; i < UsersList.length(); i++){
            View userLayout = inflater.inflate(R.layout.joined_users_layout, joinedUserLayout, true);
            TextView usernameText = (TextView) userLayout.findViewById(R.id.JoinedUserNameEditText);
            Button kickBtn = (Button) userLayout.findViewById(R.id.KickUserBtn);

            try{
                usernameText.setText(UsersList.getString(i));
            } catch (JSONException e){
                throw new RuntimeException(e);
            }

            kickBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    URLConcatenation = currUser.getUsername() + "/";
                    URLConcatenation += usernameText.getText().toString();

                    kickUser();
                }
            });
        }
    }

    public void kickUser(){
        RequestQueue queue = Volley.newRequestQueue(TKOChessLobbyActivity.this);
        JsonObjectRequest kickUserRequest = new JsonObjectRequest(Request.Method.PUT, Const.URL_SERVER_LOBBY + URLConcatenation, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String temp;
                try{
                    temp = (String) response.get("message");
                } catch (JSONException e){
                    throw new RuntimeException(e);
                }

                if(temp.equals("success")){
                    currUser.updateUserObject(currUser.getUsername(), context);
                    displayUsersList(currUser.getListOfUsers());
                }
                else{
                    ErrorMessage.setText("Could not remove user");
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse (VolleyError error) {
                ErrorMessage.setText("An error occurred");
            }
        });
    }
}
