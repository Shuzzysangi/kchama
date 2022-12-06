package com.sharon.sample.mpesa;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores user payment information
 */
public class UserPayment {
    private String id ,mPesaCode, purpose, name, phoneNumber;
    private int amount;
    boolean verified = false;


    public UserPayment() {
        // required default constructor
    }

    public String getId() {return  id;}
    public String getMpesaCode() {
        return mPesaCode;
    }
    public String getPurpose() {
        return purpose;
    }
    public boolean isVerified(){ return verified;}

    public int getAmount() {
        return amount;
    }
    public String getName(){return  name;}
    public String getPhoneNumber(){return phoneNumber;}


    // setters
    // setters
    public void setId(String id) {this.id = id;}
    public void setMpesaCode(String mPesaCode) {
        this.mPesaCode = mPesaCode;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
    public void setVerified(boolean flag){ this.verified = flag;}

    public void setName(String name) {this.name = name;}
    public void setPhoneNumber(String phoneNumber){this.phoneNumber = phoneNumber;}

    public Map<String, Object> toMap(){
        Map<String, Object> info = new HashMap<>();
        info.put("id", id);
        info.put("mPesaCode", mPesaCode);
        info.put("amount", amount);
        info.put("purpose", purpose);
        info.put("phoneNumber", "");
        info.put("verified", verified);
        return  info;
    }
}
