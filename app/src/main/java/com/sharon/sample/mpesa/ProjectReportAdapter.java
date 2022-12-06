package com.sharon.sample.mpesa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProjectReportAdapter extends BaseAdapter {
    Context context;
    ArrayList<Project> projects;
    public ProjectReportAdapter(Context context, ArrayList<Project> projects){
        this.context = context;
        this.projects = projects;
    }

    @Override
    public int getCount() {
        return projects.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = LayoutInflater.from(context).inflate(R.layout.project_report_item, parent, false);

        // get views in the inflated layout
        TextView sNo, name, desc, date;

        sNo = view.findViewById(R.id.sNo);
        name = view.findViewById(R.id.name);
        desc = view.findViewById(R.id.desc);
        date = view.findViewById(R.id.date);


        Project project = projects.get(position);
        sNo.setText(String.valueOf(position+1));
        name.setText(project.getName());
        desc.setText(project.getDescription());
        date.setText(project.getDate());

        return view;
    }
}
