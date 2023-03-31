package com.example.tko_chess;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tko_chess.ultils.Const;

import org.json.JSONException;
import org.json.JSONObject;

public class FriendsActivity extends AppCompatActivity {

    //Button declarations
    ImageButton FriendsToMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        //Button Initializations
        FriendsToMenu = findViewById(R.id.FriendstoMenuBtn);

        //Friends list GET request
        //Create a Request Que for the JsonObjectRequest
        RequestQueue queue = Volley.newRequestQueue(FriendsActivity.this);

        //Checks to see if there is a user that matches the input username and login.
        JsonArrayRequest FriendsListReq = new JsonArrayRequest(Request.Method.GET, Const.URL_SERVER_LOGIN, ,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String temp;
                        //Get confirmation/failure of receiving FriendsList from backend.
                        try {
                            temp = (String) response.get("message");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        //If request for Friends List was "success"
                        if (temp.equals("true")) {

                        }
                        //else, show error message
                        else {
//                            try {
//
//                            } catch (JSONException e) {
//                                throw new RuntimeException(e);
//                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());

            }
        });
        queue.add(FriendsListReq);


        FriendsToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FriendsActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });
    }
}
