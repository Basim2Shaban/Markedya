package com.markedya.basim;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.markedya.basim.Framnts.AboutAppFrag;
import com.markedya.basim.Framnts.Basket;
import com.markedya.basim.Framnts.ContentUsFrag;
import com.markedya.basim.Framnts.HomeFrag;
import com.markedya.basim.Framnts.ProductsFragment;
import com.markedya.basim.reside_menu.ResideMenu;
import com.markedya.basim.reside_menu.ResideMenuItem;

import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static ResideMenu resideMenu;
    private Context mContext;
    private ResideMenuItem itemHome , itemRateApp;
    private ResideMenuItem item_language , itemAboutApp,itemContentUs , itemAboutUs;
    private static boolean lang = true ;
    private static String  languagenow;
    public static int direc ;
    public TextView textCounterNum ;
    public Button btn_Back ;
    private FirebaseAuth mAuth;

    

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            signIn();
        }else{
            Log.e("Email Is :",currentUser.getEmail());
        }



        mContext = this;

        setUpMenu();


// Elcode Alle Bekhalek teftah elfragment ala tool
        if( savedInstanceState == null ){
            changeFragment(new HomeFrag(),"home");
        }

    }

    @SuppressLint({"NewApi", "SetTextI18n"})
    private void setUpMenu() {

        // attach to current activity;
        resideMenu = new ResideMenu(this);

        resideMenu.setBackgroundColor(getResources().getColor(R.color.bg_reside));
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        resideMenu.setScaleValue(0.6f);

        // create menu items;
        createMenuItemsAndClicks();





        if (lang ){
            direc = ResideMenu.DIRECTION_RIGHT;
            resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);
        }else{
            direc = ResideMenu.DIRECTION_LEFT;
            resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
        }
        putItemsInResideAndEdits(direc);



        // You can disable a direction by setting ->
        // resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

        findViewById(R.id.icon_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (direc == 0){
                    resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);

                }else {
                    resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
                }
            }
        });

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {
        if (view == itemHome){
            onItemClick(itemHome);
            changeFragment(new HomeFrag(),"home");
        }
        else if (view == itemRateApp){
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + this.getPackageName())));
            } catch (android.content.ActivityNotFoundException anfe) {
                viewInBrowser(this, "https://play.google.com/store/apps/details?id=" + this.getPackageName());
            }
        }
        else if (view == itemAboutApp){
            onItemClick(itemAboutApp);
            changeFragment(new AboutAppFrag(),"about");
        }
        else if (view == itemContentUs){
            onItemClick(itemContentUs);
            changeFragment(new ContentUsFrag(),"content");
        }

        else if (view == item_language){
            if (lang){
                lang = false ;
                languagenow = "en";
                changeLanguage(this,languagenow);
            }else {
                lang = true ;
                languagenow = "ar";
                changeLanguage(this,languagenow);
            }

        }

        resideMenu.closeMenu();
    }

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {

        }

        @Override
        public void closeMenu() {

        }
    };

    public void changeFragment(Fragment targetFragment,String tag){
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment,tag)
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    // What good method is to access resideMenu？
    public static ResideMenu getResideMenu(){
        return resideMenu;
    }


    public void createMenuItemsAndClicks(){
        itemHome     = new ResideMenuItem(this, 0,getString(R.string.Home));
       // itemAboutUs  = new ResideMenuItem(this, 0,getString(R.string.AboutUs));
        itemRateApp = new ResideMenuItem(this, 0,getString(R.string.RateApp));
        itemAboutApp = new ResideMenuItem(this, 0,getString(R.string.AboutApp));
        itemContentUs = new ResideMenuItem(this, 0,getString(R.string.ConnUs));
        item_language = new ResideMenuItem(this, 0,getString(R.string.Language));


        itemHome.setOnClickListener(this);
      //  itemAboutUs.setOnClickListener(this);
        itemRateApp.setOnClickListener(this);
        itemAboutApp.setOnClickListener(this);
        itemContentUs.setOnClickListener(this);
        item_language.setOnClickListener(this);
    }


    
    public void putItemsInResideAndEdits(int direction){
        resideMenu.addMenuItem(itemHome, direction);
       // resideMenu.addMenuItem(itemAboutUs, direction);
        resideMenu.addMenuItem(itemRateApp, direction);
        resideMenu.addMenuItem(itemAboutApp, direction);
        resideMenu.addMenuItem(itemContentUs, direction);
        resideMenu.addMenuItem(item_language, direction);


        itemHome.setPadding(0,50,0,0);
        itemHome.setGravity(Gravity.CENTER);
        item_language.setPadding(15,140,15,0);
    }


    @SuppressLint("ClickableViewAccessibility")
    public void onItemClick(ResideMenuItem item){
    itemHome.setTextColor(getResources().getColor(R.color.white));
    itemRateApp.setTextColor(getResources().getColor(R.color.white));
//    itemAboutUs.setTextColor(getResources().getColor(R.color.white));
    item_language.setTextColor(getResources().getColor(R.color.white));
    itemAboutApp.setTextColor(getResources().getColor(R.color.white));
    itemContentUs.setTextColor(getResources().getColor(R.color.white));
    if (item != item_language){
        item.setTextColor(getResources().getColor(R.color.black));
    }


    }

    @SuppressLint("NewApi")
    public void changeLanguage(Context ctx, String lang){
            Locale mLocale = new Locale(lang);
            Locale.setDefault(mLocale);
            Configuration config = ctx.getResources().getConfiguration();

            config.locale = mLocale;
            config.setLayoutDirection(config.locale);
            ctx.getResources().updateConfiguration(config, null);
            changeFragment(new HomeFrag(),"home");
            recreate();
    }


    public void collectViews(){
        textCounterNum = (TextView)findViewById(R.id.txtCounterBasket);
        btn_Back = (Button) findViewById(R.id.btn_back);
    }

    public void btnBackAction(){
        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void signIn(){
        mAuth.signInWithEmailAndPassword("basm0590@gmail.com","Asd1996")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.e("Login is Done",": yes");
                        }else{
                            Log.e("Login Is Error : ", "Error");
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
         collectViews();
         btnBackAction();
//         repeatingTask();
    }

    @Override
    public void onBackPressed() {
        HomeFrag homeFrag = (HomeFrag) getSupportFragmentManager().findFragmentByTag("home");
        AboutAppFrag aboutApp = (AboutAppFrag) getSupportFragmentManager().findFragmentByTag("about");
        ContentUsFrag contentUs = (ContentUsFrag) getSupportFragmentManager().findFragmentByTag("content");
        Basket basket = (Basket) getSupportFragmentManager().findFragmentByTag("basket");
        if (homeFrag!= null && homeFrag.isVisible()){
            finish();
        }
        else if (aboutApp != null && aboutApp.isVisible()){
            changeFragment(new HomeFrag() ,"home");
        }
        else if (contentUs != null && contentUs.isVisible()){
            changeFragment(new HomeFrag() ,"home");
        }
        else if (contentUs != null && basket.isVisible()){
            changeFragment(new ProductsFragment(),"products");
        }
        else {
            super.onBackPressed();
        }

    }


//
//    @SuppressLint("ShortAlarm")
//    public void repeatingTask(){
//        AlarmManager alarmManager = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
//        long when = System.currentTimeMillis();         // notification time
//        Intent intent = new Intent(this, MyBroadCast.class);
//        keepingServiceState(true);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,when,1000*60*1, pendingIntent);
//    }

    public void keepingServiceState(boolean service){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putBoolean("service", service);
        editor.commit();

    }

    public static void viewInBrowser(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        if (null != intent.resolveActivity(context.getPackageManager())) {
            context.startActivity(intent);
        }
    }
}

