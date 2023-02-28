package com.example.tko_chess;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

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

public class RegisterActivity extends AppCompatActivity {

	EditText RegUsername, RegPassword, ConfirmPassword;
	Button Register, toLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		//Text fields for users to enter username/password for their account
		RegUsername = (EditText) findViewById(R.id.RegUsernameText);
		RegPassword = (EditText) findViewById(R.id.RegPasswordText);
		ConfirmPassword = (EditText) findViewById(R.id.ConfirmPasswordText);

		//Strings containing username/password. Used to check that user does exist in database.
		String username = RegUsername.getText().toString();
		String password = RegPassword.getText().toString();
		String confirmPassword = ConfirmPassword.getText().toString();

	/*
	JSONObject user = new JSONObject();
		try {
			user.put("name", Username.getText());
			user.put("password", Password.getText());
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
	*/
	}
}
