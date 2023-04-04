package com.example.tko_chess;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.net.URL;


/**
 * @author Lex Somers
 */
public class RegisterActivity extends AppCompatActivity {

	EditText RegUsername, RegPassword, ConfirmPassword;
	TextView RegisterError;
	Button Register, RegisterToLogin;

	Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);



		//toLogin button that takes user back to login screen.
		RegisterToLogin = (Button) findViewById(R.id.toLoginBtn);
		RegisterToLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(RegisterActivity.this, LogInActivity.class));
			}
		});



		//Register button that creates a new user in remote server
		Register = (Button) findViewById(R.id.RegisterBtn);
		Register.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Create a string holding the username to concatenate to the URL
				String URLConcatenation = "";

				//Text fields for users to enter username/password for their account
				RegUsername = (EditText) findViewById(R.id.RegUsernameText);
				RegPassword = (EditText) findViewById(R.id.RegPasswordText);
				ConfirmPassword = (EditText) findViewById(R.id.ConfirmPasswordText);
				RegisterError = (TextView) findViewById(R.id.RegisterErrorText);

				URLConcatenation += RegUsername.getText().toString();
				URLConcatenation += RegPassword.getText().toString();
				URLConcatenation += ConfirmPassword.getText().toString();

				//Create a Request Que for the JsonObjectRequest
				RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);

				//Attempts to post a new user to remote server.
				JsonObjectRequest registerObjectReq = new JsonObjectRequest(Request.Method.POST, Const.URL_SERVER_USERS + URLConcatenation, null,
						new Response.Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject response) {
								String temp;

								//Get confirmation/failure of registration message from backend. Throw error if response is not string
								try {
									temp = (String) response.get("message");
								} catch (JSONException e) {
									throw new RuntimeException(e);
								}

								//If registration was "success", take user to main menu and clear error
								if (temp.equals("success")) {

									//"Logs in" the user by setting SingletonUser to their username and password.
									SingletonUser currUser = SingletonUser.getInstance();
									currUser.updateUserObject(RegUsername.getText().toString(), context);


									RegisterError.setText("");
									Intent intent = new Intent(RegisterActivity.this, MainMenuActivity.class);
									startActivity(intent);
								}
								//else, show error message
								else {
									try {
										RegisterError.setText(response.get("message").toString());
									} catch (JSONException e) {
										throw new RuntimeException(e);
									}
								}
							}
						}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						System.out.println(error.toString());
						RegisterError.setText("onErrorResponse.");
					}
				});
				queue.add(registerObjectReq);

				URLConcatenation = "";
			}
		});
	}
}
