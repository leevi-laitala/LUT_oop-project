package com.example.oop_project;
/*
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.oop_project.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
*/


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View.OnKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.w3c.dom.*;

import javax.xml.parsers.*;



public class MainActivity extends AppCompatActivity implements OnItemSelectedListener {
    ArrayList<String> arrIds = new ArrayList<String>();
    ArrayList<String> arrNames = new ArrayList<String>();
    ArrayList<String> arrStarts = new ArrayList<String>();
    ArrayList<String> arrEnds = new ArrayList<String>();
    ArrayList<String> arrTitles = new ArrayList<String>();

    String dateStr = "";
    String timeBeginStr = "";
    String timeEndStr = "";
    LocalTime timeStart;
    LocalTime timeEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Spinner spinner = (Spinner)findViewById(R.id.theaters);
        spinner.setOnItemSelectedListener(this);
        List<String> theaterIds = new ArrayList<String>();

        String url = "https://www.finnkino.fi/xml/TheatreAreas/";
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(url);
            doc.getDocumentElement().normalize();

            NodeList theatres = doc.getDocumentElement().getElementsByTagName("TheatreArea");

            for (int i = 0; i < theatres.getLength(); i++) {
                Node node = theatres.item(i);
                Element element = (Element)node;

                String id = element.getElementsByTagName("ID").item(0).getTextContent();
                String name = element.getElementsByTagName("Name").item(0).getTextContent();

                arrIds.add(id);
                arrNames.add(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> dataAdapt = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrNames);
        spinner.setAdapter(dataAdapt);

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
    }

    public void constructListView() {
        ListAdapt adapter = new ListAdapt(this, arrTitles, arrStarts, arrEnds);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        String url = "";

        try {
            if (dateStr.isEmpty()) {
                url = String.format("https://www.finnkino.fi/xml/Schedule/?area=%s", arrIds.get(arrNames.indexOf(item)));
            } else {
                url = String.format("https://www.finnkino.fi/xml/Schedule/?area=%s&dt=%s.%s.%s", arrIds.get(arrNames.indexOf(item)), dateStr.substring(8, 10), dateStr.substring(5, 7), dateStr.substring(0, 4));
                System.out.println(url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        arrStarts.clear();
        arrEnds.clear();
        arrTitles.clear();

        LocalTime starts;
        LocalTime ends;

        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(url);
            doc.getDocumentElement().normalize();

            NodeList theatres = doc.getDocumentElement().getElementsByTagName("Show");

            for (int i = 0; i < theatres.getLength(); i++) {
                Node node = theatres.item(i);
                Element element = (Element)node;

                String name = element.getElementsByTagName("Title").item(0).getTextContent();
                String start = element.getElementsByTagName("dttmShowStart").item(0).getTextContent();
                String end = element.getElementsByTagName("dttmShowEnd").item(0).getTextContent();

                if (!timeBeginStr.isEmpty() || !timeEndStr.isEmpty()) {
                    try {
                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;
                        starts = LocalTime.parse(start.substring(11, 16).replace("-", ":"), dateTimeFormatter);
                        ends = LocalTime.parse(end.substring(11, 16).replace("-", ":"), dateTimeFormatter);

                        if (starts.isAfter(timeStart) && starts.isBefore(timeEnd)) {
                            //System.out.println("Movie begins at " + starts + " which is after " + timeStart + " , and ends at " + ends + " which is before " + timeEnd);
                            arrStarts.add(start);
                            arrEnds.add(end);
                            arrTitles.add(name);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    arrStarts.add(start);
                    arrEnds.add(end);
                    arrTitles.add(name);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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



class ListAdapt extends RecyclerView.Adapter<ListAdapt.ViewHolder> {
    ArrayList<String> arrTitle, arrStart, arrEnd;
    Context context;

    public ListAdapt(Context cx, ArrayList<String> titles, ArrayList<String> starts, ArrayList<String> ends) {
        context = cx;
        arrTitle = titles;
        arrStart = starts;
        arrEnd = ends;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(arrTitle.get(position));
        holder.start.setText("Starts at " + arrStart.get(position).substring(11, 16) + " " + arrStart.get(position).substring(0, 10));
        holder.end.setText("Ends at " + arrEnd.get(position).substring(11, 16) + " " + arrStart.get(position).substring(0, 10));
    }

    @Override
    public int getItemCount() {
        return arrTitle.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, start, end;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            start = itemView.findViewById(R.id.start);
            end = itemView.findViewById(R.id.end);
        }
    }
}
