package com.example.myshoppingapp;

import android.content.SharedPreferences;

public class Cart {
    static private SharedPreferences preferences = null;
    static public int money = 0; //in cents
    final static private String MoneyID = "MONEY";

    static public void InitCart(SharedPreferences pref)
    {
        preferences = pref;
        money = preferences.getInt(MoneyID, 10000); //Default $100
    }

    static public void AddMoney(int cents)
    {
        money += cents;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(MoneyID, money);
        editor.commit();
    }
}
