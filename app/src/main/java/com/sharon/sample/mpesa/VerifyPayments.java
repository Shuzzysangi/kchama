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

public class VerifyPayments extends AppCompatActivity {
    ListView lvPayments;
    UserPaymentsAdaper adapter;
    UserPayment payment;
    ArrayList<UserPayment> payments;
    DatabaseReference paymentsRef;
    ProgressDialog loader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_payments);
        
        lvPayments = findViewById(R.id.lvPayments);
        paymentsRef = FirebaseDatabase.getInstance().getReference("payments");
        loader = new ProgressDialog(this);
        payments = new ArrayList<>();
        adapter = new UserPaymentsAdaper(this, payments);
        lvPayments.setAdapter(adapter);

        // show loading indicator
        loader.setMessage("Getting payment info... Please wait....");
        loader.setCancelable(false);
        loader.show();
        paymentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                payments.clear();
                boolean found = false;
                for(DataSnapshot ds: snapshot.getChildren()){
                    payment = ds.getValue(UserPayment.class);
                    found = true;
                    // do not add if payment is already verified
                    if(payment.isVerified()) continue;

                    payments.add(payment);
                }

                adapter.notifyDataSetChanged();
                // hide progress loader
                loader.dismiss();
                if(!found){
                    // all payments have been verified
                    Toast.makeText(VerifyPayments.this, "All members payments have been verified. ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loader.setMessage(error.getMessage());loader.dismiss();
            }
        });
    }
}