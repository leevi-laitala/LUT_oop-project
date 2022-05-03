package com.example.oop_project;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class TheatreManager {
    private ArrayList<Theatre> m_arrTheatres = new ArrayList<Theatre>();
    private int m_selectedTheatreID; // Current state. Stores array index

    private String m_urlTheatreAreaIDs;
    private LocalDateTime m_date;

    @RequiresApi(api = Build.VERSION_CODES.O)
    TheatreManager() {
        m_urlTheatreAreaIDs = "https://www.finnkino.fi/xml/TheatreAreas/";
        readTheatreIDs(m_urlTheatreAreaIDs);

        setDate(null);

        m_selectedTheatreID = 0;
    }

    public int nameToId(String name) {
        // TODO: Better off using hashmaps where key is either the id or the name
        for (int i = 0; i < m_arrTheatres.size(); i++) {
            if (m_arrTheatres.get(i).getName() == name) {
                return i;
            }
        }

        return -1; // Fail
    }

    public void selectTheatre(int id) {
        // Check if id exists
        for (int i = 0; i < m_arrTheatres.size(); i++) {
            if (m_arrTheatres.get(i).getAreaID() == id) {
                m_selectedTheatreID = id;
                return;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setDate(LocalDateTime dt) { // If given null as parameter, set date to today
        if (dt != null) {
            m_date = dt;
        } else {
            LocalDateTime today = LocalDateTime.now();
            m_date = today;
        }

        for (int i = 0; i < m_arrTheatres.size(); i++) {
            m_arrTheatres.get(i).setDate(m_date);
        }
    }

    public ArrayList<Theatre> getTheatres() {
        return m_arrTheatres;
    }

    public ArrayList<String> getTheatreNames() {
        ArrayList<String> names = new ArrayList<String>();

        for (int i = 0; i < m_arrTheatres.size(); i++) {
            names.add(m_arrTheatres.get(i).getName());
        }

        return names;
    }

    // Get movies from current selected theatre
    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<Movie> getMovies() {
        ArrayList<Movie> movies = m_arrTheatres.get(m_selectedTheatreID).getMovies();
        ArrayList<Movie> validMovies = new ArrayList<Movie>();

        System.out.println("Parsing valid movies");

        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getBeginTime().isAfter(m_date)) {
                validMovies.add(movies.get(i));
                System.out.println("-> Added " + movies.get(i).getTitle() + " to valid movies list!");
            }
        }

        return validMovies;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<String> getMovieTitles() {
        ArrayList<Movie> movies = getMovies();
        ArrayList<String> movieTitles = new ArrayList<String>();

        for (int i = 0; i < movies.size(); i++) {
            movieTitles.add(movies.get(i).getTitle());
        }

        return movieTitles;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<String> getMoviePortraitURLs() {
        ArrayList<Movie> movies = getMovies();
        ArrayList<String> movieURLs = new ArrayList<String>();

        for (int i = 0; i < movies.size(); i++) {
            movieURLs.add(movies.get(i).getPortraitURL());
        }

        return movieURLs;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<LocalDateTime> getMovieBeginTimes() {
        ArrayList<Movie> movies = getMovies();
        ArrayList<LocalDateTime> movieBeginTimes = new ArrayList<LocalDateTime>();

        for (int i = 0; i < movies.size(); i++) {
            movieBeginTimes.add(movies.get(i).getBeginTime());
        }

        return movieBeginTimes;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<LocalDateTime> getMovieEndTimes() {
        ArrayList<Movie> movies = getMovies();
        ArrayList<LocalDateTime> movieEndTimes = new ArrayList<LocalDateTime>();

        for (int i = 0; i < movies.size(); i++) {
            movieEndTimes.add(movies.get(i).getEndTime());
        }

        return movieEndTimes;
    }

    // Parses theatre ids and names from given url, and stores them in m_arrTheatres array
    // as Theatre objects
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void readTheatreIDs(String url) {
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

                if (name.contains(":")) {
                    m_arrTheatres.add(new Theatre(Integer.parseInt(id), name));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
