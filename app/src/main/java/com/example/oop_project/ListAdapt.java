package com.example.oop_project;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.KeyEvent;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

class ListAdapt extends RecyclerView.Adapter<ListAdapt.ViewHolder> {
    ArrayList<String> m_arrTitles;
    ArrayList<Bitmap> m_arrPortraitBmps;
    ArrayList<LocalDateTime> m_arrBeginTimes;
    ArrayList<LocalDateTime> m_arrEndTimes;
    Context context;

    public ListAdapt(Context cx, ArrayList<String> titles, ArrayList<String> portraitURLs, ArrayList<LocalDateTime> beginTimes, ArrayList<LocalDateTime> endTimes) {
        context = cx;
        m_arrTitles = titles;
        m_arrBeginTimes = beginTimes;
        m_arrEndTimes = endTimes;

        m_arrPortraitBmps = new ArrayList<Bitmap>();

        for (int i = 0; i < portraitURLs.size(); i++) {
            String url = portraitURLs.get(i).replace("http://", "https://");
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

    // TODO: Make whole row clickable
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(m_arrTitles.get(position));
        holder.portrait.setImageBitmap(m_arrPortraitBmps.get(position));

        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm dd MM yyyy");
        holder.startTime.setText(m_arrBeginTimes.get(position).format(format));

        holder.title.setOnKeyListener(new View.OnKeyListener() {
            public void onClick(View v) {
                System.out.println("Jefa :D");
            }
        });
    }

    @Override
    public int getItemCount() {
        return m_arrTitles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
            in.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return bitmap;
    }
}