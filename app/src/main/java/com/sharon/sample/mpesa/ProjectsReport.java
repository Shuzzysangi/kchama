package com.sharon.sample.mpesa;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.data.DataBufferSafeParcelable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProjectsReport extends Fragment {
    View view;
    DatabaseReference projectsRef;
    ListView lvProjects;
    ProgressDialog loader;
    public ProjectsReport() {
        // Required empty public constructor

        projectsRef = FirebaseDatabase.getInstance().getReference("project");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_projects_report, container, false);
        loader = new ProgressDialog(getActivity());
        ArrayList<Project> projects = new ArrayList<>();
        ProjectReportAdapter adapter = new ProjectReportAdapter(getActivity(), projects);
        lvProjects = view.findViewById(R.id.lvProjects);
        TextView total = view.findViewById(R.id.tvProjectsCount);
        lvProjects.setAdapter(adapter);

        // get projects from database
        loader.setMessage("Getting projects..");
        loader.show();
        projectsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                projects.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    Project project = ds.getValue(Project.class);
                    // add to total projects
                    projects.add(project);
                }

                // notify update projects count
                adapter.notifyDataSetChanged();
                loader.dismiss();
                // update total projects

                total.setText(String.valueOf(projects.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loader.dismiss();
            }
        });

        return view;
    }
}