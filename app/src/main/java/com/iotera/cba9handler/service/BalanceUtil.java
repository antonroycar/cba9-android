package com.iotera.cba9handler.service;

import android.content.SharedPreferences;
import android.util.Log;

public class BalanceUtil extends BVTTYService{
    public static void clrBalance() {
        if (sh != null) {
            int newBalance = 0;
            SharedPreferences.Editor editor = sh.edit();
            editor.putInt("balance", newBalance);
            editor.apply();
            final String strBalance = "Saldo Rp. " + newBalance;
            Log.i("[BV - CBA9]", strBalance);
        }
    }

    public static void addBalance(int cash) {
        if (sh != null) {
            int balance = sh.getInt("balance", 0);
            int newBalance = balance + cash;
            SharedPreferences.Editor editor = sh.edit();
            editor.putInt("balance", newBalance);
            editor.apply();
            final String strBalance = "Saldo Rp. " + newBalance;
            Log.i("[BV - CBA9]", strBalance);
        }
    }

    public static void reduceBalance(int price){
        if (sh != null) {
            int balance = sh.getInt("balance", 0);
            int newBalance = balance - price;
            if (newBalance < 0){
                newBalance = 0;
            }
            SharedPreferences.Editor editor = sh.edit();
            editor.putInt("balance", newBalance);
            editor.apply();
            final String strBalance = "Saldo Rp. " + newBalance;
            Log.i("[BV - CBA9]", strBalance);
        }
    }
}
