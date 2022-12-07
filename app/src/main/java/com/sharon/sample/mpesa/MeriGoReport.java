package com.sharon.sample.mpesa;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MeriGoReport extends Fragment {
    View view;
    DatabaseReference awardedUsersRef, usersRef;
    ListView lvMeriGo;
    ProgressDialog loader;
    public MeriGoReport() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_meri_go_report, container, false);
        awardedUsersRef = FirebaseDatabase.getInstance().getReference("users/awarded");
        usersRef = FirebaseDatabase.getInstance().getReference("user");
        lvMeriGo = view.findViewById(R.id.lvMeriGo);
        loader = new ProgressDialog(getActivity());
        ArrayList<AwardedUser> awardedUsers = new ArrayList<>();

        MeriGoReportAdapter adapter = new MeriGoReportAdapter(getActivity(), awardedUsers);
        lvMeriGo.setAdapter(adapter);

        // get awarded from database
        loader.setMessage("Getting users...");
        loader.show();
        awardedUsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                awardedUsers.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    AwardedUser user =  ds.getValue(AwardedUser.class);
                    awardedUsers.add(user);
                }

                adapter.notifyDataSetChanged();
                loader.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loader.dismiss();
            }
        });

        TextView tvMerigoCount = view.findViewById(R.id.tvMerigoCount);
        // get total number of users, to get total number of merigo cycles
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int usersCount = 0;
                for(DataSnapshot ds: snapshot.getChildren()){
                    usersCount +=1;
                }

//                tvMerigoCount.setText(awardedUsers.size()/usersCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
}