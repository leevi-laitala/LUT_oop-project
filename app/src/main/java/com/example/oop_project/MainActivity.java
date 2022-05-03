package com.example.oop_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.security.AppUriAuthenticationPolicy;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.oop_project.ui.login.LoginActivity;

import java.time.LocalDateTime;
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
        */
    }

    public void openLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void constructListView() {
        ArrayList<String> movieTitles = manager.getMovieTitles();
        ArrayList<String> moviePortraitURLs = manager.getMoviePortraitURLs();
        ArrayList<LocalDateTime> movieBeginTimes = manager.getMovieBeginTimes();
        ArrayList<LocalDateTime> movieEndTimes = manager.getMovieEndTimes();

        ListAdapt adapter = new ListAdapt(this, movieTitles, moviePortraitURLs, movieBeginTimes, movieEndTimes, new ListAdapt.ItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                //movies.get(position).getId(); gets id
                Intent movieIntent = new Intent(MainActivity.this, MovieActivity.class);
                movieIntent.putExtra("movieId",position);//id here when ready
                startActivity(movieIntent);
            }
        });
      
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
    public void onNothingSelected(AdapterView<?> parent) {}
}

