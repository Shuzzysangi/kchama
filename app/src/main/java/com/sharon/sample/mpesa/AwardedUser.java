package com.sharon.sample.mpesa;

public class AwardedUser extends  User{
    private String awardedDate;
    private double awardedAmount;
    public AwardedUser(){
        // required public constructor
    }

    // setters
    public void setAwardedDate(String awardDate) {
        this.awardedDate = awardDate;
    }

    public void setAwardedAmount(double awardedAmount) {
        this.awardedAmount = awardedAmount;
    }

    // getters

    public double getAwardedAmount() {
        return awardedAmount;
    }
    public String getAwardedDate() {
        return awardedDate;
    }
}
