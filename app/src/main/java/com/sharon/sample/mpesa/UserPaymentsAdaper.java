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

public class UserPaymentsAdaper extends BaseAdapter {
    ArrayList<UserPayment> payments;
    Context context;
    DatabaseReference paymentsRef;
    ProgressDialog loader;
    public UserPaymentsAdaper(Context ctx, ArrayList<UserPayment> payments){
        context = ctx;
        this.payments = payments;
        paymentsRef = FirebaseDatabase.getInstance().getReference("payments");
        loader = new ProgressDialog(context);
//        loader.setTitle("Verifying info");
        loader.setMessage("Verifying payment ...");
        loader.setCancelable(true);
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
        TextView mPesaCode, amount, purpose, name;
        Button verify;
        view = LayoutInflater.from(context).inflate(R.layout.verify_payments, parent, false);
        mPesaCode = view.findViewById(R.id.mPesaCode);
        amount = view.findViewById(R.id.amount);
        purpose = view.findViewById(R.id.purpose);
//        name = view.findViewById(R.id.name);
        verify = view.findViewById(R.id.verify);

        mPesaCode.setText(payments.get(position).getMpesaCode());
        amount.setText(String.valueOf(payments.get(position).getAmount()));
        purpose.setText(payments.get(position).getPurpose());
//        name.setText(payments.get(position).getName());

        // verify on button click
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loader.show();
                UserPayment pymt = payments.get(position);
                pymt.setVerified(true);

                paymentsRef.child(pymt.getId()).setValue(pymt.toMap())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                loader.dismiss();
                                if(task.isSuccessful()){
                                    Toast.makeText(context, "Payment verified successfully", Toast.LENGTH_SHORT).show();
                                    // remove the item from array
                                    payments.remove(pymt);
                                    UserPaymentsAdaper.super.notifyDataSetChanged();
                                }else{
                                    Toast.makeText(context, "Something went wrong verifying payment info.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        return view;
    }
}
