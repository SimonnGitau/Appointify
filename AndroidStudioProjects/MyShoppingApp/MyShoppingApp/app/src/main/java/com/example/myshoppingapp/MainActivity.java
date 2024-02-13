package com.example.myshoppingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    public static ConfirmPurchaseDialog confirmPurchaseDialog = null;
    public static SharedPreferences preferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Cart.InitCart(preferences);

        confirmPurchaseDialog = new ConfirmPurchaseDialog(this);

        ShopDataManager.InitShopItems(findViewById(R.id.mainLayout), this);
    }
}