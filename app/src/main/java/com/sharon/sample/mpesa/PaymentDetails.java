package com.sharon.sample.mpesa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class PaymentDetails extends AppCompatActivity {
    EditText edtMpesaCode, edtAmount, edtPurpose;
    Button btnSubmit;
    String mPesaCode, purpose;
    int amount;
    DatabaseReference paymentsRef;
    SharedPreferences prefs;
    FirebaseDatabase database;
    ProgressDialog loader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        edtMpesaCode = findViewById(R.id.mPesaCode);
        edtAmount = findViewById(R.id.amount);
        edtPurpose = findViewById(R.id.purpose);
        btnSubmit = findViewById(R.id.submit);
        database = FirebaseDatabase.getInstance();
        paymentsRef = database.getReference("/payments");
        loader = new ProgressDialog(this);
        prefs = getSharedPreferences("user_details", Context.MODE_PRIVATE);
        loader.setTitle("Posting data");
        loader.setMessage("Uploading your data... Please wait...");
        loader.setCancelable(false);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPesaCode = edtMpesaCode.getText().toString().trim();
                amount = Integer.parseInt(edtAmount.getText().toString().trim());
                purpose = edtPurpose.getText().toString().trim();

                if(TextUtils.isEmpty(mPesaCode) || TextUtils.isEmpty(edtAmount.getText().toString()) || TextUtils.isEmpty(purpose)){
                    Toast.makeText(PaymentDetails.this, "Please fill in all the fields.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // get logged in user details
                String email = prefs.getString("email", "defaultuser@gmail.com");
                String id = prefs.getString("id", null);
                String phoneNumber = prefs.getString("phoneNumber", null);

                loader.show();
                paymentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String key = paymentsRef.push().getKey();
                        UserPayment payment = new UserPayment();
                        payment.setId(key);
                        payment.setMpesaCode(mPesaCode);
                        payment.setAmount(amount);
                        payment.setPurpose(purpose);
                        payment.setPhoneNumber(phoneNumber);

                        paymentsRef.child(key).setValue(payment.toMap());
                        loader.dismiss();
                        Toast.makeText(PaymentDetails.this, "Payment Details submitted successfully.", Toast.LENGTH_SHORT).show();

                        // clear entered user inputs
                        clearInputs();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        loader.dismiss();
                    }
                });
            }
        });
        
        
    }

    // clear user inputs
    public void clearInputs(){
        edtMpesaCode.setText("");
        edtAmount.setText("");
        edtPurpose.setText("");
    }
}