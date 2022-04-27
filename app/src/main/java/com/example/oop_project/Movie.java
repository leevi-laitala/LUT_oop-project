package com.example.oop_project;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

// Container for movie information, title, start time, length, etc.
public class Movie {
    private String m_title;
    private int m_length;

    private String m_timeBegin;
    private String m_timeEnd;

    // Graphics
    //private String m_urlEventImagePortrait;
    //private String m_urlEventImageLandscape;

    Movie(String title, String begin, String end) {
        m_title = title;

        m_timeBegin = begin;
        m_timeEnd = end;
    }

    public String getTitle() {
        return m_title;
    }

    private boolean parseTime(String dateStr) { return true; } // Returns success as boolean
}
