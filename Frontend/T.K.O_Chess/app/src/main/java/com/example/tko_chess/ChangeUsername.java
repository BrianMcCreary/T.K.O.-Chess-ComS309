package com.example.tko_chess;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tko_chess.ultils.Const;

import org.json.JSONException;
import org.json.JSONObject;


public class ChangeUsername extends AppCompatActivity {

    ImageButton goBack;
    Button save;

    EditText currentUser;
    EditText userPassword;
    EditText newUser;
    TextView checker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);

        goBack = findViewById(R.id.backBtn5);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangeUsername.this, Profiles.class);
                startActivity(intent);
            }
        });

        save = findViewById(R.id.savebtn2);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   currentUser = (EditText) findViewById(R.id.currentUsername);
                   userPassword = (EditText) findViewById(R.id.currentPassword);
                   newUser = (EditText) findViewById(R.id.newUsername);
                   checker = (TextView) findViewById(R.id.checker);

                   String username = newUser.getText().toString();
                   String currentUsername = currentUser.getText().toString();
                   String password = userPassword.getText().toString();

                   JSONObject newUsername = new JSONObject();
                   try{
                       newUsername.put("username", newUser.getText());
                       newUsername.put("password", newUser.getText());
                   }
                   catch (JSONException E){
                       E.printStackTrace();
                   }

                   RequestQueue queue = Volley.newRequestQueue(ChangeUsername.this);


                   JsonObjectRequest changeUserRequest = new JsonObjectRequest(Request.Method.PUT, Const.URL_SERVER_CHANGEUSERNAME + currentUsername + "/" + username + "/" + password, null, new Response.Listener<JSONObject>() {
                       @Override
                       public void onResponse(JSONObject response) {
                           String temp;

                           try{
                               temp = (String) response.get("message");
                           }catch (JSONException E){
                               throw new RuntimeException(E);
                           }

                           if(temp.equals("success")){
                               newUser = findViewById(R.id.newUsername);
                               String name = newUser.getText().toString();
                               Intent intent = new Intent(ChangeUsername.this, Profile_Editor.class);
                               //Intent intent2 = new Intent(ChangeUsername.this, Profiles.class);
                               //intent.putExtra("key1", name);
                               startActivity(intent);
                               //startActivity(intent2);
                           }
                           else{
                               try{
                                   checker.setText(response.get("message").toString());
                               } catch (JSONException E){
                                   throw new RuntimeException(E);
                               }
                           }
                       }
                   }, new Response.ErrorListener() {
                       @Override
                       public void onErrorResponse(VolleyError error){
                           System.out.println(error.toString());
                           checker.setText("An error occured");
                       }
                   });
                   queue.add(changeUserRequest);
            }
        });
    }
}
