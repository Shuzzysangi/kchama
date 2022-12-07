package com.sharon.sample.mpesa;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class UserReg extends AppCompatActivity {

    private TextInputEditText registerFullName;
    private TextInputEditText registerPhoneNumber;
    private TextInputEditText registerEmail;
    private TextInputEditText registerUserID;
    private TextInputEditText registerPassword;
    private Button userRegisterButton;
    private ProgressDialog loader;
    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseRef;
    SharedPreferences prefs;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reg);
        registerFullName=findViewById(R.id.registerFullName);
        registerPhoneNumber=findViewById(R.id.registerPhoneNumber);
        registerUserID=findViewById(R.id.registerUserID);
        registerEmail=findViewById(R.id.registerEmail);

        registerPassword=findViewById(R.id.registerPassword);
        userRegisterButton=findViewById(R.id.userRegisterButton);
        loader=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();

        userRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = registerEmail.getText().toString().trim();
                final String password = registerPassword.getText().toString().trim();
                final String userId = registerUserID.getText().toString().trim();
                final String fullName = registerFullName.getText().toString().trim();
                final String phoneNumber = registerPhoneNumber.getText().toString().trim();
                prefs = getSharedPreferences("user_details", Context.MODE_PRIVATE);

                if (TextUtils.isEmpty(email)) {
                    registerEmail.setError("Email is Required!");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    registerPassword.setError("Password is Required!");
                    return;
                }
                    if (TextUtils.isEmpty(userId)) {
                        registerUserID.setError("UserID Required!");
                        return;
                }
                if (TextUtils.isEmpty(fullName)) {
                    registerFullName.setError("FullName is Required!");
                    return;
                }
                if (TextUtils.isEmpty(phoneNumber)) {
                    registerPhoneNumber.setError("PhoneNumber is Required!");
                    return;
                } else {
                    loader.setMessage("Registering member....");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()){
                                String error=task.getException().toString();
                                Toast.makeText(UserReg.this,"Error"+error,Toast.LENGTH_LONG).show();
                                loader.dismiss();

                            }
                            else {
                                String currentUserId=mAuth.getCurrentUser().getUid();
                                userDatabaseRef= FirebaseDatabase.getInstance().getReference().child("user").child(currentUserId);
                                HashMap userInfo=new HashMap();
                                userInfo.put("id",currentUserId);
                                userInfo.put("name",fullName);
                                userInfo.put("email",email);
                                userInfo.put("phoneNumber",phoneNumber);
                                userInfo.put("type","user");
                                userInfo.put("awarded", false);

                                userDatabaseRef.updateChildren(userInfo).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(UserReg.this,"User registered successfully",Toast.LENGTH_SHORT).show();

                                            Intent intent=new Intent(UserReg.this,AdminDashboard.class);

                                            // save user info to shared preferences
                                            SharedPreferences.Editor editor = prefs.edit();
                                            editor.putString("name", fullName);
                                            editor.putString("phoneNumber", phoneNumber);
                                            editor.putString("email", email);
                                            editor.putString("password", password);
                                            editor.apply();
                                            editor.commit();

                                            loader.dismiss();
                                            startActivity(intent);
                                            finish();

                                        }else{
                                            Toast.makeText(UserReg.this,"Registration not successful. Error: "+task.getException().toString(),Toast.LENGTH_SHORT).show();
                                            finish();
                                            loader.dismiss();
                                        }
                                    }
                                });

                            }
                        }
                    });
                }
            }
        });
    }
}