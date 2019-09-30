//package com.markedya.basim;
//
//import android.annotation.SuppressLint;
//import android.annotation.TargetApi;
//import android.app.AlarmManager;
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.opengl.Visibility;
//import android.os.Build;
//import android.os.IBinder;
//import android.preference.PreferenceManager;
//import android.support.v4.app.NotificationCompat;
//import android.util.Log;
//import android.view.View;
//import android.widget.Toast;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.ValueEventListener;
//import com.markedya.basim.Firebase_Side.FirebaseVar;
//import com.markedya.basim.Framnts.HomeFrag;
//import java.util.Random;
//import java.util.Timer;
//import java.util.TimerTask;
//
//public class NotificationService extends Service {
//    private FirebaseVar firebaseVar = new FirebaseVar();
//    private static String number = null;
//
//    public NotificationService() {
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        // TODO: Return the communication channel to the service.
//        return null;
//    }
//
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        int NOTIFICATION_ID = (int) (System.currentTimeMillis()%10000);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForeground(NOTIFICATION_ID, new Notification.Builder(this).build());
//        }
//    }
//
//
//    @SuppressLint("LongLogTag")
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        onDataChange();
//        Log.e("From NotiFication Service number is equal : ", " "+ getUserState());
//        return Service.START_STICKY ;
//    }
//
//    @Override
//    public void onDestroy() {
//        repeatingTask();////////////
//        super.onDestroy();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            stopForeground(true);
//        } else {
//            stopSelf();
//        }
//    }
//
//
//    @Override
//    public void onTaskRemoved(Intent rootIntent) {
//
//        //When remove app from background then start it again
//        startService(new Intent(this, NotificationService.class));
//
//        super.onTaskRemoved(rootIntent);
//    }
//
//
//    @TargetApi(Build.VERSION_CODES.O)
//    public void pushNotification(String title , String message){
//        Random random = new Random();
//        int id = random.nextInt(50*160);
//        long[] pattern = {500, 500, 500};
//
//        NotificationChannel channel = new NotificationChannel(getString(id),"ll" , NotificationManager.IMPORTANCE_DEFAULT);
//
//        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
//
//        Notification notification = new NotificationCompat.Builder(this, getString(id))
//                .setContentTitle(title)
//                .setContentText(message)
//                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
//                        R.mipmap.ic_launcher))
//                .setSmallIcon( R.mipmap.ic_launcher)
//                .setVibrate(pattern)
//                .setAutoCancel(true).build();
//
//        startForeground(id,notification);
//
//      //  Uri uri = Uri.parse("android.resource://"
//          //      + this.getPackageName() + "/" + R.raw.plucky);
//
//      //  builder.setSound(uri);
//
//
//
///*
//        // this code for intent after clicked
//        Intent appActivityIntent = new Intent(this, MainActivity.class);
//        PendingIntent contentAppActivityIntent =
//                PendingIntent.getActivity(this, 0, appActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        notification.setContentIntent(contentAppActivityIntent);
//        ///////////////////
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        notificationManager.notify(id, builder.build());
//
//*/
//    }
//
//
//    public void onDataChange(){
//        firebaseVar.mNotification.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String title = dataSnapshot.child("title").getValue().toString();
//                String message = dataSnapshot.child("message").getValue().toString();
//                number = dataSnapshot.child("number").getValue().toString();
//
//                try {
//
//                    if (getUserState()== null){
//                        pushNotification(title, message);
//                        keepNotificaionState(number);
//                        number = null ;
//
//                    }
//                    else{
//                        if (!getUserState().equals(number)) {
//                            pushNotification(title, message);
//                            keepNotificaionState(number);
//                            number = null;
//                        }
//                    }
//
//                }catch (Exception e){
//                    Log.e("message",e.getMessage());
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//    }
//
//
//    public void timer(){
//        Timer timer = new Timer();
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                onDataChange();
//                Log.e("number is equal : ", " "+ getUserState());
//
//            }
//        },30*1000,1000*60*20);
//    }
//
//    public void repeatingTask(){
//        long ct = System.currentTimeMillis(); //get current time
//        AlarmManager mgr=(AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
//        Intent i= new Intent(getApplicationContext(), NotificationService.class);
//        PendingIntent pi=PendingIntent.getService(getApplicationContext(), 0, i, 0);
//
//        mgr.set(AlarmManager.RTC_WAKEUP, ct + 60000 , pi); //60 seconds is 60000 milliseconds
//    }
//
//
//    private void keepNotificaionState(String state){
//        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
//        editor.putString("state", state);
//        editor.commit();
//
//    }
//
//    public String getUserState(){
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        return prefs.getString("state",null);
//    }
//
//
//}
