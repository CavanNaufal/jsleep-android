package com.MuhammadCavanNaufalAziziJSleepDN.jsleep_android.model;


public class Account extends Serializable{
    public String name, email, password;
    public Renter renter;
    public double balance;

    @Override
    public String toString(){
        return "Account{" +
                "balance=" + balance +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", renter=" + renter +
                '}';
    }
}
