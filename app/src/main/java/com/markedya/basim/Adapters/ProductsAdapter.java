package com.markedya.basim.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.markedya.basim.MainActivity;
import com.markedya.basim.Models.ProductsModel;
import com.markedya.basim.R;
import com.markedya.basim.UseAtAll;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHold> {
    Context context ;
    ArrayList<ProductsModel> arrayList ;
    TextView textViewCounter ;
    private MainActivity mainActivity = new MainActivity();
    public static ArrayList<ProductsModel> basket_list = new ArrayList();
    private static ArrayList<ProductsModel> getBasketList = new ArrayList();
    private UseAtAll useAtAll = new UseAtAll();
    public static double unitWieght ;


    public ProductsAdapter(Context context, ArrayList<ProductsModel> arrayList, TextView textViewCounter) {
        this.context = context;
        this.arrayList = arrayList;
        this.textViewCounter = textViewCounter;
    }



    @NonNull
    @Override
    public ViewHold onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_products , viewGroup , false);
        return new ViewHold(view);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull final ViewHold viewHold, final int position) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            viewHold.imageViewPro.setClipToOutline(true);
        }

        // this for check from the language before set the data
        if (mainActivity.direc == 0){
            viewHold.textViewName.setText(arrayList.get(position).getEnglish_name());
        }else{
            viewHold.textViewName.setText(arrayList.get(position).getArabic_name());
        }
        //////////////////////////////

        // this code for set the weight who the user add it before
        try {
            getBasketList.clear();
            if (useAtAll.getData(context) != 0) {
                getBasketList.addAll(useAtAll.getDataForList(context));
            }
            for (int i = 0; i < getBasketList.size(); i++) {
                if (arrayList.get(position).getKey().equals(getBasketList.get(i).getKey())) {
                    viewHold.textViewUnit.setText(getBasketList.get(i).getUnit());
                }
            }

        }catch (Exception e){
            Log.e(" error is : " , e.getMessage());
        }
        ////////////////////////////////

        Glide.with(context).load(arrayList.get(position).getImage()).into(viewHold.imageViewPro);
        viewHold.textViewPrice.setText(arrayList.get(position).getPrice()+ " " + context.getString(R.string.pound));



        // to get unit weight from data and convert it to value i can use it in the basket accounts
        switch (arrayList.get(position).getUnit()){
            case "100":
                unitWieght = 0.1 ;
                break;
            case "500":
                unitWieght = 0.5 ;
                break;
            case "1000":
                unitWieght = 1 ;
                break;
        }


        onAddClick(viewHold,position);
        onRemoveClick(viewHold , position);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }



    class ViewHold extends RecyclerView.ViewHolder{
       public TextView textViewName , textViewPrice , textViewUnit ;
       public ImageView imageViewPro ;
       public Button btnAddToBask , btnRemoveFromBask ;


        private ViewHold(@NonNull View itemView) {
            super(itemView);
            textViewName = (TextView)itemView.findViewById(R.id.produc_name);
            textViewPrice = (TextView)itemView.findViewById(R.id.produc_price);
            textViewUnit = (TextView)itemView.findViewById(R.id.produc_howMuch_txt);
            imageViewPro = (ImageView) itemView.findViewById(R.id.produc_image);
            btnAddToBask = (Button) itemView.findViewById(R.id.produc_addBtn);
            btnRemoveFromBask = (Button) itemView.findViewById(R.id.produc_removeBtn);
        }
    }


    // this method for use in btn add state
    @SuppressLint("SetTextI18n")
    public void checkFromItemHereOrNot(int position , ViewHold viewHold){
        boolean find = false;
        // the code in for , for check from the old item if it found let it and set the weight update to it
        for (int i = 0; i < basket_list.size(); i++) {
            if (arrayList.get(position).getKey().equals(basket_list.get(i).getKey())){
                ProductsModel productsModel = arrayList.get(position);
                productsModel.setUnit(viewHold.textViewUnit.getText().toString());
                basket_list.set(i,productsModel);
                find = true;
                break;
            }
        }
        if(!find) {
            // this for set the new item to list if it is not inside ;
            putDataInBasketList(viewHold.textViewUnit.getText().toString(), position);
        }
        // this for keep data after update
        useAtAll.keepData(basket_list ,context);
        // send count to text counter in basket
        if (basket_list.size() != 0){
            textViewCounter.setVisibility(View.VISIBLE);
            textViewCounter.setText(basket_list.size()+"");
        }else{
            textViewCounter.setVisibility(View.INVISIBLE);
        }
    }


    // this method for do btn onClick inside it ;
    private void onAddClick(final ViewHold viewHold , final int position){
        try {
            if (basket_list.size()== 0){
                if (useAtAll.getData(context) != 0){
                    basket_list.addAll(useAtAll.getDataForList(context));

                    basket_list.size();
                }
            }
        }catch (Exception e){
            Log.e(" error is : " , e.getMessage());
        }


        viewHold.btnAddToBask.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                double dbInText = Double.parseDouble(viewHold.textViewUnit.getText().toString());
                float finalCount =  Float.valueOf(String.valueOf(dbInText + unitWieght));
                viewHold.textViewUnit.setText(finalCount +"");

                checkFromItemHereOrNot(position , viewHold);
                useAtAll.keepData(basket_list,context);
                useAtAll.setAnimaition(textViewCounter,context);
                useAtAll.playSoundAdd(context);
            }
        });
    }

    // this method for do btn onRemove inside it
    private void onRemoveClick(final ViewHold viewHold , final int position){
        viewHold.btnRemoveFromBask.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                float finalCount = 0;
                // get the weight in textView then i am subtract unitWeight from it and sent value to textV
                double dbInText = Double.parseDouble(viewHold.textViewUnit.getText().toString());
               if (dbInText != 0.0 && dbInText != 0){
                    finalCount = Float.valueOf(String.valueOf(dbInText - unitWieght));
                   viewHold.textViewUnit.setText(finalCount +"");
               }


               // for memory this for loop just update the value in list and after out we keeping the update
                for (int i = 0 ;i<basket_list.size() ;i++){
                   // if the key in this activity maine list is equal the key in keeping list do those conditions
                    if (arrayList.get(position).getKey().equals(basket_list.get(i).getKey())){
                        double test = Double.parseDouble(basket_list.get(i).getUnit());
                        // if the weight still not equal zero update it in basket list then we will keep
                        if (test >= 0.1 && test != 0.0){
                            ProductsModel productsModel = arrayList.get(position);
                            productsModel.setUnit(viewHold.textViewUnit.getText().toString());
                            // play sound here
                            useAtAll.playSoundRemove(context);
                            basket_list.set(i,productsModel);
                        }
                        // if the weight is equal zero remove it from basket list then we will keep
                        if (Double.parseDouble(basket_list.get(i).getUnit()) == 0.0){
                            // play sound here
                            useAtAll.playSoundRemove(context);
                            basket_list.remove(i);
                        }

                    }
                }


                // keep data after removing update who above in for loop
                useAtAll.keepData(basket_list,context);
               // code for set count update to text counter
               if (basket_list.size() != 0){
                   textViewCounter.setVisibility(View.VISIBLE);
                   useAtAll.setAnimaition(textViewCounter,context);
                   textViewCounter.setText(basket_list.size()+"");
               }else{
                   textViewCounter.setVisibility(View.INVISIBLE);
               }

            }
        });
    }


    // this method to use in btn add item to basket but if this item was'nt there ;
    private void putDataInBasketList(String unit , int position){
        String key = arrayList.get(position).getKey();
        String image = arrayList.get(position).getImage();
        String arabic_name = arrayList.get(position).getArabic_name();
        String english_name = arrayList.get(position).getEnglish_name();
        String price = arrayList.get(position).getPrice();
        ProductsModel productsModel = new ProductsModel(key,image,arabic_name,english_name,price,unit);
        basket_list.add(productsModel);

    }




}
