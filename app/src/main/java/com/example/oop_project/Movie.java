package com.example.oop_project;

import android.os.Build;
import androidx.annotation.RequiresApi;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Container for movie information, title, start time, etc.
public class Movie {
    private String m_title;

    private LocalDateTime m_timeBegin;
    private LocalDateTime m_timeEnd;

    // Graphics
    private String m_urlEventImagePortrait;
    //private String m_urlEventImageLandscape;

    @RequiresApi(api = Build.VERSION_CODES.O)
    Movie(String title, String begin, String end, String portraitURL) {
        m_title = title;
        m_urlEventImagePortrait = portraitURL;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        m_timeBegin = LocalDateTime.from(dtf.parse(begin.replace("T", " ")));
        m_timeEnd = LocalDateTime.from(dtf.parse(end.replace("T", " ")));
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

    public int getLength() {
        // TODO: Maths
        return 0;
    }
}
