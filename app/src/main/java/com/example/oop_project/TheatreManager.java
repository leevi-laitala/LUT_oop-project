package com.example.oop_project;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

@RequiresApi(api = Build.VERSION_CODES.O)
public class TheatreManager {
    private static final TheatreManager singleton = new TheatreManager();

    private final ArrayList<Theatre> m_arrTheatres = new ArrayList<>();
    private int m_selectedTheatreID; // Current state. Stores array index

    private LocalDateTime m_date;

    // Make class singleton
    public static TheatreManager getInstance( ) {
        return singleton;
    }

    private TheatreManager() {
        // URL where to read all theatres
        String m_urlTheatreAreaIDs = "https://www.finnkino.fi/xml/TheatreAreas/";

        // Read theatres from url
        readTheatreIDs(m_urlTheatreAreaIDs);

        // Set date to current date
        setDate(null);

        // By default select first theatre
        m_selectedTheatreID = 0;
    }

    // Select theatre with array index
    public void selectTheatre(int id) {
        // Check if index is out of bounds
        if (m_arrTheatres.size() > id) {
            // If not, select it
            m_selectedTheatreID = id;
        }
    }

    // Select date from which to show movies
    public void setDate(LocalDateTime dt) {
        // If given null as parameter, set date to today
        m_date = (dt != null) ? dt : LocalDateTime.now();

        // Update all theatres with new date
        for (int i = 0; i < m_arrTheatres.size(); i++) {
            m_arrTheatres.get(i).setDate(m_date);
        }
    }

    // Return array with theatre names
    public ArrayList<String> getTheatreNames() {
        ArrayList<String> names = new ArrayList<>();

        for (int i = 0; i < m_arrTheatres.size(); i++) {
            names.add(m_arrTheatres.get(i).getName());
        }

        return names;
    }

    // Get movies from current selected theatre
    public ArrayList<Movie> getMovies() {
        ArrayList<Movie> movies = m_arrTheatres.get(m_selectedTheatreID).getMovies();
        ArrayList<Movie> validMovies = new ArrayList<>();

        // Only select those movies which are shown after given date
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getBeginTime().isAfter(m_date)) {
                validMovies.add(movies.get(i));
            }
        }

        return validMovies;
    }

    // Return array of movie titles
    public ArrayList<String> getMovieTitles() {
        ArrayList<Movie> movies = getMovies();
        ArrayList<String> movieTitles = new ArrayList<>();

        for (int i = 0; i < movies.size(); i++) {
            movieTitles.add(movies.get(i).getTitle());
        }

        return movieTitles;
    }

    // Return array of movie lengths
    public ArrayList<Duration> getMovieLengths() {
        ArrayList<Movie> movies = getMovies();
        ArrayList<Duration> movieLengths = new ArrayList<>();

        for (int i = 0; i < movies.size(); i++) {
            movieLengths.add(movies.get(i).getLength());
        }

        return movieLengths;
    }

    // Return array of urls
    public ArrayList<String> getMoviePortraitURLs() {
        ArrayList<Movie> movies = getMovies();
        ArrayList<String> movieURLs = new ArrayList<>();

        for (int i = 0; i < movies.size(); i++) {
            movieURLs.add(movies.get(i).getPortraitURL());
        }

        return movieURLs;
    }

    // Return array of localdatetimes
    public ArrayList<LocalDateTime> getMovieBeginTimes() {
        ArrayList<Movie> movies = getMovies();
        ArrayList<LocalDateTime> movieBeginTimes = new ArrayList<>();

        for (int i = 0; i < movies.size(); i++) {
            movieBeginTimes.add(movies.get(i).getBeginTime());
        }

        return movieBeginTimes;
    }

    public ArrayList<LocalDateTime> getMovieEndTimes() {
        ArrayList<Movie> movies = getMovies();
        ArrayList<LocalDateTime> movieEndTimes = new ArrayList<>();

        for (int i = 0; i < movies.size(); i++) {
            movieEndTimes.add(movies.get(i).getEndTime());
        }

        return movieEndTimes;
    }

    // Parses theatre ids and names from given url, and stores them in m_arrTheatres array
    // as Theatre objects
    private void readTheatreIDs(String url) {
        try {
            // Initialize document parser
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(url); // Parse online document
            doc.getDocumentElement().normalize();

            // Add all theatres to NodeList object
            NodeList theatres = doc.getDocumentElement().getElementsByTagName("TheatreArea");

            // Iterate through all theatres
            for (int i = 0; i < theatres.getLength(); i++) {
                Node node = theatres.item(i);
                Element element = (Element)node; // Take only single theatre for parsing

                // Parse id and name
                String id = element.getElementsByTagName("ID").item(0).getTextContent();
                String name = element.getElementsByTagName("Name").item(0).getTextContent();

                // Save theatre to array
                if (name.contains(":")) {
                    m_arrTheatres.add(new Theatre(Integer.parseInt(id), name));
                }
            }
        } catch (Exception e) {
            // If something goes wrong, print stack trace
            e.printStackTrace();
        }
    }
}
