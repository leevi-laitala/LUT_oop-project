package com.example.oop_project;

import android.os.Build;
import androidx.annotation.RequiresApi;
import org.w3c.dom.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

// Container storing movie information
public class Theatre {
    private String m_theatreURL;

    private int m_theatreAreaID;
    private String m_theatreName;

    private LocalDateTime m_date;

    private ArrayList<Movie> m_arrMovies = new ArrayList<Movie>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    Theatre(int areaId, String name) {
        m_theatreAreaID = areaId;
        m_theatreName = name;

        m_date = LocalDateTime.now();

        m_theatreURL = "https://www.finnkino.fi/xml/Schedule/?area=%s&dt=%s"; // Contains formats "%s" for area and date
        fetchMovies();
    }

    public String getName() {
        return m_theatreName;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setDate(LocalDateTime dt) { m_date = dt; fetchMovies(); }

    public int getAreaID() { return m_theatreAreaID; }

    public ArrayList<Movie> getMovies() {
        return m_arrMovies;
    }

    // Add movie to array
    public void addMovie(Movie movie) {
        m_arrMovies.add(movie);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean fetchMovies() { // Returns success as boolean
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            System.out.println(String.format(m_theatreURL, m_theatreAreaID, m_date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
            Document doc = builder.parse(String.format(m_theatreURL, m_theatreAreaID, m_date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
            doc.getDocumentElement().normalize();

            NodeList theatres = doc.getDocumentElement().getElementsByTagName("Show");

            for (int i = 0; i < theatres.getLength(); i++) {
                Node node = theatres.item(i);
                Element element = (Element)node;

                String title = element.getElementsByTagName("Title").item(0).getTextContent();
                String dtStart = element.getElementsByTagName("dttmShowStart").item(0).getTextContent();
                String dtEnd = element.getElementsByTagName("dttmShowEnd").item(0).getTextContent();
                String portraitURL = element.getElementsByTagName("EventLargeImagePortrait").item(0).getTextContent();
                String landscapeURL = element.getElementsByTagName("EventLargeImageLandscape").item(0).getTextContent();
                String ratingIconURL = element.getElementsByTagName("RatingImageUrl").item(0).getTextContent();

                m_arrMovies.add(new Movie(title, dtStart, dtEnd, portraitURL, landscapeURL, ratingIconURL));
            }

            return true; // Success
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
