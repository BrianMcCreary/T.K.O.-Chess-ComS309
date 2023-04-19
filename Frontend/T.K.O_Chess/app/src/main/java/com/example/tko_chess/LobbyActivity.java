package com.example.tko_chess;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

import com.example.tko_chess.ultils.Const;

public class LobbyActivity extends AppCompatActivity {

	//Text Declarations
	TextView LobbyName;
	TextView ReadyStatus;
	TextView HostOptions;

	//Button Declarations
	ImageButton LobbyToHostJoin;
	Button PlayerBtn;
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
		PlayerBtn = findViewById(R.id.PlayerBtn);
		SpectatorBtn = findViewById(R.id.SpectatorBtn);
		NotReadyBtn = findViewById(R.id.NotReadyBtn);
		ReadyBtn = findViewById(R.id.ReadyBtn);
		StartGameBtn = findViewById(R.id.StartGameBtn);
		GameSettingsBtn = findViewById(R.id.GameSettingsBtn);

		//Text Initializations
		LobbyName = findViewById(R.id.LobbyNameText);
		ReadyStatus = findViewById(R.id.ReadyStatusText);
		HostOptions = findViewById(R.id.HostOptionsText);

		//String Initializations
		GameMode = getIntent().getExtras().getString("Gamemode");
		HostOrJoin = getIntent().getExtras().getString("HostOrJoin");
		LobbyCode = getIntent().getExtras().getString("LobbyCode");

		URLConcatenation = currUser.getUsername() + "/" + HostOrJoin + "/" + LobbyCode;

		Draft[] drafts = {
				new Draft_6455()
		};

		try {
			WebSocket = new WebSocketClient(new URI(Const.URL_SERVER_WEBSOCKETLOBBY + URLConcatenation), (Draft)drafts[0]) {
				@Override
				public void onOpen(ServerHandshake serverHandshake) {

				}

				@Override
				public void onMessage(String message) {

				}

				@Override
				public void onClose(int code, String reason, boolean remote) {

				}

				@Override
				public void onError(Exception e) {

				}
			};
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return;
		}

		//Connects to the websocket
		WebSocket.connect();

		//Hide and disable host options/ready status
		hideViews();

	}



	//Hides views on screen depending on the user's role in the lobby
	private void hideViews() {
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



	//Shows
}
