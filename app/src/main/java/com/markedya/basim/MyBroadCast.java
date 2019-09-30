//package com.markedya.basim;
//
//import android.annotation.SuppressLint;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Build;
//import android.util.Log;
//
//public class MyBroadCast extends BroadcastReceiver {
//
//
//    @SuppressLint("UnsafeProtectedBroadcastReceiver")
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        Log.i("Broadcast Listened", "Service tried to stop");
//        // TODO: This method is called when the BroadcastReceiver is receiving
//        // an Intent broadcast.
//        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            context.startForegroundService(new Intent(context, NotificationService.class));
//        } else {
//            context.startService(new Intent(context, NotificationService.class));
//        }
//
//    }
//}
