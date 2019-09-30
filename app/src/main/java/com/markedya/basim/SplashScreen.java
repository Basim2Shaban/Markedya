package com.markedya.basim;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class SplashScreen extends AppCompatActivity {
    private boolean connected = false;
    private ProgressBar progressBar ;
    private TextView textView ;
    private Button button ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        collectViewsHere();
        checkFromInternet();
        onBtnClickReload(button);
        conditionAfterTheCheck();


    }


    @SuppressLint("MissingPermission")
    public void checkFromInternet(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else{
            connected = false;
        }

    }
    public void collectViewsHere(){
        progressBar = (ProgressBar)findViewById(R.id.progressBar_splash);
        textView = (TextView)findViewById(R.id.textView_error_conn);
        button = (Button)findViewById(R.id.btn_reload);
    }
    public void onBtnClickReload(Button bt){
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connected = true ;
                progressBar.setVisibility(View.VISIBLE);
                button.setVisibility(View.INVISIBLE);
                textView.setVisibility(View.INVISIBLE);
                checkFromInternet();
                conditionAfterTheCheck();
            }
        });
    }
    public void conditionAfterTheCheck(){
        if (connected) {
            progressBar.setVisibility(View.VISIBLE);
            button.setEnabled(false);
            button.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.INVISIBLE);

            Timer  mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashScreen.this , MainActivity.class));
                    SplashScreen.this.finish();
                }
            }, 1000);
        }else{
            progressBar.setVisibility(View.INVISIBLE);
            button.setEnabled(true);
            button.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        }
    }
}
