package com.example.oop_project;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Map;

public class ArchiveActivity extends AppCompatActivity {
    ArrayList<Float> m_arrRatings;
    ArrayList<String> m_arrEventIDs;

    Map<String, ?> m_mapData;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);

        // Initialize arrays for ratings and eventIDs
        m_arrRatings = new ArrayList<>();
        m_arrEventIDs = new ArrayList<>();

        // Open file from device storage
        SharedPreferences sp = getSharedPreferences("MovieArchive", Context.MODE_PRIVATE);
        m_mapData = sp.getAll(); // Save file to hashmap

        // Populate arrays from hashmap
        for (Map.Entry<String, ?> it : m_mapData.entrySet()) {
            m_arrEventIDs.add(it.getKey());
            m_arrRatings.add((Float) it.getValue());
        }

        // Construct recycler view
        constructListView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void constructListView() {
        System.out.println(m_arrEventIDs);

        // Init adapter
        ArchiveAdapt adapter = new ArchiveAdapt(this, m_arrEventIDs, m_arrRatings, position -> System.out.println("Jee jee jee"));

        // Populate recycler view
        RecyclerView recyclerView = findViewById(R.id.archiveList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
