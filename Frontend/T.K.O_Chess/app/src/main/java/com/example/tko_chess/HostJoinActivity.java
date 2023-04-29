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

/**
 * @author Lex Somers
 *
 * Host or join screen where users can choost to host a new game lobby
 *      or join an existing one, specified by a lobby code.
 */
public class HostJoinActivity extends AppCompatActivity {

	//Button Declarations
	/**
	 * Takes user back to main menu.
	 */
	ImageButton HorJToMenuBtn;

	/**
	 * Takes user to lobby screen. User is the host of that new lobby.
	 */
	Button HostGameBtn;

	/**
	 * Takes user to lobby screen if the lobby specified by EditText LobbyCode exists.
	 */
	Button JoinGameBtn;

	//Text Declarations
	/**
	 * Entry field for specifying which lobby the user wishes to join.
	 */
	EditText LobbyCode;

	/**
	 * View displaying join lobby errors.
	 */
	TextView JoinError;

	//String Declarations
	/**
	 * Holds what game mode the user selected to play.
	 */
	String GameMode = "";

	/**
	 * Holds the end of the URL path mapping for any requests made.
	 */
	String URLConcatenation = "";


	/**
	 *
	 * @param savedInstanceState If the activity is being re-initialized after
	 *     previously being shut down then this Bundle contains the data it most
	 *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
	 *
	 *     Loads host and join game screen onto device.
	 */
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

		/**
		 * Takes user to main menu screen.
		 */
		HorJToMenuBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(HostJoinActivity.this, MainMenuActivity.class);
				startActivity(intent);
			}
		});

		/**
		 * Takes user to lobby screen. User is host of that new lobby.
		 */
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

		/**
		 * Takes user to lobby screen if the lobby specified by EditText LobbyCode exists.
		 */
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
