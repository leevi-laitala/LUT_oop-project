package com.example.oop_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MovieActivity extends AppCompatActivity {
    Intent myIntent = getIntent();
    int movieId = myIntent.getIntExtra("movieId",0);
    private TextView infoText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        infoText = (TextView) findViewById(R.id.infoTextView);
        //infoText.setText(INSERT ID RETVIEVE INFO);

        //receives data which movies information need to be shown
    }
}