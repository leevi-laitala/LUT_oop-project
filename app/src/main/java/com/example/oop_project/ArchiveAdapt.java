package com.example.oop_project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

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

// Adapter class to populate archive list with movie ratings, titles and images
class ArchiveAdapt extends RecyclerView.Adapter<ArchiveAdapt.ViewHolder> {
    ArrayList<String> m_arrTitles;
    ArrayList<Bitmap> m_arrPortraitBmps;
    ArrayList<Float> m_arrRatings;
    Context context;
    ItemClickListener m_itemListener;

    public ArchiveAdapt(Context cx, ArrayList<String> eventIDs, ArrayList<Float> ratings, ItemClickListener itemClickListener) {
        context = cx;
        m_arrRatings = ratings;

        m_arrTitles = new ArrayList<>();
        m_arrPortraitBmps = new ArrayList<>();

        try {
            // Populate member arrays
            for (int i = 0; i < eventIDs.size(); i++) {
                // Init parser
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document doc = builder.parse(eventIDs.get(i));
                doc.getDocumentElement().normalize();

                // Select all shows
                NodeList events = doc.getDocumentElement().getElementsByTagName("Show");

                // Only care about first show, since all shows are the same movie, but in different
                // theatres, that we don't care about currently
                Node node = events.item(0);
                Element element = (Element) node;

                // Get portrait url, first try to get "high" resolution one, if fails get low resolution
                String portraitURL;
                try {
                    portraitURL = element.getElementsByTagName("EventLargeImagePortrait").item(0).getTextContent().replace("http://", "https://");
                } catch (NullPointerException ne) {
                    portraitURL = element.getElementsByTagName("EventSmallImagePortrait").item(0).getTextContent().replace("http://", "https://");
                }

                // Download image from url
                Bitmap portrait = DownloadImage(portraitURL);
                m_arrPortraitBmps.add(portrait); // Add to array

                // Get title from xml file
                String title = element.getElementsByTagName("Title").item(0).getTextContent();
                m_arrTitles.add(title); // Add to array
            }
        } catch (Exception e) {
            // Print stack trace if fails
            e.printStackTrace();
        }

        // Assing parameter to member variable
        m_itemListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.archive_row, parent, false);
        return new ViewHolder(view);
    }

    // Assign variables to view elements
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Position is array index, used to populate list
        holder.title.setText(m_arrTitles.get(position));
        holder.portrait.setImageBitmap(m_arrPortraitBmps.get(position));
        holder.bar.setRating(m_arrRatings.get(position));

        // Set callback for click event
        holder.itemView.setOnClickListener(view -> m_itemListener.OnItemClick(position));
    }

    @Override
    public int getItemCount() {
        // Item count equals to array sizes, take size from titles for example
        return m_arrTitles.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        // Fetch views by id and save them to variables
        TextView title;
        RatingBar bar;
        ImageView portrait;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            bar = itemView.findViewById(R.id.ratingBar2);
            portrait = itemView.findViewById(R.id.archivePortrait);
        }
    }

    // Functions OpenHttpConnection and DownloadImage copied from https://stackoverflow.com/a/12173580

    private InputStream OpenHttpConnection(String urlString) throws IOException {
        InputStream in = null;
        int response;

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
        InputStream in;
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

    public interface ItemClickListener{
        void OnItemClick(int position);
    }
}
