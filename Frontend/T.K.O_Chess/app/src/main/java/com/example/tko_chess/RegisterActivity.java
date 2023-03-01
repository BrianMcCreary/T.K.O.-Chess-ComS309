package com.example.tko_chess;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.example.tko_chess.ultils.Const;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity {

	EditText RegUsername, RegPassword, ConfirmPassword;
	TextView PasswordMatch;
	Button Register, toLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		//Text fields for users to enter username/password for their account
		RegUsername = (EditText) findViewById(R.id.RegUsernameText);
		RegPassword = (EditText) findViewById(R.id.RegPasswordText);
		ConfirmPassword = (EditText) findViewById(R.id.ConfirmPasswordText);
		PasswordMatch = (TextView) findViewById(R.id.PasswordMatchText);

		//Strings containing username/password. Used to fill username and password fields of a user object.
		String username = RegUsername.getText().toString();
		String password = RegPassword.getText().toString();
		String confirmPassword = ConfirmPassword.getText().toString();

		//Register button that creates a new user in remote server
		Register = (Button) findViewById(R.id.RegisterBtn);
		Register.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Creates a new user JsonObject that will be sent to the remote server
				JSONObject user = new JSONObject();
				try {
					user.put("username", RegUsername.getText());
					user.put("password", RegPassword.getText());
				}
				catch (JSONException e) {
					e.printStackTrace();
				}

				//Attempts to post a new user to remote server.
				JsonObjectRequest userObjectReq = new JsonObjectRequest(Request.Method.POST, Const.URL_SERVER_USERS, user,
						new Response.Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject response) {
								boolean temp;
								try {
									temp = (boolean) response.get("message");
								} catch (JSONException e) {
									throw new RuntimeException(e);
								}
								if (temp) {
									Intent intent = new Intent(RegisterActivity.this, MainMenuActivity.class);
									startActivity(intent);
								}
							}
						}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {

					}
				});
			}
		});
	}
}
