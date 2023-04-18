package com.example.tko_chess;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tko_chess.ultils.Const;

import androidx.appcompat.app.AppCompatActivity;

public class HostJoinActivity extends AppCompatActivity {

	//Button Declarations
	Button HostGameBtn;
	Button JoinGameBtn;

	//EditText Declarations
	EditText LobbyName;
	EditText LobbyCode;

	//String Declarations
	String GameMode = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_host_or_join);
		GameMode = getIntent().getExtras().getString("Gamemode");

		//Button Initializations
		HostGameBtn = findViewById(R.id.HostGameBtn);
		JoinGameBtn = findViewById(R.id.JoinGameBtn);

		LobbyName = findViewById(R.id.LobbyNameEditText);
		LobbyCode = findViewById(R.id.LobbyCodeEditText);

		HostGameBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

			}
		});
		
		JoinGameBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

			}
		});
	}
}
