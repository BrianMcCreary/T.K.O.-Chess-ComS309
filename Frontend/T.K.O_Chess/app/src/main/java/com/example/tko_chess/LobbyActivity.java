package com.example.tko_chess;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;

import java.net.URI;
import java.net.URISyntaxException;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tko_chess.ultils.Const;

public class LobbyActivity extends AppCompatActivity {

	//Text Declarations
	TextView LobbyName;
	TextView ReadyStatus;
	TextView HostOptions;
	TextView LeaveLobbyError;

	//Button Declarations
	ImageButton LobbyToHostJoin;
	Button Player1Btn;
	Button Player2Btn;
	Button SpectatorBtn;
	ImageButton NotReadyBtn;
	ImageButton ReadyBtn;
	Button StartGameBtn;
	Button GameSettingsBtn;

	//String Declarations
	String HostOrJoin;
	String PlayerOrSpectator;
	String GameMode;
	String LobbyCode;
	String URLConcatenation;

	//LinearLayout Declarations
	LinearLayout LobbyOverlay;

	//User ready tracker
	boolean UserReady = false;

	//Get access to currently logged in user info.
	SingletonUser currUser = SingletonUser.getInstance();

	//WebSocket declarations
	private WebSocketClient WebSocket;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lobby);

		//Button Initializations
		LobbyToHostJoin = findViewById(R.id.LobbyToHostJoinBtn);
		Player1Btn = findViewById(R.id.Player1Btn);
		Player2Btn = findViewById(R.id.Player2Btn);
		SpectatorBtn = findViewById(R.id.SpectatorBtn);
		NotReadyBtn = findViewById(R.id.NotReadyBtn);
		ReadyBtn = findViewById(R.id.ReadyBtn);
		StartGameBtn = findViewById(R.id.StartGameBtn);
		GameSettingsBtn = findViewById(R.id.GameSettingsBtn);

		//Text Initializations
		LobbyName = findViewById(R.id.LobbyNameText);
		ReadyStatus = findViewById(R.id.ReadyStatusText);
		HostOptions = findViewById(R.id.HostOptionsText);
		LeaveLobbyError = findViewById(R.id.LeaveLobbyErrorText);

		//String Initializations
		GameMode = getIntent().getExtras().getString("Gamemode");
		HostOrJoin = getIntent().getExtras().getString("HostOrJoin");
		LobbyCode = getIntent().getExtras().getString("LobbyCode");
		PlayerOrSpectator = "Spectator";
		URLConcatenation = currUser.getUsername() + "/" + HostOrJoin + "/" + LobbyCode;

		//LinearLayout Initializations
		LobbyOverlay = findViewById(R.id.LobbyOverlayLinearLayout);



		//Disable the StartGameBtn initially until host gets CanStart message
		disableStartGame();



		//Beginning of WebSocket code
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		Draft[] drafts = {
				new Draft_6455()
		};

		try {
			WebSocket = new WebSocketClient(new URI(Const.URL_SERVER_WEBSOCKETLOBBY + URLConcatenation), (Draft)drafts[0]) {
				@Override
				public void onOpen(ServerHandshake serverHandshake) {
					Log.d("OPEN", "run() returned: " + "is connecting");
					System.out.println("onOpen returned");
				}

				@Override
				public void onMessage(String message) {
					Log.d("", "run() returned: " + message);
					String[] strings = message.split(" ");

					switch (strings[0]) {
						//A new user has joined the lobby and been assigned Spectator.
						case "Spectator":
							//TODO
							displayLobby();

							//Exit switch statement
							break;



						//Closes websocket, hides all buttons and displays lobby exit overlay
						case "Kicked":
						case "HostLeft":
							WebSocket.close();

							//Hides and disables all buttons
							hideAllButtons();

							//Displays overlay for host leaving
							displayExitLobbyOverlay(strings[0]);

							//Exit switch statement
							break;


						//Enables the start game button
						case "CanStart":
							enableStartGame();

							//Exit switch statement
							break;


						//Enables the start game button
						case "CannotStart":
							disableStartGame();

							//Exit switch statement
							break;
					}
				}

				@Override
				public void onClose(int code, String reason, boolean remote) {
					Log.d("CLOSE", "onClose() returned: " + reason);
					System.out.println("onClose returned");
				}

				@Override
				public void onError(Exception e) {
					Log.d("Exception:", e.getMessage().toString());
				}
			};
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return;
		}

		//Connects to the websocket
		WebSocket.connect();
		//End of WebSocket code
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


		//Takes user back to host or join screen
		LobbyToHostJoin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//If User is a player, and are readied up, then don't leave lobby.
				if ((!PlayerOrSpectator.equals("Spectator")) && (UserReady)) {
					LeaveLobbyError.setText("Please unready before leaving.");
				}
				//If user is a spectator or a player who is not readied up, then leave the lobby.
				else {
					WebSocket.close();

					Intent intent = new Intent(LobbyActivity.this, HostJoinActivity.class);
					intent.putExtra("Gamemode", GameMode);
					startActivity(intent);
				}
			}
		});



		//Changes user's role in the lobby to player 1 if possible.
		Player1Btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				WebSocket.send("SwitchToP1");
			}
		});



		//Changes user's role in the lobby to player 2 if possible.
		Player1Btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				WebSocket.send("SwitchToP2");
			}
		});



		//Changes user's role in the lobby to spectator.
		SpectatorBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				WebSocket.send("SwitchToSpectator");
			}
		});



		//Changes status of user to not ready
		NotReadyBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				WebSocket.send("UnReady");
				UserReady = false;

				//If user is not ready, enable leaving the lobby.
				enableLeaveLobby();
			}
		});



		//Changes status of user to ready
		ReadyBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				WebSocket.send("Ready");
				UserReady = true;

				//If user is ready, disable leaving the lobby.
				disableLeaveLobby();

			}
		});



		//Starts games for all players and spectators
		StartGameBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				WebSocket.send("Start " + GameMode);
				//TODO Implement moving host to correct game mode screen. Also, close the websocket
			}
		});



		//Hide and disable host options/ready status
		hideViews();
	}



	//Hides views on screen depending on the user's role in the lobby
	private void hideViews() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				//If user is not host, hide host options
				if (HostOrJoin.equals("join")) {
					//Hides host options from user.
					HostOptions.setVisibility(View.INVISIBLE);
					StartGameBtn.setVisibility(View.INVISIBLE);
					GameSettingsBtn.setVisibility(View.INVISIBLE);

					//Disables host option buttons
					StartGameBtn.setClickable(false);
					GameSettingsBtn.setClickable(false);
				} else

				//If user is spectator, hide ready status options
				if (PlayerOrSpectator.equals("spectator")) {
					//Hides ready status options from user
					ReadyStatus.setVisibility(View.INVISIBLE);
					NotReadyBtn.setVisibility(View.INVISIBLE);
					ReadyBtn.setVisibility(View.INVISIBLE);

					NotReadyBtn.setClickable(false);
					ReadyBtn.setClickable(false);
				}
			}
		});
	}



	//Disables and hides all buttons on screen
	private void hideAllButtons() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				LobbyToHostJoin.setVisibility(View.INVISIBLE);
				Player1Btn.setVisibility(View.INVISIBLE);
				Player2Btn.setVisibility(View.INVISIBLE);
				SpectatorBtn.setVisibility(View.INVISIBLE);
				NotReadyBtn.setVisibility(View.INVISIBLE);
				ReadyBtn.setVisibility(View.INVISIBLE);
				StartGameBtn.setVisibility(View.INVISIBLE);
				GameSettingsBtn.setVisibility(View.INVISIBLE);

				LobbyToHostJoin.setClickable(false);
				Player1Btn.setClickable(false);
				Player2Btn.setClickable(false);
				SpectatorBtn.setClickable(false);
				NotReadyBtn.setClickable(false);
				ReadyBtn.setClickable(false);
				StartGameBtn.setClickable(false);
				GameSettingsBtn.setClickable(false);
			}
		});
	}



	//Displays the host left overlay
	private void displayExitLobbyOverlay(String message) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				View inflatedLayout = getLayoutInflater().inflate(R.layout.lobby_exitlobby_layout, null, false);
				Button LobbyToMenuBtn = (Button) inflatedLayout.findViewById(R.id.LobbyToMenuBtn);
				TextView ExitLobbyText = (TextView) inflatedLayout.findViewById(R.id.ExitLobbyText);

				ExitLobbyText.setText(message);


				LobbyToMenuBtn.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						//Returns user to main menu
						Intent intent = new Intent(LobbyActivity.this, MainMenuActivity.class);
						startActivity(intent);
					}
				});

				LobbyOverlay.addView(inflatedLayout);
			}
		});
	}



	//Enables start game button
	private void enableStartGame() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				StartGameBtn.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.soft_blue)));
				StartGameBtn.setClickable(true);
			}
		});
	}



	//Disables start game button
	private void disableStartGame() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				StartGameBtn.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.faded_soft_blue)));
				StartGameBtn.setClickable(false);
			}
		});
	}



	//Disables leave lobby button
	private  void disableLeaveLobby() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				LobbyToHostJoin.setClickable(false);
			}
		});
	}



	//Disables leave lobby button
	private  void enableLeaveLobby() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				LobbyToHostJoin.setClickable(true);
			}
		});
	}



	//Displays the lobby's current members and related info.
	private void displayLobby() {
		RequestQueue queue = Volley.newRequestQueue(LobbyActivity.this);
		JsonArrayRequest lobbyMembersReq	 = new JsonArrayRequest(Request.Method.GET, Const.URL_SERVER_GETLOBBY + LobbyCode, null, new Response.Listener<JSONArray>() {
			@Override
			public void onResponse(JSONArray response) {
				//TODO
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {

			}
		});

		//Send the request we created
		queue.add(lobbyMembersReq);
	}
}
