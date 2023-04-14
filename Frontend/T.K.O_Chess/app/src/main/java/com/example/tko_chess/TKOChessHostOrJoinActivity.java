package com.example.tko_chess;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tko_chess.ultils.Const;

public class TKOChessHostOrJoinActivity extends AppCompatActivity {

    Button hostBtn;
    Button joinBtn;
    ImageButton backBtn;
    String URLConcatenation = "";
    public String lobbyKey = "";
}
