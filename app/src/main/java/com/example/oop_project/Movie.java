package com.example.oop_project;

import android.os.Build;
import androidx.annotation.RequiresApi;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Container for movie information, title, start time, etc.
public class Movie {
    private final String m_title;

    private final LocalDateTime m_timeBegin;
    private final LocalDateTime m_timeEnd;

    private final Duration m_length;

    // Graphics
    private final String m_urlEventImagePortrait;
    private final String m_eventID;

    @RequiresApi(api = Build.VERSION_CODES.O)
    Movie(String title, String begin, String end, String portraitURL, String landscapeURL, String ratingIconURL, String eventid) {
        m_title = title;
        m_urlEventImagePortrait = portraitURL;
        m_eventID = eventid;

        // Parse date and time from strings to localdatetime objects
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        m_timeBegin = LocalDateTime.from(dtf.parse(begin.replace("T", " ")));
        m_timeEnd = LocalDateTime.from(dtf.parse(end.replace("T", " ")));

        // Calculate difference between datetime objects
        m_length = Duration.between(m_timeBegin, m_timeEnd);
    }

    // Getters and setters below

    public String getTitle() {
        return m_title;
    }

    public String getEventIDUrl() { return "https://www.finnkino.fi/xml/Schedule/?EventID=" + m_eventID; }

    public String getPortraitURL() {
        return m_urlEventImagePortrait;
    }

    public LocalDateTime getBeginTime() {
        return m_timeBegin;
    }

    public LocalDateTime getEndTime() {
        return m_timeEnd;
    }

    public Duration getLength() {
        return m_length;
    }
}
