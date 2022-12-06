package com.sharon.sample.mpesa;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class VerifiedAdaper extends BaseAdapter {
    ArrayList<UserPayment> payments;
    Context context;
    public VerifiedAdaper(Context ctx, ArrayList<UserPayment> payments){
        context = ctx;
        this.payments = payments;
    }

    @Override
    public int getCount() {
        return payments.size();
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
        Button btnVerify;
        TextView mPesaCode, amount, purpose, name, phoneNumber;
        view = LayoutInflater.from(context).inflate(R.layout.verify_payments, parent, false);
        mPesaCode = view.findViewById(R.id.mPesaCode);
        amount = view.findViewById(R.id.amount);
        purpose = view.findViewById(R.id.purpose);
        name = view.findViewById(R.id.name);
        phoneNumber = view.findViewById(R.id.phoneNumber);
        btnVerify = view.findViewById(R.id.verify);

        // hide verify button
        btnVerify.setVisibility(View.GONE);

        mPesaCode.setText("M-Pesa Code: "+payments.get(position).getMpesaCode());
        amount.setText("Amount: "+String.valueOf(payments.get(position).getAmount()));
        purpose.setText("Purpose: "+payments.get(position).getPurpose());
        name.setText("Name: "+payments.get(position).getName());
        phoneNumber.setText("Phone: "+payments.get(position).getPhoneNumber());
        
        return view;
    }
}
