package com.sharon.sample.mpesa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VerifiedPaymentsActivity extends AppCompatActivity {
    ListView lvPayments;
    VerifiedAdaper adapter;
    UserPayment payment;
    ArrayList<UserPayment> payments;
    DatabaseReference paymentsRef;
    ProgressDialog loader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verified_payments);
        lvPayments = findViewById(R.id.lvPayments);
        paymentsRef = FirebaseDatabase.getInstance().getReference("payments");
        loader = new ProgressDialog(this);

        payments = new ArrayList<>();
        adapter = new VerifiedAdaper(this, payments);
        lvPayments.setAdapter(adapter);

        // show loading indicator
        loader.setMessage("Getting Verified Payments... Please wait....");
        loader.show();

        paymentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                payments.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    payment = ds.getValue(UserPayment.class);
                    // do not add if payment is not verified
                    if(!payment.isVerified()) continue;

                    payments.add(payment);
                }

                adapter.notifyDataSetChanged();
                // hide progress loader
                loader.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loader.setMessage(error.getMessage());loader.dismiss();
            }
        });
    }
}