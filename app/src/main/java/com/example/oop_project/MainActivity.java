package com.example.oop_project;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements OnItemSelectedListener {
    // Dialog for picking date
    private DatePickerDialog datePickerDialog;

    // Singleton
    TheatreManager manager;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Login button, used as archive button
        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(view -> openArchiveActivity());
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Get singleton instance of TheatreManager
        manager = TheatreManager.getInstance();

        // Init theatre dropdown menu
        Spinner spinner = findViewById(R.id.theatreList);
        spinner.setOnItemSelectedListener(this);

        // Get theatre names for dropdown menu
        ArrayList<String> theatreNames = manager.getTheatreNames();

        // Data adapter to display arraylist in dropdown menu
        ArrayAdapter<String> dataAdapt = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, theatreNames);
        spinner.setAdapter(dataAdapt);

        // Init date picker popup
        initDatePicker();
    }

    // Used to open archive activity
    public void openArchiveActivity() {
        Intent intent = new Intent(this, ArchiveActivity.class);
        startActivity(intent);
    }

    // Construct movie list
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void constructListView() {
        ArrayList<String> movieTitles = manager.getMovieTitles();
        ArrayList<String> moviePortraitURLs = manager.getMoviePortraitURLs();
        ArrayList<LocalDateTime> movieBeginTimes = manager.getMovieBeginTimes();
        ArrayList<LocalDateTime> movieEndTimes = manager.getMovieEndTimes();
        ArrayList<Duration> movieLengths = manager.getMovieLengths();

        // Init custom adapter for recycler view, wiht click event callback to start movie activity
        ListAdapt adapter = new ListAdapt(this, movieTitles, moviePortraitURLs, movieBeginTimes, movieEndTimes, movieLengths, position -> {
            Intent movieIntent = new Intent(MainActivity.this, MovieActivity.class);
            movieIntent.putExtra("eventID", manager.getMovies().get(position).getEventIDUrl()); // Gets selected movie's event id, which can be parsed later
            startActivity(movieIntent);
        });

        RecyclerView recyclerView = findViewById(R.id.movieList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // When selecting theatre from the list
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Update theatre manager with current theatre
        manager.selectTheatre(position);

        // Repopulate movie list with new theatre
        try {
            constructListView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Required to be overridden when overriding onItemSelected
    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    // Initialize date picker popup
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initDatePicker()
    {
        // Callback for when pressing ok button on the dialog
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;

            // Parse date from selected date
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy.M.d");
            LocalDateTime date = LocalDate.from(format.parse(year + "." + month + "." + day)).atStartOfDay();

            // Update manager with new date
            manager.setDate(date);

            // Repopulate movie list using new date
            try {
                constructListView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        // Init calendar instance
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 365);
    }

    // Launch popup
    public void openDatePicker(View view) {
        datePickerDialog.show();
    }
}

