package com.shubh.yatri.utils;

import android.widget.Toast;
import android.content.Context;

public class ToastHelper {
    public static void showShort(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
