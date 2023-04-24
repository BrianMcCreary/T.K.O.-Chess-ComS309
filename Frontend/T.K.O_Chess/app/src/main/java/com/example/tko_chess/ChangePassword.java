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
 * This is where the user can change their password
 */
public class ChangePassword extends AppCompatActivity {

    ImageButton goBack; // takes user back to the profile editor screen
    Button save; // applies the changes the user made
    EditText username; // where the user will input their username
    EditText currentPassword; // where the user will input their current password
    EditText newPassword; // where the user will input their new password
    TextView checker; // displays an error message if the user made a mistake


    /**
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     *     loads the change password screen for the user
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        goBack = findViewById(R.id.backBtn6);

        goBack.setOnClickListener(new View.OnClickListener() {
            /**
             * @param view The view that was clicked.
             *
             * When pressed the user is take back to the profile screen
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangePassword.this, Profiles.class);
                startActivity(intent);
            }
        });

        save = findViewById(R.id.savebtn3);

        save.setOnClickListener(new View.OnClickListener() {
            /**
             * @param v The view that was clicked.
             * When pressed the changes the user made are applied and updated on the backend and the user is brought back to the profile editor screen
             */
            @Override
            public void onClick(View v) {
                username = (EditText) findViewById(R.id.Username);
                currentPassword = (EditText) findViewById(R.id.currentpassword);
                newPassword = (EditText) findViewById(R.id.newPassword);
                checker = (TextView) findViewById(R.id.checker2);

                String newPass = newPassword.getText().toString();
                String currentPass = currentPassword.getText().toString();
                String user = username.getText().toString();

                RequestQueue queue = Volley.newRequestQueue(ChangePassword.this);

                JsonObjectRequest changeUserRequest = new JsonObjectRequest(Request.Method.PUT, Const.URL_SERVER_CHANGEPASSWORD + user + "/" + newPass + "/" + currentPass, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String temp;

                        try{
                            temp = (String) response.get("message");
                        }catch (JSONException E){
                            throw new RuntimeException(E);
                        }

                        if(temp.equals("success")){
                            Intent intent = new Intent(ChangePassword.this, Profile_Editor.class);
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
