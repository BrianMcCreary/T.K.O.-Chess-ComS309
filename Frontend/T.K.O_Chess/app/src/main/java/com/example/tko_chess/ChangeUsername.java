package com.example.tko_chess;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

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

/**
 * @author Zachary Scurlock
 * This is where the user can change their username
 */
public class ChangeUsername extends AppCompatActivity {

    ImageButton goBack; // takes user back to the profile editor screen
    Button save; // applies the changes the user made
    EditText currentUser; // where the user will input their current username
    EditText userPassword; // where the user will input their password
    EditText newUser; // where the user will input their new username
    TextView checker; // updates to an error message if the user made a mistake


    /**
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     *     loads the change username screen for the user
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);

        goBack = findViewById(R.id.backBtn5);

        goBack.setOnClickListener(new View.OnClickListener() {
            /**
             * @param view The view that was clicked.
             * When pressed the user is take back to the profile screen
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangeUsername.this, Profiles.class);
                startActivity(intent);
            }
        });

        save = findViewById(R.id.savebtn2);

        save.setOnClickListener(new View.OnClickListener() {
            /**
             * @param v The view that was clicked.
             * When pressed the changes the user made are applied and updated on the backend and brought back to the profile editor screen
             */
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
                               Intent intent = new Intent(ChangeUsername.this, Profile_Editor.class);
                               startActivity(intent);
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
