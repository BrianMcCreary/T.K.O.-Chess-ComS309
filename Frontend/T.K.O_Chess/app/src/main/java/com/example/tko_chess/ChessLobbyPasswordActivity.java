package com.example.tko_chess;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tko_chess.ultils.Const;

import org.json.JSONException;
import org.json.JSONObject;

public class ChessLobbyPasswordActivity extends AppCompatActivity {

    EditText lobbyKey;
    Button joinBtn;
    ImageButton backBtn;
    TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_password);

        backBtn = findViewById(R.id.backBtn8);

        //Goes back to Host or Join Screen
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChessLobbyPasswordActivity.this, ChessHostOrJoinActivity.class);
                startActivity(intent);
            }
        });

        joinBtn = findViewById(R.id.joinBtn2);

        //Takes user to joined lobby
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lobbyKey = (EditText) findViewById(R.id.lobbyPassword);
                error = (TextView) findViewById(R.id.loginErrorText);

                String lobbyPassword = lobbyKey.getText().toString();

                JSONObject lobbyPass = new JSONObject();
                try{
                    lobbyPass.put("Lobby Password", lobbyKey.getText());
                } catch (JSONException e){
                    e.printStackTrace();
                }

                RequestQueue queue = Volley.newRequestQueue(ChessLobbyPasswordActivity.this);

                JsonObjectRequest userObjectRequest = new JsonObjectRequest(Request.Method.POST, Const.URL_SERVER_CHESSLOBBYPASSWORD, lobbyPass,
                        new Response.Listener<JSONObject>(){

                            @Override
                            public void onResponse(JSONObject response){
                                String temp;

                                try{
                                    temp = (String) response.get("message");
                                } catch (JSONException e){
                                    throw new RuntimeException(e);
                                }

                                if(temp.equals("true")){
                                    Intent intent = new Intent(ChessLobbyPasswordActivity.this, ChessLobbyActivity.class);
                                    startActivity(intent);
                                }

                                else {
                                    try{
                                        error.setText(response.get("message").toString());
                                    } catch (JSONException e){
                                        throw new RuntimeException(e);
                                    }
                                }
                            }
                        }, new Response.ErrorListener(){
                           @Override
                           public void onErrorResponse(VolleyError Error){
                               System.out.println(error.toString());
                               error.setText("Uh Oh SpaghettiOs");
                           }
                        });
                queue.add(userObjectRequest);
            }
        });
    }
}
