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
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.example.oop_project.ui.login.LoginActivity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnItemSelectedListener {
    private Button loginButton;
    private Button dateButton;
    private DatePickerDialog datePickerDialog;

    TheatreManager manager;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Login activity
        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openArchiveActivity();
            }
        });
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        manager = new TheatreManager();

        Spinner spinner = (Spinner)findViewById(R.id.theatreList);
        spinner.setOnItemSelectedListener(this);

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

        initDatePicker();

        dateButton = findViewById(R.id.selectDateButton);
    }

    public void openLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void openArchiveActivity() {
        Intent intent = new Intent(this, ArchiveActivity.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void constructListView() {
        ArrayList<String> movieTitles = manager.getMovieTitles();
        ArrayList<String> moviePortraitURLs = manager.getMoviePortraitURLs();
        ArrayList<LocalDateTime> movieBeginTimes = manager.getMovieBeginTimes();
        ArrayList<LocalDateTime> movieEndTimes = manager.getMovieEndTimes();

        ListAdapt adapter = new ListAdapt(this, movieTitles, moviePortraitURLs, movieBeginTimes, movieEndTimes, new ListAdapt.ItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                Intent movieIntent = new Intent(MainActivity.this, MovieActivity.class);
                movieIntent.putExtra("eventID", manager.getMovies().get(position).getEventIDUrl()); // Gets selected movie's event id, which can be parsed later
                startActivity(movieIntent);
            }
        });
      
        RecyclerView recyclerView = findViewById(R.id.movieList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;

                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy.M.d");
                LocalDateTime date = LocalDate.from(format.parse(year + "." + month + "." + day)).atStartOfDay();

                manager.setDate(date);
                try {
                    constructListView();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 365);
    }

    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }
}

