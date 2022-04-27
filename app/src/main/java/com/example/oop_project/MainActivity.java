package com.example.oop_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

import com.example.oop_project.ui.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnItemSelectedListener {
    private Button loginButton;

    ArrayList<Theatre> arrTheatres = new ArrayList<Theatre>(); // Test array containing theatre objects
    TheatreManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Login activity
        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLoginActivity();
            }
        });
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);



        manager = new TheatreManager();

        Spinner spinner = (Spinner)findViewById(R.id.theatreList);
        spinner.setOnItemSelectedListener(this);
        List<String> theaterIds = new ArrayList<String>();

        ArrayList<String> theatreNames = manager.getTheatreNames();

        ArrayAdapter<String> dataAdapt = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, theatreNames);
        spinner.setAdapter(dataAdapt);

        /*
        EditText date=(EditText)findViewById(R.id.editTextDate);
        date.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    dateStr = date.getText().toString();
                    System.out.println(dateStr + " " + dateStr.isEmpty());
                    return true;
                }
                return false;
            }
        });

        EditText time=(EditText)findViewById(R.id.editTextTimeStart);
        time.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (time.getText().toString().isEmpty()) {
                        timeBeginStr = "";
                        return true;
                    }
                    timeBeginStr = time.getText().toString() + ":00";

                    try {
                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;
                        timeStart = LocalTime.parse(timeBeginStr, dateTimeFormatter);
                        System.out.println("ANNETTU START: " + timeStart);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            }
        });

        EditText time2=(EditText)findViewById(R.id.editTextTimeEnd);
        time2.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (time2.getText().toString().isEmpty()) {
                        timeEndStr = "";
                        return true;
                    }

                    timeEndStr = time2.getText().toString() + ":00";

                    try {
                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;
                        timeEnd = LocalTime.parse(timeEndStr, dateTimeFormatter);
                        System.out.println("ANNETTU END: " + timeEnd);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            }
        });
        */
    }

    public void openLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void constructListView() {
        ArrayList<Movie> movies = manager.getMovies();

        System.out.println("Movies amount: " + movies.size());

        ArrayList<String> movieTitles = new ArrayList<String>();

        for (int i = 0; i < movies.size(); i++) {
            movieTitles.add(movies.get(i).getTitle());
        }

        System.out.println(movieTitles);

        ListAdapt adapter = new ListAdapt(this, movieTitles);
        RecyclerView recyclerView = findViewById(R.id.movieList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        manager.selectTheatre(position);

        try {
            constructListView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Jofa
    }
}

