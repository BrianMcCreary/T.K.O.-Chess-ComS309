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
	ImageButton HorJToMenuBtn;
	Button HostGameBtn;
	Button JoinGameBtn;

	//Text Declarations
	EditText LobbyCode;
	TextView JoinError;

	//String Declarations
	String GameMode = "";
	String URLConcatenation = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_host_or_join);
		GameMode = getIntent().getExtras().getString("Gamemode");

		//Button Initializations
		HorJToMenuBtn = findViewById(R.id.HorJGametoMenuBtn);
		HostGameBtn = findViewById(R.id.HostGameBtn);
		JoinGameBtn = findViewById(R.id.JoinGameBtn);

		//Text Initializations
		LobbyCode = findViewById(R.id.LobbyCodeEditText);
		JoinError = findViewById(R.id.JoinErrorText);

		HorJToMenuBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(HostJoinActivity.this, MainMenuActivity.class);
				startActivity(intent);
			}
		});

		HostGameBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//Takes user to hosted lobby screen
				Intent intent = new Intent(HostJoinActivity.this, LobbyActivity.class);
				intent.putExtra("Gamemode", GameMode);
				intent.putExtra("HostOrJoin", "host");
				intent.putExtra("LobbyCode", "0");
				startActivity(intent);
			}
		});

		JoinGameBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//Concatenates the lobby code to the URL for the find lobby request
				URLConcatenation = LobbyCode.getText().toString();

				RequestQueue queue = Volley.newRequestQueue(HostJoinActivity.this);
				StringRequest FindLobbyReq = new StringRequest(Request.Method.GET, Const.URL_SERVER_LOBBYFIND + URLConcatenation, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						//If lobby exists, take user to lobby screen and join that lobby.
						if (response.equals("success")) {
							Intent intent = new Intent(HostJoinActivity.this, LobbyActivity.class);

							//Sending extra info about type of lobby and type of user joining the lobby
							intent.putExtra("Gamemode", GameMode);
							intent.putExtra("HostOrJoin", "join");
							intent.putExtra("LobbyCode", URLConcatenation);

							startActivity(intent);
						} else { //Display error message from backend
							JoinError.setText(response);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						//Display error message
						JoinError.setText("An error occurred.");
					}
				});

				//Send the request we created
				queue.add(FindLobbyReq);
			}
		});
	}
}
