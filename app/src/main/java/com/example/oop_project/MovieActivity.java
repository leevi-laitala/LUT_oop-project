package com.example.oop_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MovieActivity extends AppCompatActivity {
    private String m_eventID;
    private String m_title;
    private float m_rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        m_rating = -1.f; // -1, so no rating

        Intent m_intent = getIntent();
        m_eventID = m_intent.getStringExtra("eventID");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        String available = "";

        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(m_eventID);
            doc.getDocumentElement().normalize();

            NodeList events = doc.getDocumentElement().getElementsByTagName("Show");

            System.out.println(events.getLength());

            Node node = events.item(0);
            Element element = (Element)node;

            String landscapeURL;
            try {
                landscapeURL = element.getElementsByTagName("EventLargeImageLandscape").item(0).getTextContent().replace("http://", "https://");
            } catch (NullPointerException ne) {
                landscapeURL = element.getElementsByTagName("EventSmallImageLandscape").item(0).getTextContent().replace("http://", "https://");
            }

            ImageView landscapeView = (ImageView) findViewById(R.id.landscape);
            Bitmap landscape = DownloadImage(landscapeURL);
            landscapeView.setImageBitmap(landscape);

            String ratingURL = element.getElementsByTagName("RatingImageUrl").item(0).getTextContent().replace("http://", "https://");

            ImageView ratingView = (ImageView) findViewById(R.id.rating);
            Bitmap ratingImage = DownloadImage(ratingURL);
            ratingView.setImageBitmap(ratingImage);

            TextView infoText = (TextView) findViewById(R.id.titleText);
            m_title = element.getElementsByTagName("Title").item(0).getTextContent();
            infoText.setText(m_title);

            for (int i = 0; i < events.getLength(); i++) {
                node = events.item(i);
                element = (Element)node;

                available += element.getElementsByTagName("Theatre").item(0).getTextContent() + ", at " + element.getElementsByTagName("dttmShowStart").item(0).getTextContent().substring(11, 16) + "\n";
            }

            TextView list = (TextView) findViewById(R.id.list);
            list.setText(available);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RatingBar bar = (RatingBar) findViewById(R.id.ratingBar);
        bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                m_rating = ratingBar.getRating();
            }
        });

        Button addTo = (Button) findViewById(R.id.addToWatchlist);
        addTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedpreferences = getSharedPreferences("MovieArchive", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putFloat(m_eventID, m_rating);
                editor.commit();

                finish();
            }
        });
    }

    // Functions OpenHttpConnection and DownloadImage copied from https://stackoverflow.com/a/12173580

    private InputStream OpenHttpConnection(String urlString) throws IOException {
        System.out.println(urlString);

        InputStream in = null;
        int response = -1;

        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();

        if (!(conn instanceof HttpURLConnection))
            throw new IOException("Not an HTTP connection");

        try {
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            response = httpConn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new IOException("Error connecting");
        }
        return in;
    }

    private Bitmap DownloadImage(String URL) {
        Bitmap bitmap = null;
        InputStream in = null;
        try {
            in = OpenHttpConnection(URL);
            bitmap = BitmapFactory.decodeStream(in);
            if (in != null) {
                in.close();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return bitmap;
    }
}