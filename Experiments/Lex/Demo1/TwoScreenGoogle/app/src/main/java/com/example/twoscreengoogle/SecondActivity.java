package com.example.twoscreengoogle;

import androidx.appcompat.app.AppCompatActivity;

<<<<<<< HEAD
import android.os.Bundle;
=======
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
>>>>>>> 96080ae939e775a09e6c667b9910c19a28a95957
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {
    ListView chessPieceList;
    String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        if (getIntent().hasExtra("key1")) {
            TextView tv = (TextView) findViewById(R.id.textView);
            String text = getIntent().getExtras().getString("key1");
            tv.setText(text);
        }

        Resources res = getResources();
        chessPieceList = (ListView) findViewById(R.id.listOfThings);
        items = res.getStringArray(R.array.ChessPieces);

        chessPieceList.setAdapter(new ArrayAdapter<String>(this, R.layout.my_listview_detail, items));
    }
}