package com.sharon.sample.mpesa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sharon.mpesa.stkpush.Mode;
import com.sharon.mpesa.stkpush.api.response.STKPushResponse;
import com.sharon.mpesa.stkpush.interfaces.STKListener;
import com.sharon.mpesa.stkpush.interfaces.TokenListener;
import com.sharon.mpesa.stkpush.model.Mpesa;
import com.sharon.mpesa.stkpush.model.STKPush;
import com.sharon.mpesa.stkpush.model.Token;
import com.sharon.mpesa.stkpush.model.Transaction;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MpesaActivity extends AppCompatActivity implements TokenListener {

    public static final String TAG = MpesaActivity.class.getSimpleName();

    private EditText phoneET, amountET;
    private SweetAlertDialog sweetAlertDialog;
    private Mpesa mpesa;

    private String phone_number;
    private String amount;
    private String spinDateStr;
    private Date dateNow, spinDate;
    FirebaseDatabase database;
    DatabaseReference cyclesRef;
    boolean isPenalized  = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpesa);


        phoneET = findViewById(R.id.phoneET);
        amountET = findViewById(R.id.amountET);
        database = FirebaseDatabase.getInstance();
        cyclesRef = database.getReference("/cycles");

        mpesa = new Mpesa(Config.CONSUMER_KEY, Config.CONSUMER_SECRET, Mode.SANDBOX);

        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitleText("Connecting to Safaricom");
        sweetAlertDialog.setContentText("Please wait...");
        sweetAlertDialog.setCancelable(false);

        // get spin date from database
        cyclesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    spinDateStr = snapshot.getValue().toString();
                    SimpleDateFormat formatter = new SimpleDateFormat();
                    try {
                        spinDate = formatter.parse(spinDateStr);
                        dateNow = new Date();

                        // check if user will be fined or not
                        if(!dateNow.before(spinDate)){
                            //Toast.makeText(MpesaActivity.this, "Note that due to late payment, you will attract a penalty. You were supposed to have paid before "+spinDateStr, Toast.LENGTH_LONG).show();
                            isPenalized = true;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void startMpesa(View view) {

        phone_number = phoneET.getText().toString();
        amount = amountET.getText().toString();

        if (phone_number.isEmpty()) {
            Toast.makeText(MpesaActivity.this, "Phone Number is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (amount.isEmpty()) {
            Toast.makeText(MpesaActivity.this, "Amount is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!phone_number.isEmpty() && !amount.isEmpty()) {
            try {
                sweetAlertDialog.show();
                mpesa.getToken(this);
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, "UnsupportedEncodingException: " + e.getLocalizedMessage());
            }
        } else {
            Toast.makeText(MpesaActivity.this, "Please make sure that phone number and amount is not empty ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onTokenSuccess(Token token) {
        STKPush stkPush = new STKPush();
        stkPush.setBusinessShortCode(Config.BUSINESS_SHORT_CODE);
        stkPush.setPassword(STKPush.getPassword(Config.BUSINESS_SHORT_CODE, Config.PASSKEY, STKPush.getTimestamp()));
        stkPush.setTimestamp(STKPush.getTimestamp());
        stkPush.setTransactionType(Transaction.CUSTOMER_PAY_BILL_ONLINE);
        stkPush.setAmount(amount);
        stkPush.setPartyA(STKPush.sanitizePhoneNumber(phone_number));
        stkPush.setPartyB(Config.PARTYB);
        stkPush.setPhoneNumber(STKPush.sanitizePhoneNumber(phone_number));
        stkPush.setCallBackURL(Config.CALLBACKURL);
        stkPush.setAccountReference("KChama");
        stkPush.setTransactionDesc("some description");

        mpesa.startStkPush(token, stkPush, new STKListener() {
            @Override
            public void onResponse(STKPushResponse stkPushResponse) {
                Log.e(TAG, "onResponse: " + stkPushResponse.toJson(stkPushResponse));
                String message = "Please enter your pin to complete transaction";
                sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                sweetAlertDialog.setTitleText("Transaction started");
                sweetAlertDialog.setContentText(message);
                //change redirection
//                Toast.makeText(AdminDashboard.this, "Login Successful", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(AdminDashboard.this, UserReg.class);
//                startActivity(intent);
//                finish();
            }

            @Override
            public void onError(Throwable throwable) {
                Log.e(TAG, "stk onError: " + throwable.getStackTrace());
                sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText("Error");
                sweetAlertDialog.setContentText(throwable.getMessage());
            }
        });
    }

    @Override
    public void OnTokenError(Throwable throwable) {
        Log.e(TAG, "mpesa Error: " + throwable.getMessage());
        sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
        sweetAlertDialog.setTitleText("Error");
        sweetAlertDialog.setContentText(throwable.getMessage());
    }
}
