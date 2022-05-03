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
    private final String m_theatreURL;
    private final int m_theatreAreaID;
    private final String m_theatreName;

    private LocalDateTime m_date;

    // List of movies in this theatre at selected date
    private final ArrayList<Movie> m_arrMovies = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    Theatre(int areaId, String name) {
        m_theatreAreaID = areaId;
        m_theatreName = name;

        m_date = LocalDateTime.now();

        m_theatreURL = "https://www.finnkino.fi/xml/Schedule/?area=%s&dt=%s"; // Contains formats "%s" for area and date

        // Fill movies containing array
        fetchMovies();
    }

    // Return theatre name
    public String getName() {
        return m_theatreName;
    }

    // Sets date, to select what movies are shown
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setDate(LocalDateTime dt) { m_date = dt; fetchMovies(); }

    // Returns array of movies
    public ArrayList<Movie> getMovies() {
        return m_arrMovies;
    }

    // Fetches movies from this theatre, and selected date if provided
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void fetchMovies() { // Returns success as boolean
        m_arrMovies.clear();

        try {
            // Initialize document parser for xml file
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(String.format(m_theatreURL, m_theatreAreaID, m_date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
            doc.getDocumentElement().normalize();

            // Select all "Show" elements from the xml file, and put them into NodeList
            NodeList theatres = doc.getDocumentElement().getElementsByTagName("Show");

            // Iterate through the "Show" elements
            for (int i = 0; i < theatres.getLength(); i++) {
                Node node = theatres.item(i);
                Element element = (Element)node; // Take only one element at a time for parsing

                // Save parsed elements to temporary containers
                String title = element.getElementsByTagName("Title").item(0).getTextContent();
                String dtStart = element.getElementsByTagName("dttmShowStart").item(0).getTextContent();
                String dtEnd = element.getElementsByTagName("dttmShowEnd").item(0).getTextContent();

                // Get graphics sources, first try "high" resolution one, if fails, then low resolution
                String portraitURL;
                try {
                    portraitURL = element.getElementsByTagName("EventLargeImagePortrait").item(0).getTextContent();
                } catch (NullPointerException ne) {
                    portraitURL = element.getElementsByTagName("EventSmallImagePortrait").item(0).getTextContent();
                }

                // Same here
                String landscapeURL;
                try {
                    landscapeURL = element.getElementsByTagName("EventLargeImageLandscape").item(0).getTextContent();
                } catch (NullPointerException ne) {
                    landscapeURL = element.getElementsByTagName("EventSmallImageLandscape").item(0).getTextContent();
                }

                String ratingIconURL = element.getElementsByTagName("RatingImageUrl").item(0).getTextContent();
                String eventID = element.getElementsByTagName("EventID").item(0).getTextContent();

                // Add new movie instance to the array
                m_arrMovies.add(new Movie(title, dtStart, dtEnd, portraitURL, landscapeURL, ratingIconURL, eventID));
            }
        } catch (Exception e) { // If something fails, print stack trace
            e.printStackTrace();
        }
    }
}
