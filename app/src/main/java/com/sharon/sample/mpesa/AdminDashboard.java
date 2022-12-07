package com.sharon.sample.mpesa;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class AdminDashboard extends AppCompatActivity {


    private CardView Reg;
    private CardView wels;
    private CardView Send;
    private CardView logout;
    private CardView verify;
    private CardView userPayments;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        Reg=findViewById(R.id.reg);
       Send=findViewById(R.id.Send);
        logout=findViewById(R.id.logout);
        verify=findViewById(R.id.verify);
       wels=findViewById(R.id.wels);
        userPayments = findViewById(R.id.userPayments);

        Reg.setOnClickListener(View->{
            Intent intent = new Intent(AdminDashboard.this,ViewInfo.class);
            startActivity(intent);
        });
        Send.setOnClickListener(View->{
            Intent intent = new Intent(AdminDashboard.this, SendSMS.class);
            startActivity(intent);
        });

        verify.setOnClickListener(View->{
            Intent intent = new Intent(AdminDashboard.this,VerifyPayments.class);
            startActivity(intent);
        });
        wels.setOnClickListener(View->{
            Intent intent = new Intent(AdminDashboard.this, Welfare.class);
            startActivity(intent);
        });

        userPayments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboard.this, UserPaymentsActivity.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(AdminDashboard.this);
                builder.setCancelable(false);
                builder.setTitle("Exit");
                builder.setMessage("are you sure you want to logout?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(AdminDashboard.this,Dashboard.class));
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
    }
}