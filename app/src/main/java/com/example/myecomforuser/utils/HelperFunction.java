package com.example.myecomforuser.utils;

import android.content.Context;
import android.widget.Toast;

public class HelperFunction {
    public static void showToast(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
