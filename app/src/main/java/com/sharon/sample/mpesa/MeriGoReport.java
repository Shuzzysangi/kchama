package com.sharon.sample.mpesa;

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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MeriGoReport extends Fragment {
    View view;
    TableLayout tableLayout;
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
        tableLayout = view.findViewById(R.id.tableLayout);
        tableLayout = view.findViewById(R.id.tableLayout);

        TableRow row = new TableRow(getContext());
        row.setBackgroundColor(Color.parseColor("#51B435"));
        row.setPadding(10,10,10,10);

        // setting column one of the row1
        TextView col1 = new TextView(getActivity());
        col1.setText("S/No.");
        col1.setLayoutParams( new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        // setting column 2 of the row1
        TextView col2 = new TextView(getActivity());
        col2.setText("Phone Number");
        col2.setLayoutParams( new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        // setting column 3 of the row1
        TextView col3 = new TextView(getActivity());
        col3.setText("Date");
        col3.setLayoutParams( new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        // adding columns to row
        row.addView(col1);
        row.addView(col2);
        row.addView(col3);
        tableLayout.addView(row);

        // creating other items
        ArrayList<AwardedUser> users = new ArrayList<>();
        AwardedUser user = new AwardedUser();
        user.setPhoneNumber("071355567");
        user.setName("John Doe");
        user.setAwardedDate("02/12/2022 12:50");
        user.setAwardedAmount(300);
        users.add(user);

        //loop through the info adding data to the items
        for (int i=0; i<users.size(); i++){
            AwardedUser aUser = users.get(i);
            TableRow tbrow = new TableRow(getActivity());

            TextView t1 = new TextView(getActivity());
            TextView t2 = new TextView(getActivity());
            TextView t3 = new TextView(getActivity());
            TextView t4 = new TextView(getActivity());
            // s.No
            t1.setPadding(3,3,3,3);
            t1.setTypeface(null, Typeface.BOLD);
            t1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            t1.setText(String.valueOf(i));
            // phone number
            t2.setPadding(3,3,3,3);
            t2.setTypeface(null, Typeface.BOLD);
            t2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            t2.setText(aUser.getPhoneNumber());

            // name
            t3.setPadding(3,3,3,3);
            t3.setTypeface(null, Typeface.BOLD);
            t3.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            t3.setText(aUser.getName());


            // Award Date
            t4.setPadding(3,3,3,3);
            t4.setTypeface(null, Typeface.BOLD);
            t4.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            t4.setText(aUser.getAwardedDate());

            // add items to row
            tbrow.addView(t1);
            tbrow.addView(t2);
            tbrow.addView(t3);
            tbrow.addView(t4);
            // add row to table
            tableLayout.addView(tbrow);
        }
        return view;
    }
}