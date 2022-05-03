package com.example.oop_project;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

class ListAdapt extends RecyclerView.Adapter<ListAdapt.ViewHolder> {
    ArrayList<String> m_arrTitles;
    ArrayList<Bitmap> m_arrPortraitBmps;
    ArrayList<LocalDateTime> m_arrBeginTimes;
    ArrayList<LocalDateTime> m_arrEndTimes;
    ArrayList<Duration> m_arrDurations;
    Context context;
    ItemClickListener m_itemListener;

    public ListAdapt(Context cx, ArrayList<String> titles, ArrayList<String> portraitURLs, ArrayList<LocalDateTime> beginTimes, ArrayList<LocalDateTime> endTimes, ArrayList<Duration> lengths, ItemClickListener itemClickListener) {
        context = cx;
        m_arrTitles = titles;
        m_arrBeginTimes = beginTimes;
        m_arrEndTimes = endTimes;
        m_arrDurations = lengths;
  
        m_itemListener = itemClickListener;

        m_arrPortraitBmps = new ArrayList<>();

        // Download portrait images, and save them in array
        for (int i = 0; i < portraitURLs.size(); i++) {
            String url = portraitURLs.get(i).replace("http://", "https://"); // Upgrade to https
            m_arrPortraitBmps.add(DownloadImage(url));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row, parent, false);
        return new ViewHolder(view);
    }

    // Assign variables to view elements
    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Position is array index, used to populate list
        holder.title.setText(m_arrTitles.get(position));
        holder.portrait.setImageBitmap(m_arrPortraitBmps.get(position));

        // Set date with custom format
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
        holder.startTime.setText(m_arrBeginTimes.get(position).format(format));

        // Set lenght text, and make it pretty with regex
        holder.length.setText("Length: " + m_arrDurations.get(position).toString().substring(2).replaceAll("(\\d[HMS])(?!$)", "$1 ").toLowerCase());

        // Set click event callback
        holder.itemView.setOnClickListener(view -> m_itemListener.OnItemClick(position));
    }

    // Return list length
    @Override
    public int getItemCount() {
        return m_arrTitles.size();
    }

    // Fetch views by id and save them to variables
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView startTime;
        TextView length;
        ImageView portrait;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            startTime = itemView.findViewById(R.id.start);
            length = itemView.findViewById(R.id.length);
            portrait = itemView.findViewById(R.id.portrait);
        }
    }

    // Functions OpenHttpConnection and DownloadImage copied from https://stackoverflow.com/a/12173580

    private InputStream OpenHttpConnection(String urlString) throws IOException {
        System.out.println(urlString);

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