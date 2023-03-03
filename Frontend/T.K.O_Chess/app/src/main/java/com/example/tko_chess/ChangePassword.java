package com.example.tko_chess;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.tko_chess.ultils.Const;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.w3c.dom.Text;

public class ChangePassword extends AppCompatActivity {

    ImageButton goBack;
    Button save;
    EditText username;
    EditText currentPassword;
    EditText newPassword;
    TextView checker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        goBack = findViewById(R.id.backBtn6);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangePassword.this, Profiles.class);
                startActivity(intent);
            }
        });

        save = findViewById(R.id.savebtn3);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = (EditText) findViewById(R.id.Username);
                currentPassword = (EditText) findViewById(R.id.currentpassword);
                newPassword = (EditText) findViewById(R.id.newPassword);
                checker = (TextView) findViewById(R.id.checker2);

                String newPass = newPassword.getText().toString();
                String currentPass = currentPassword.getText().toString();
                String user = username.getText().toString();

//                JSONObject newPassword = new JSONObject();
//                try{
//                    newPassword.put("username", newPass.getText());
//                    newPassword.put("password", newPass.getText());
//                }
//                catch (JSONException E){
//                    E.printStackTrace();
//                }

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
