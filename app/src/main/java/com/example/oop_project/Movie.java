package com.example.oop_project;

import android.os.Build;
import androidx.annotation.RequiresApi;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Container for movie information, title, start time, etc.
public class Movie {
    private String m_title;

    private LocalDateTime m_timeBegin;
    private LocalDateTime m_timeEnd;

    private Duration m_length;

    // Graphics
    private String m_urlEventImagePortrait;
    private String m_urlEventImageLandscape;
    private String m_urlRatingIcon;

    @RequiresApi(api = Build.VERSION_CODES.O)
    Movie(String title, String begin, String end, String portraitURL, String landscapeURL, String ratingIconURL) {
        m_title = title;
        m_urlEventImagePortrait = portraitURL;
        m_urlEventImageLandscape = landscapeURL;
        m_urlRatingIcon = ratingIconURL;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        m_timeBegin = LocalDateTime.from(dtf.parse(begin.replace("T", " ")));
        m_timeEnd = LocalDateTime.from(dtf.parse(end.replace("T", " ")));

        m_length = Duration.between(m_timeBegin, m_timeEnd);
    }

    public String getTitle() {
        return m_title;
    }

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
