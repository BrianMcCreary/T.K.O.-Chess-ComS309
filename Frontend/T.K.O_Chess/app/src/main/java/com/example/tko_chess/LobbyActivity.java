package com.example.tko_chess;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.net.URI;
import java.net.URISyntaxException;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.tko_chess.ultils.Const;

/**
 * @author Lex Somers
 *
 * Lobby that users can join to spectate or play games.
 */
public class LobbyActivity extends AppCompatActivity {

	/**
	 * TextView displays ready up/unready prompt.
	 */
	TextView ReadyStatus;

	/**
	 * TextView displays host options prompt.
	 */
	TextView HostOptions;

	/**
	 * TextView displays the lobby code.
	 */
	TextView LobbyCodeText;

	/**
	 * TextView displays error messages caused when leaving the lobby.
	 */
	TextView LeaveLobbyError;

	/**
	 * TextView displays error messages caused by some event within the lobby.
	 */
	TextView LobbyError;

	/**
	 * TextView displays the most recent event that occurred within the lobby.
	 */
	TextView LobbyEvent;

	/**
	 * Button takes user back to the host or join lobby screen.
	 */
	ImageButton LobbyToHostJoin;

	/**
	 * Button changes user's player type to Player1.
	 */
	Button Player1Btn;

	/**
	 * Button changes user's player type to Player2.
	 */
	Button Player2Btn;

	/**
	 * Button changes user's player type to Spectator.
	 */
	Button SpectatorBtn;

	/**
	 * ImageButton changes user's ready status to "NotReady" if user is not a spectator.
	 */
	ImageButton NotReadyBtn;

	/**
	 * ImageButton changes user's ready status to "Ready" if user is not a spectator.
	 */
	ImageButton ReadyBtn;

	/**
	 * Button starts the game for all users in the lobby. Viewable only by the host of the lobby.
	 */
	Button StartGameBtn;

	/**
	 * Button displays game settings menu. Viewable only by the host of the lobby.
	 */
	Button GameSettingsBtn;

	/**
	 * String holds what gamemode a user is playing.
	 * Gamemodes are chess, chessboxing, or boxing.
	 */
	String GameMode = "";

	/**
	 * String stores the of the lobby the user is currently in.
	 */
	String LobbyCode = "";

	/**
	 * String stores if the user is the host of the lobby or if they joined the lobby.
	 */
	String HostOrJoin = "";

	/**
	 * String stores the user's player type.
	 * Player types are Player1, Player2, or Spectator.
	 */
	String PlayerOrSpectator = "Spectator";

	/**
	 * String stores which lobby member is currently assigned player type "Player1"
	 */
	String WhoPlayer1 = "";

	/**
	 * String stores which lobby member is currently assigned player type "Player2"
	 */
	String WhoPlayer2 = "";

	/**
	 * String stores a message about the most recent change of roles, ready status, etc within the lobby.
	 */
	String lobbyMessage = "";

	/**
	 * String stores the ending of the URL path mapping for the websocket.
	 */
	String URLConcatenation = "";

	//LinearLayout Declarations
	/**
	 * LinearLayout container for the lobby overlay displayed upon closing of the lobby.
	 */
	LinearLayout LobbyOverlay;

	/**
	 * LinearLayout container displaying all lobby members and relevant information about them.
	 * The display of the lobby members changes depending on if the user is the host or member of the lobby.
	 */
	LinearLayout LobbyMembersLayout;

	/**
	 * Boolean tracks if the user is currently readied up or not.
	 */
	boolean UserReady = false;

	/**
	 * Boolean tracks if the host can start the game or not.
	 */
	boolean CanStart = false;

	/**
	 * SingletonUser instance which stores the currently logged in user.
	 */
	SingletonUser currUser = SingletonUser.getInstance();

	/**
	 * WebSocket used for connecting to the lobby, leaving the lobby,
	 *      changing from spectator to player, changing user ready
	 *      status, and kicking other lobby members from the lobby
	 *      if user is the host.
	 */
	private WebSocketClient WebSocket;

	/**
	 *
	 * @param savedInstanceState If the activity is being re-initialized after
	 *     previously being shut down then this Bundle contains the data it most
	 *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
	 *
	 * Loads lobby screen onto device. Certain views and buttons will be disabled
	 *     or hidden based on user's player type and role within the lobby.
	 */
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
		ReadyStatus = findViewById(R.id.ReadyStatusText);
		HostOptions = findViewById(R.id.HostOptionsText);
		LobbyCodeText = findViewById(R.id.LobbyCodeText);
		LeaveLobbyError = findViewById(R.id.LeaveLobbyErrorText);
		LobbyError = findViewById(R.id.LobbyErrorText);
		LobbyEvent = findViewById(R.id.LobbyEventText);

		//String Initializations
		GameMode = getIntent().getExtras().getString("Gamemode");
		LobbyCode = getIntent().getExtras().getString("LobbyCode");
		HostOrJoin = getIntent().getExtras().getString("HostOrJoin");
		URLConcatenation = currUser.getUsername() + "/" + HostOrJoin + "/" + LobbyCode;

		//LinearLayout Initializations
		LobbyOverlay = findViewById(R.id.LobbyOverlayLinearLayout);
		LobbyMembersLayout = findViewById(R.id.LobbyLinearLayout);

		//Display lobby code if spectator.
		if (!HostOrJoin.equals("host")) {
			LobbyCodeText.setText(LobbyCode);
		}

		//Disable the StartGameBtn initially until host gets CanStart message
		disableStartGame();

		//Hide views that user shouldn't see initially
		hideOrShowViews();

		//TODO ///Disable game settings button until we implement something for it./// DELETE AFTER IMPLEMENTING
		GameSettingsBtn.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.faded_soft_blue)));
		GameSettingsBtn.setClickable(false);
		//TODO ///Disable game settings button until we implement something for it./// DELETE AFTER IMPLEMENTING

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

					//Clears lobby event and error texts
					hideEventErrorTexts();

					switch (strings[0]) {
						//Sent to client upon joining lobby
						case "JustJoined":
							String[] temp = message.split(" ");
							String[] members = temp[1].split("#");

							//Updates lobby display
							displayLobbyMembers(members);

							//Exit switch statement
							break;


						//Sent to host upon connection to websocket
						case "LobbyCode":
							//Display lobby code
							LobbyCode = strings[1];
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									LobbyCodeText.setText(LobbyCode);
								}
							});

							//Exit switch statement
							break;


						//Starts the game for everyone
						case "StartGame":
							//Disconnects from lobby
							WebSocket.close();

							//Take user to chess game
							if (GameMode.equals("Chess")) {
								Intent intent = new Intent(LobbyActivity.this, ChessActivity.class);

								//Sending extra info about type of game and user's role in that game (Spectator or player)
								intent.putExtra("UserRole", PlayerOrSpectator);
								intent.putExtra("Gamemode", GameMode);
								intent.putExtra("Player1", WhoPlayer1);
								intent.putExtra("Player2", WhoPlayer2);

								startActivity(intent);
							} else

							//Take user to chess boxing game
							if (GameMode.equals("ChessBoxing")) {
								Intent intent = new Intent(LobbyActivity.this, ChessActivity.class);

								//Sending extra info about type of game and user's role in that game (Spectator or player)
								intent.putExtra("UserRole", PlayerOrSpectator);
								intent.putExtra("Gamemode", GameMode);
								intent.putExtra("Player1", WhoPlayer1);
								intent.putExtra("Player2", WhoPlayer2);

								startActivity(intent);
							} else

							//Take user to boxing game
							if (GameMode.equals("Boxing")) {
								Intent intent = new Intent(LobbyActivity.this, BoxingActivity.class);

								//Sending extra info about type of game and user's role in that game (Spectator or player)
								intent.putExtra("UserRole", PlayerOrSpectator);
								intent.putExtra("Gamemode", GameMode);
								intent.putExtra("Player1", WhoPlayer1);
								intent.putExtra("Player2", WhoPlayer2);

								startActivity(intent);
							}

							//Exit switch statement
							break;


						//Updates screen with new ready status of user specified by strings[1]
						case "Ready":
							//Updates client side info
							userReadied(strings);

							//If user is ready, disable leaving the lobby.
							disableLeaveLobby();

							//Alerts player of lobby event
							displayLobbyEvent(strings);

							//Exit switch statement
							break;


						//Updates screen with new ready status of the specified user (strings[1]).
						case "UnReady":
							//Updates client side info
							userUnReadied(strings);

							//If user is not ready, enable leaving the lobby.
							enableLeaveLobby();

							//Alerts player of lobby event
							displayLobbyEvent(strings);

							//Exit switch statement
							break;


						//Updates screen with new PlayerType of the specified user (strings[1]).
						case "Switch":
							//Updates client side info
							switchPlayerType(strings);

							//Hides or displays ready status buttons
							hideOrShowViews();

							//Alerts player of lobby event
							displayLobbyEvent(strings);

							//Exit switch statement
							break;


						//Updates screen by removing the user who left.
						case "PlayerLeft":
							//Updates client side info
							playerLeftLobby(strings);

							//Alerts player of lobby event
							displayLobbyEvent(strings);

							//Exit switch statement
							break;


						//Updates screen by adding the new user that joined. User will be spectator by default.
						case "Spectator":
							//Tells users spectator joined
							spectatorJoined(strings);

							//Alerts player of lobby event
							displayLobbyEvent(strings);

							//Exit switch statement
							break;


						//Closes websocket and displays lobby exit overlay
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
							//Alerts player of lobby event
							displayLobbyEvent(strings);

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
					LobbyError.setText("An error occurred.");
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



		/**
		 * Takes user back to host or join screen.
		 */
		LobbyToHostJoin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//Clears lobby event and error texts
				hideEventErrorTexts();

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
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
			}
		});



		/**
		 * Changes user's role in the lobby to player 1 if possible.
		 */
		Player1Btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//Clears lobby event and error texts
				hideEventErrorTexts();

				//Change user PlayerType to player 1 if user is not already player 1
				if (!WhoPlayer1.equals(currUser.getUsername())) {
					WebSocket.send("SwitchToP1");
				}
			}
		});



		/**
		 * Changes user's role in the lobby to player 2 if possible.
		 */
		Player2Btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//Clears lobby event and error texts
				hideEventErrorTexts();

				//Change user PlayerType to player 2 if user is not already player 2
				if (!WhoPlayer2.equals(currUser.getUsername())) {
					WebSocket.send("SwitchToP2");
				}
			}
		});



		/**
		 * Changes user's role in the lobby to spectator.
		 */
		SpectatorBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//Clears lobby event and error texts
				hideEventErrorTexts();

				//Change user PlayerType to spectator if user is not already spectator
				if (!PlayerOrSpectator.equals("Spectator")) {
					WebSocket.send("SwitchToSpectate");
				}
			}
		});



		/**
		 * Changes status of user to not ready.
		 */
		NotReadyBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//Clears lobby event and error texts
				hideEventErrorTexts();

				//Change user's ready status to unready if user is not already "not ready".
				if (UserReady) {
					WebSocket.send("UnReady");
				}
			}
		});



		/**
		 * Changes status of user to ready.
		 */
		ReadyBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//Clears lobby event and error texts
				hideEventErrorTexts();

				//Change user's ready status to ready if user is not already "ready".
				if (!UserReady) {
					WebSocket.send("Ready");
				}
			}
		});



		/**
		 * Starts games for all players and spectators.
		 */
		StartGameBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//Clears lobby event and error texts
				hideEventErrorTexts();

				if (CanStart) {
					WebSocket.send("Start " + GameMode);
				}
			}
		});



		//Hide and disable host options/ready status
		hideOrShowViews();
	}



	/**
	 * Hides Lobby event text and error texts.
	 */
	private void hideEventErrorTexts() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				//Clears latest error messages
				LobbyError.setText("");
				LeaveLobbyError.setText("");
				LobbyEvent.setText("");
			}
		});
	}



	/**
	 * Hides views on screen depending on the user's role in the lobby.
	 */
	private void hideOrShowViews() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				//If user is not host, hide host options
				if (HostOrJoin.equals("join")) {
					//Hides host options from user.
					HostOptions.setVisibility(View.INVISIBLE);
					ReadyStatus.setVisibility(View.INVISIBLE);
					StartGameBtn.setVisibility(View.INVISIBLE);
					GameSettingsBtn.setVisibility(View.INVISIBLE);
					ReadyBtn.setVisibility(View.INVISIBLE);
					NotReadyBtn.setVisibility(View.INVISIBLE);

					//Disables host option buttons
					StartGameBtn.setClickable(false);
					GameSettingsBtn.setClickable(false);
					ReadyBtn.setClickable(false);
					NotReadyBtn.setClickable(false);
				}

				//If user is spectator, hide ready status options
				if (PlayerOrSpectator.equals("Spectator")) {
					//Hides ready status options from user
					ReadyStatus.setVisibility(View.INVISIBLE);
					NotReadyBtn.setVisibility(View.INVISIBLE);
					ReadyBtn.setVisibility(View.INVISIBLE);

					NotReadyBtn.setClickable(false);
					ReadyBtn.setClickable(false);
				}

				if (PlayerOrSpectator.equals("Player1") || PlayerOrSpectator.equals("Player2")) {
					//Hides ready status options from user
					ReadyStatus.setVisibility(View.VISIBLE);
					NotReadyBtn.setVisibility(View.VISIBLE);
					ReadyBtn.setVisibility(View.VISIBLE);

					NotReadyBtn.setClickable(true);
					ReadyBtn.setClickable(true);
				}
			}
		});
	}



	/**
	 * Disables and hides all buttons on screen.
	 */
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



	/**
	 * Displays the host left overlay.
	 * @param message is a string containing the information of why
	 *                the user must leave the lobby.
	 */
	private void displayExitLobbyOverlay(String message) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				View inflatedLayout = getLayoutInflater().inflate(R.layout.lobby_exitlobby_layout, null, false);
				Button LobbyToMenuBtn = (Button) inflatedLayout.findViewById(R.id.LobbyToMenuBtn);
				TextView ExitLobbyText = (TextView) inflatedLayout.findViewById(R.id.ExitLobbyText);

				//If host left, display host left message
				if (message.equals("HostLeft")) {
					ExitLobbyText.setText("The host has left the lobby.");
				} else

				//If kicked, display kicked message
				if (message.equals("Kicked")) {
					ExitLobbyText.setText("You have been kicked.");
				}


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



	/**
	 * Enables start game button.
	 */
	private void enableStartGame() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				CanStart = true;
				StartGameBtn.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.soft_blue)));
				StartGameBtn.setClickable(true);
			}
		});
	}



	/**
	 * Disables start game button.
	 */
	private void disableStartGame() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				CanStart = false;
				StartGameBtn.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.faded_soft_blue)));
				StartGameBtn.setClickable(false);
			}
		});
}


	/**
	 * Disables leave lobby button.
	 */
	private  void disableLeaveLobby() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				LobbyToHostJoin.setClickable(false);
			}
		});
	}



	/**
	 * Disables leave lobby button.
	 */
	private void enableLeaveLobby() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				LobbyToHostJoin.setClickable(true);
			}
		});
	}



	/**
	 * Switches client side data for role that was switched.
	 * @param strings is a string array containing the information
	 *                which lobby member switched to what new role.
	 */
	private void switchPlayerType(String[] strings) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				String temp = currUser.getUsername();
				//If user who changed PlayerType was currUser, store their new PlayerType.
				if (strings[3].equals(currUser.getUsername())) {
					PlayerOrSpectator = strings[2];
				}

				//If player 1 is the user changing their PlayerType, then update WhoPlayer1
				if (strings[3].equals(WhoPlayer1)) {
					WhoPlayer1 = "";
				}

				//If player 2 is the user changing their PlayerType, then update WhoPlayer2
				if (strings[3].equals(WhoPlayer2)) {
					WhoPlayer2 = "";
				}

				//If user is changing to player 1, update WhoPlayer1
				if (strings[2].equals("Player1")) {
					WhoPlayer1 = strings[3];
				}

				//If user is changing to player 2, update WhoPlayer2
				if (strings[2].equals("Player2")) {
					WhoPlayer2 = strings[3];
				}

				//Change lobby member's playerType icon
				int numChildren = LobbyMembersLayout.getChildCount();
				for (int i = 0; i < numChildren; i++) {
					LinearLayout member = (LinearLayout) LobbyMembersLayout.getChildAt(i);
					TextView memberName = (TextView) member.getChildAt(0);

					//If this member object is the member who switched, then...
					if (memberName.getText().equals(strings[3])) {
						ImageView readyOrSpectator = (ImageView) member.getChildAt(1);

						//If user is switching to a player, change spectator image to not ready image.
						if (strings[2].equals("Player1") || strings[2].equals("Player2")) {
							readyOrSpectator.setImageResource(R.drawable.notreadystatus);
						} else

						//If user is switching to spectator change ready image to spectator image.
						if (strings[2].equals("Spectator")) {
							readyOrSpectator.setImageResource(R.drawable.spectator);
						}

					}
				}
			}
		});
	}



	/**
	 * Updates client info about user ready status.
	 * @param strings is a string array containing the information of
	 *                which user unreadied.
	 */
	private void userUnReadied(String[] strings) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				String temp = currUser.getUsername();

				//If user who changed ready status was currUser, store their new ready status.
				if (strings[1].equals(currUser.getUsername())) {
					UserReady = false;
				}

				//Change lobby member's playerType icon
				int numChildren = LobbyMembersLayout.getChildCount();
				for (int i = 0; i < numChildren; i++) {
					LinearLayout member = (LinearLayout) LobbyMembersLayout.getChildAt(i);
					TextView memberName = (TextView) member.getChildAt(0);

					//If this member object is the member who unreadied, then...
					if (memberName.getText().equals(strings[1])) {
						ImageView readyStatus = (ImageView) member.getChildAt(1);
						readyStatus.setImageResource(R.drawable.notreadystatus);
					}
				}
			}
		});
	}



	/**
	 * Updates client info about user ready status.
	 * @param strings is a string array containing the information of
	 * 	              which user readied.
	 */
	private void userReadied(String[] strings) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				String temp = currUser.getUsername();

				//If user who changed ready status was currUser, store their new ready status.
				if (strings[1].equals(currUser.getUsername())) {
					UserReady = true;
				}

				//Change lobby member's playerType icon
				int numChildren = LobbyMembersLayout.getChildCount();
				for (int i = 0; i < numChildren; i++) {
					LinearLayout member = (LinearLayout) LobbyMembersLayout.getChildAt(i);
					TextView memberName = (TextView) member.getChildAt(0);

					//If this member object is the member who readied, then...
					if (memberName.getText().equals(strings[1])) {
						ImageView readyStatus = (ImageView) member.getChildAt(1);
						readyStatus.setImageResource(R.drawable.readystatus);
					}
				}
			}
		});
	}



	/**
	 * Updates client info about players.
	 * @param strings is a string array containing the information of
	 *                which lobby member left the lobby.
	 */
	private void playerLeftLobby(String[] strings) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				//Updates who is player 1
				if (strings[1].equals("Player1")) {
					WhoPlayer1 = "";
				} else

					//Updates who is player 2
					if (strings[1].equals("Player2")) {
						WhoPlayer2 = "";
					}

				//Remove member visual from lobby
				int numChildren = LobbyMembersLayout.getChildCount();
				for (int i = 0; i < numChildren; i++) {
					LinearLayout member = (LinearLayout) LobbyMembersLayout.getChildAt(i);
					TextView memberName = (TextView) member.getChildAt(0);

					//If this member object is the member who left, then...
					if (memberName.getText().equals(strings[2])) {
						LobbyMembersLayout.removeViewAt(i);
					}
				}
			}
		});
	}



	/**
	 * Updates client that spectator has joined.
	 * @param strings is a string array containing the information of
	 *                which user has just joined the lobby.
	 */
	private void spectatorJoined(String[] strings) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				//Add host visual
				if (HostOrJoin.equals("host")) {
					//Create member object
					View newMember = getLayoutInflater().inflate(R.layout.lobby_host_layout, null, false);
					TextView MemberNameText = (TextView) newMember.findViewById(R.id.MemberNameTextView);
					ImageView MemberReadyStatus = (ImageView) newMember.findViewById(R.id.ReadyStatusImageView);
					Button KickMemberBtn = (Button) newMember.findViewById(R.id.KickMemberBtn);

					//Set name and spectator image
					MemberNameText.setText(strings[1]);
					MemberReadyStatus.setImageResource(R.drawable.spectator);

					//Kicks user from lobby
					KickMemberBtn.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							WebSocket.send("Kick " + strings[1]);
						}
					});

					//Add the new member object to the screen.
					LobbyMembersLayout.addView(newMember);
				} else

				//Add member visual
				if (HostOrJoin.equals("join")) {
					//Create member object
					View newMember = getLayoutInflater().inflate(R.layout.lobby_member_layout, null, false);
					TextView MemberNameText = (TextView) newMember.findViewById(R.id.MemberTextView);
					ImageView MemberReadyStatus = (ImageView) newMember.findViewById(R.id.PlayerStatusImageView);

					//Set name and spectator image
					MemberNameText.setText(strings[1]);
					MemberReadyStatus.setImageResource(R.drawable.spectator);

					//Add the new member object to the screen.
					LobbyMembersLayout.addView(newMember);
				}
			}
		});
	}



	/**
	 * Displays lobby event at top of screen.
	 * @param strings is a string array containing the information of
	 *                what change in the lobby status has just occurred.
	 */
	private void displayLobbyEvent(String[] strings) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				switch (strings[0]) {
					case "Ready":
						//Tell users who readied up
						lobbyMessage = strings[1] + " has readied-up.";
						LobbyEvent.setText(lobbyMessage);
						break;


					case "UnReady":
						//Tell users who unreadied
						lobbyMessage = strings[1] + " has un-readied.";
						LobbyEvent.setText(lobbyMessage);
						break;


					case "Switch":
						//Tell users who switched to what
						lobbyMessage = strings[3] + " (" + strings[1] + ")" + " switched to " + strings[2] + ".";
						LobbyEvent.setText(lobbyMessage);
						break;


					case "Spectator":
						//Tell users who joined
						lobbyMessage = strings[1] + " has joined.";
						LobbyEvent.setText(lobbyMessage);
						break;


					case "PlayerLeft":
						//Tell users who left
						lobbyMessage = strings[2] + " (" + strings[1] + ")" + " has left.";
						LobbyEvent.setText(lobbyMessage);
						break;


					case "CanStart":
						//Tell host both players are ready
						lobbyMessage = "Both players ready.";
						LobbyEvent.setText(lobbyMessage);
						break;
				}
			}
		});
	}



	/**
	 * Displays who is in the lobby and what their corresponding player type
	 *     and or ready status are.
	 * @param listMembers is a string array containing the information of
	 *                    all lobby members, their player type, and their
	 *                    ready status if applicable.
	 */
	private void displayLobbyMembers(String[] listMembers) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < listMembers.length; i++) {
					String[] member = listMembers[i].split("[.]");

					//Display members for host
					if (HostOrJoin.equals("host")) {
						//Create member object
						View newMember = getLayoutInflater().inflate(R.layout.lobby_host_layout, null, false);
						TextView MemberNameText = (TextView) newMember.findViewById(R.id.MemberNameTextView);
						ImageView MemberReadyStatus = (ImageView) newMember.findViewById(R.id.ReadyStatusImageView);
						Button KickMemberBtn = (Button) newMember.findViewById(R.id.KickMemberBtn);

						//Display name of member
						MemberNameText.setText(member[0]);

						//Display spectator image
						if (member[1].equals("Spectator")) {
							MemberReadyStatus.setImageResource(R.drawable.spectator);
						} else

							//Display ready or not ready image
							if (member[1].equals("Player1") || member[1].equals("Player2")) {
								if (member[2].equals("NotReady")) {
									MemberReadyStatus.setImageResource(R.drawable.notreadystatus);
								} else

								if (member[2].equals("Ready")) {
									MemberReadyStatus.setImageResource(R.drawable.readystatus);
								}
							}

						//Kicks user from lobby
						KickMemberBtn.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View view) {
								WebSocket.send("Kick " + member[0]);
							}
						});

						//Add the new member object to the screen.
						LobbyMembersLayout.addView(newMember);
					} else

					//Display members for member
					if (HostOrJoin.equals("join")) {
						//Create member object
						View newMember = getLayoutInflater().inflate(R.layout.lobby_member_layout, null, false);
						TextView MemberNameText = (TextView) newMember.findViewById(R.id.MemberTextView);
						ImageView MemberReadyStatus = (ImageView) newMember.findViewById(R.id.PlayerStatusImageView);

						//Display name of member
						MemberNameText.setText(member[0]);

						//Display spectator image
						if (member[1].equals("Spectator")) {
							MemberReadyStatus.setImageResource(R.drawable.spectator);
						} else

							//Display ready or not ready image
							if (member[1].equals("Player1") || member[1].equals("Player2")) {
								if (member[2].equals("NotReady")) {
									MemberReadyStatus.setImageResource(R.drawable.notreadystatus);
								} else

								if (member[2].equals("Ready")) {
									MemberReadyStatus.setImageResource(R.drawable.readystatus);
								}
							}

						//Add the new member object to the screen.
						LobbyMembersLayout.addView(newMember);
					}
				}
			}
		});
	}
}