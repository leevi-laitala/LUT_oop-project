package com.example.oop_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class ListAdapt extends RecyclerView.Adapter<ListAdapt.ViewHolder> {
    ArrayList<String> arrTitle;
    Context context;

    public ListAdapt(Context cx, ArrayList<String> titles) {
        context = cx;
        arrTitle = titles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(arrTitle.get(position));
        //holder.start.setText("Starts at " + arrStart.get(position).substring(11, 16) + " " + arrStart.get(position).substring(0, 10));
        //holder.end.setText("Ends at " + arrEnd.get(position).substring(11, 16) + " " + arrStart.get(position).substring(0, 10));
    }

    @Override
    public int getItemCount() {
        return arrTitle.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, start, end;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
        }
    }
}