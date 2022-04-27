package com.example.oop_project;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class TheatreManager {
    private ArrayList<Theatre> m_arrTheatres = new ArrayList<Theatre>();
    private int m_selectedTheatreID; // Current state. Stores array index

    private String m_urlTheatreAreaIDs;



    TheatreManager() {
        m_urlTheatreAreaIDs = "https://www.finnkino.fi/xml/TheatreAreas/";
        readTheatreIDs(m_urlTheatreAreaIDs);

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
        // TODO: Guards to prevent invalid ids
        m_selectedTheatreID = id;
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
    public ArrayList<Movie> getMovies() {
        return m_arrTheatres.get(m_selectedTheatreID).getMovies();
    }

    // Parses theatre ids and names from given url, and stores them in m_arrTheatres array
    // as Theatre objects
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
