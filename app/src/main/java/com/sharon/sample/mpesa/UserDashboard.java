package com.sharon.sample.mpesa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

public class UserDashboard extends AppCompatActivity {

    private CardView pay, prog;
    private CardView select;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        pay = findViewById(R.id.pay);
        select = findViewById(R.id.select);
        prog = findViewById(R.id.prog);
        pay.setOnClickListener(View -> {
            Intent intent = new Intent(UserDashboard.this, MpesaActivity.class);
            startActivity(intent);

        });
        prog.setOnClickListener(View -> {
            Intent intent = new Intent(UserDashboard.this, ProjectsActivity.class);
            startActivity(intent);

        });
       select.setOnClickListener(View -> {
            Intent intent = new Intent(UserDashboard.this,SelectWinner.class);
            startActivity(intent);

        });
    }
}