package com.sharon.sample.mpesa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.telephony.SmsManager;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SendSMS extends AppCompatActivity {
    EditText etPhone, etMessage;
    Button btSend;
    DatabaseReference usersRef;
    ArrayList<User> users;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);
        etPhone = findViewById(R.id.et_phone);
        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Getting contacts... Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        usersRef = FirebaseDatabase.getInstance().getReference("/user");

        // get all users from firebase database
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    User user = ds.getValue(User.class);
                    users.add(user);
                }

                progressDialog.dismiss();
                // set contacts in UI
                setContacts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });


        Intent intent = getIntent();
        String phone = intent.getStringExtra("phone");
        etMessage = findViewById(R.id.et_message);

        btSend = findViewById(R.id.bt_send);
        btSend.setOnClickListener(v -> {
            //checking condition
            if (ContextCompat.checkSelfPermission(SendSMS.this, Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_GRANTED) {
                //WHEN permission is granted
                //create method
                sendMessage();
            } else {
                //when permission is not granted
                //request permission
                ActivityCompat.requestPermissions(SendSMS.this
                        , new String[]{Manifest.permission.SEND_SMS}
                        , 100);
            }

        });

    }

    private void sendMessage() {
        //Get Values from edit Text
        String sMessage = etMessage.getText().toString().trim();
        //check condition

        if (!sMessage.equals("")) {
            //when both edit text value not equal to blank
            SmsManager smsManager = SmsManager.getDefault();

            for (User user: this.users){
                //send text message
                smsManager.sendTextMessage(user.getPhoneNumber(), null, sMessage, null, null);
            }

            //Display toast
            Toast.makeText(getApplicationContext()
                    , "SMS Messages sent successfully", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Please enter message to send", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//check condition
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0]
                == PackageManager.PERMISSION_GRANTED) {
            //when Permission is granted
            //call method
            sendMessage();
        } else {
            //when permission is denied
            //display toast
            Toast.makeText(getApplicationContext()
                    , "Permission Denied!", Toast.LENGTH_SHORT).show();
        }

    }

    // setup contacts
    public void setContacts() {
        String contactsList;
        StringBuilder builder = new StringBuilder();

        for (User user : this.users) {
            builder.append(user.getPhoneNumber()).append(";");
        }
        contactsList = builder.toString();
        Toast.makeText(this, contactsList, Toast.LENGTH_SHORT).show();
        etPhone.setText(contactsList);
    }
}