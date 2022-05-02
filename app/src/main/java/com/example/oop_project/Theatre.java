package com.example.oop_project;

import android.os.Build;
import androidx.annotation.RequiresApi;
import org.w3c.dom.*;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

// Container storing movie information
public class Theatre {
    private String m_theatreURL;

    private int m_theatreAreaID;
    private String m_theatreName;

    private ArrayList<Movie> m_arrMovies = new ArrayList<Movie>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    Theatre(int areaId, String name) {
        m_theatreAreaID = areaId;
        m_theatreName = name;

        m_theatreURL = "https://www.finnkino.fi/xml/Schedule/?area=%s"; // Contains format "%s"
        fetchMovies();
    }

    public String getName() {
        return m_theatreName;
    }

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
            Document doc = builder.parse(String.format(m_theatreURL, m_theatreAreaID));
            doc.getDocumentElement().normalize();

            NodeList theatres = doc.getDocumentElement().getElementsByTagName("Show");

            for (int i = 0; i < theatres.getLength(); i++) {
                Node node = theatres.item(i);
                Element element = (Element)node;

                String title = element.getElementsByTagName("Title").item(0).getTextContent();
                String dtStart = element.getElementsByTagName("dttmShowStart").item(0).getTextContent();
                String dtEnd = element.getElementsByTagName("dttmShowEnd").item(0).getTextContent();
                String portraitURL = element.getElementsByTagName("EventLargeImagePortrait").item(0).getTextContent();

                m_arrMovies.add(new Movie(title, dtStart, dtEnd, portraitURL));
            }

            return true; // Success
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
