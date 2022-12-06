package com.sharon.sample.mpesa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class UserPaymentsActivity extends AppCompatActivity {
    Button restartCycle;
    FirebaseDatabase database;
    DatabaseReference usersRef, awardedUsersRef, cycleRef;
    ProgressDialog loader;
    ValueEventListener valueEventListener;
    DatePicker datePicker;
    TimePicker timePicker;
    Button setSpinDate;
    TimePickerDialog picker;
    String spinDate; // next spin date and time
    int year, month, date, hour, minute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_payments);

        restartCycle = findViewById(R.id.restartCycle);
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        setSpinDate = findViewById(R.id.setSpinDate);
        timePicker.setIs24HourView(true);
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("user");
        awardedUsersRef = database.getReference("users/awarded");
        cycleRef = database.getReference("/cycles");
        loader = new ProgressDialog(this);


        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean foundUser = false; // checks if there was a member who had not been awarded.
                User user;
                for(DataSnapshot ds : snapshot.getChildren()){
                    user = ds.getValue(User.class);
                    /**
                     * Set member as not awarded and remove them too from the awarded members list
                     */
                    if(user.isAwarded()){
                        foundUser = true;
                        user.setAwarded(false);
                        usersRef.child(user.getId()).updateChildren(user.toMap());
                        // delete the user too in awarded list
                        awardedUsersRef.child(user.getId()).removeValue();
                    }
                }

                loader.dismiss();
                if(!foundUser){
                    Toast.makeText(UserPaymentsActivity.this, "The cycle is ready to begin!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(UserPaymentsActivity.this, "Award cycle restarted successfully.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        restartCycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loader.setMessage("Restarting Cycle.... Please Wait...");
                loader.show();
                usersRef.addListenerForSingleValueEvent(valueEventListener);
            }
        });

        /**
         * Set next spin date
         */
        setSpinDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                hour = calendar.get(Calendar.HOUR_OF_DAY);
                minute = calendar.get(Calendar.MINUTE);
                
                picker = new TimePickerDialog(UserPaymentsActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                // set spin time
                                hour = sHour;
                                minute = sMinute;
                            }
                        }, hour, minute, true);

                // date displayed time
                picker.updateTime(hour, minute);

                // set date, month and year
                date = datePicker.getDayOfMonth();
                month = datePicker.getMonth()+1;
                year  = datePicker.getYear();

                calendar.set(year, month, month, date, hour, minute);
                // format date
                spinDate = date+"/"+month+"/"+year+" "+hour+":"+minute;

                Toast.makeText(UserPaymentsActivity.this, "Spin Date: "+spinDate, Toast.LENGTH_LONG).show();

                // set spin date in firebase
                cycleRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        cycleRef.setValue(spinDate);
                        Toast.makeText(UserPaymentsActivity.this, "Spin date set successfully. ", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // TODO handle
                    }
                });
            }
        });
    }
}