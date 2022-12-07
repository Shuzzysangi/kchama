package com.sharon.sample.mpesa;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class UserDashboard extends AppCompatActivity {

    private CardView pay, prog, meriGo;
    private CardView select, logout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        pay = findViewById(R.id.pay);
        select = findViewById(R.id.select);
        prog = findViewById(R.id.prog);
        logout = findViewById(R.id.logout);
        meriGo = findViewById(R.id.M);
        pay.setOnClickListener(View -> {
            Intent intent = new Intent(UserDashboard.this, PaymentDetails.class);
            startActivity(intent);

        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(UserDashboard.this);
                builder.setCancelable(false);
                builder.setTitle("Exit");
                builder.setMessage("are you sure you want to logout?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        //  delete data in shared preferences
                        logout();
                        startActivity(new Intent(UserDashboard.this,Dashboard.class));
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
        prog.setOnClickListener(View -> {
            Intent intent = new Intent(UserDashboard.this, ProjectsActivity.class);
            startActivity(intent);

        });

        meriGo.setOnClickListener(view -> {
            Intent intent = new Intent(UserDashboard.this, MeriGo.class);
            startActivity(intent);
        });

       select.setOnClickListener(View -> {
           Intent intent = new Intent(UserDashboard.this, Reports.class);
           startActivity(intent);
       });

    }

    private void logout() {
        SharedPreferences settings = getSharedPreferences("user_details", Context.MODE_PRIVATE);
        settings.edit().clear().commit();
    }
}