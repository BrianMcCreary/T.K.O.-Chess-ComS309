package com.example.tko_chess;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tko_chess.ultils.Const;

import org.w3c.dom.Text;

/**
 * @author Lex Somers
 * This is where the user will be able to choose whether to change their username or password
 */
public class EditProfileActivity extends AppCompatActivity {

    /**
     * Takes users back to profile page.
     */
    ImageButton EditProfileToProfile; // takes user back to profile screen

    Button UpdateUsername;
    Button UpdatePassword;

    EditText CurrUsername;
    EditText CurrPassword;
    EditText NewUsername;
    EditText NewPassword;

    TextView ErrorMessage;
    TextView ChangeSuccess;

    String URLConcatenation;

    Context context = this;

    /**
     * SingletonUser instance which stores the currently logged in user.
     */
    SingletonUser currUser = SingletonUser.getInstance();


    /**
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     *     loads the profile editor screen for the user.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        EditProfileToProfile = findViewById(R.id.EditProfileToProfile);
        UpdateUsername = findViewById(R.id.UpdateUsernameBtn);
        UpdatePassword = findViewById(R.id.UpdatePasswordBtn);
        CurrUsername = findViewById(R.id.CurrUsernameEditText);
        CurrPassword = findViewById(R.id.CurrPasswordEditText);
        NewUsername = findViewById(R.id.NewUsernameEditText);
        NewPassword = findViewById(R.id.NewPasswordEditText);
        ErrorMessage = findViewById(R.id.EditProfileErrorText);
        ChangeSuccess = findViewById(R.id.ChangeSuccessfulText);

        EditProfileToProfile.setOnClickListener(new View.OnClickListener() {
            /**
             * @param view The view that was clicked.
             * When pressed the user is taken back to the profile screen
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        UpdateUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //If security username and password info is correct
                if (currUser.getUsername().equals(CurrUsername.getText().toString()) && currUser.getPassword().equals(CurrPassword.getText().toString())) {
                    URLConcatenation = CurrUsername.getText().toString() + "/" + CurrPassword.getText().toString() + "/" + NewUsername.getText().toString();

                    //Request que used to send JSON requests
                    RequestQueue queue = Volley.newRequestQueue(EditProfileActivity.this);

                    StringRequest ChangeUsernameReq = new StringRequest(Request.Method.PUT, Const.URL_SERVER_CHANGEUSERNAME + URLConcatenation, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("success")) {
                                ChangeSuccess.setText("Username successfully changed.");
                                currUser.updateUserObject(NewUsername.getText().toString(), context);
                                CurrUsername.setText("");
                                NewUsername.setText("");
                                ErrorMessage.setText("");
                            } else { //Display error message from backend
                                ErrorMessage.setText("Could not change username.");
                            }
						}
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Display error message
                            ErrorMessage.setText("An error occurred.");
                        }
                    });

                    queue.add(ChangeUsernameReq);
                } else {
                    ErrorMessage.setText("Username and password did not match.");
                }
            }
        });

        UpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //If security username and password info is correct
                if (currUser.getUsername().equals(CurrUsername.getText().toString()) && currUser.getPassword().equals(CurrPassword.getText().toString())) {
                    URLConcatenation = CurrUsername.getText().toString() + "/" + CurrPassword.getText().toString() + "/" + NewPassword.getText().toString();

                    //Request que used to send JSON requests
                    RequestQueue queue = Volley.newRequestQueue(EditProfileActivity.this);

                    StringRequest ChangePasswordReq = new StringRequest(Request.Method.PUT, Const.URL_SERVER_CHANGEPASSWORD + URLConcatenation, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("success")) {
                                ChangeSuccess.setText("Password successfully changed.");
                                currUser.updateUserObject(currUser.getUsername(), context);
                                CurrPassword.setText("");
                                NewPassword.setText("");
                                ErrorMessage.setText("");
                            } else { //Display error message from backend
                                ErrorMessage.setText("Could not change password.");
                            }
						}
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Display error message
                            ErrorMessage.setText("An error occurred.");
                        }
                    });

                    queue.add(ChangePasswordReq);
                } else {
                    ErrorMessage.setText("Username and password did not match.");
                }
            }
        });
    }
}
