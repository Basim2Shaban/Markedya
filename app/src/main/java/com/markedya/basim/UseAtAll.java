package com.markedya.basim;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.markedya.basim.Framnts.Basket;
import com.markedya.basim.Models.ProductsModel;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class UseAtAll {
ArrayList<ProductsModel> basketList ;

    // call it for get data in shared and check from size
    public int getData(final Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString("basket_list", null);
        Type type = new TypeToken<ArrayList<ProductsModel>>() {}.getType();
        basketList = new Gson().fromJson(json, type);

        if (json == null){
            return 0 ;
        }else{
            return basketList.size() ;
        }

    }

    // call it for get data in shared and put it in list
    public ArrayList<ProductsModel> getDataForList(final Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString("basket_list", null);
        Type type = new TypeToken<ArrayList<ProductsModel>>() {}.getType();
        basketList = new Gson().fromJson(json, type);

        return basketList ;
    }

    // to handle with basket icon in an everywhere fro the app
    public void onBasketClicked(Button buttonBasket , final View mView){
        buttonBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getData(mView.getContext()) != 0 ){
                    AppCompatActivity activity = (AppCompatActivity) mView.getContext();
                    Fragment myFragment = new Basket();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, myFragment,"basket")
                            .addToBackStack(null).commit();
                }else {
                    Toast toast = Toast.makeText(mView.getContext(),mView.getContext().
                            getString(R.string.emptyCar), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM,1,1);
                    toast.setMargin(0,0);
                    toast.show();
                }

            }
        });
    }

    // call it to keep data from everywhere
    public void keepData(ArrayList<ProductsModel> basket_list , Context context){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        String json = new Gson().toJson(basket_list);
        editor.putString("basket_list", json);
        editor.commit();

    }


    public void setAnimaition(TextView textView , Context context){
        Animation animation = AnimationUtils.loadAnimation(context,
                R.anim.anim_text_scale);
        textView.startAnimation(animation);
    }


    public void playSoundRemove(Context context){
        MediaPlayer mp = MediaPlayer.create(context, R.raw.remove);
        mp.start();
    }

    public void playSoundAdd(Context context){
        MediaPlayer mp = MediaPlayer.create(context, R.raw.add);
        mp.start();
    }


}
