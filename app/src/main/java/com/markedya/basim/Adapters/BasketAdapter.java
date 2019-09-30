package com.markedya.basim.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.markedya.basim.Framnts.Basket;
import com.markedya.basim.Framnts.HomeFrag;
import com.markedya.basim.MainActivity;
import com.markedya.basim.Models.ProductsModel;
import com.markedya.basim.R;
import com.markedya.basim.UseAtAll;
import java.util.ArrayList;

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.ViewHolde> {
    ArrayList<ProductsModel> arrayList ;
    Context context ;
    TextView textViewCounter ;
    private MainActivity mainActivity = new MainActivity();
    private UseAtAll useAtAll = new UseAtAll();
    private  int m ;


    public BasketAdapter(ArrayList<ProductsModel> arrayList, Context context, TextView textViewCounter) {
        this.arrayList = arrayList;
        this.context = context;
        this.textViewCounter = textViewCounter;
    }

    @NonNull
    @Override
    public ViewHolde onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_basket_info,viewGroup,false);

        return new ViewHolde(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolde viewHolde, int i) {
        if (mainActivity.direc == 0){
            viewHolde.textName.setText(arrayList.get(i).getEnglish_name());
        }else {
            viewHolde.textName.setText(arrayList.get(i).getArabic_name());
        }



        viewHolde.textWieght.setText(arrayList.get(i).getUnit() + " "+context.getString(R.string.kg));

        viewHolde.textPrice.setText(arrayList.get(i).getPrice() +" "+ context.getString(R.string.Le));

        if (Double.parseDouble(arrayList.get(i).getUnit()) >= 1.0){
            double value = Double.parseDouble(arrayList.get(i).getUnit() ) * Double.parseDouble(arrayList.get(i).getPrice()) ;
            viewHolde.textMuchPrice.setText(String.valueOf(value +" " + context.getString(R.string.Le) ));
        }
        else {
          double correctNum =  Double.parseDouble(arrayList.get(i).getUnit()) * 1000 /100 ;
          double priceOfElements = Double.parseDouble(arrayList.get(i).getPrice()) / 10 ;
          double value = correctNum * priceOfElements ;
          viewHolde.textMuchPrice.setText(value +" " + context.getString(R.string.Le) );
        }


        onCancelClick(viewHolde,i);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }



    class ViewHolde extends RecyclerView.ViewHolder{
        TextView textName , textWieght , textMuchPrice , textPrice ;
        Button btnCancel ;

        public ViewHolde(@NonNull View itemView) {
            super(itemView);
            textName = (TextView)itemView.findViewById(R.id.txt_name_bask);
            textWieght = (TextView)itemView.findViewById(R.id.txt_wieght_bask);
            textPrice = (TextView)itemView.findViewById(R.id.txt_price_bask);
            textMuchPrice = (TextView)itemView.findViewById(R.id.txt_priceOfMuch_bask);
            btnCancel = (Button) itemView.findViewById(R.id.btn_removeItem_bask);
        }
    }


    // btn remove method
    public void onCancelClick(final ViewHolde viewHolde , final int posistion){
        viewHolde.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayList.remove(posistion);
                notifyItemRemoved(posistion);
                notifyDataSetChanged();
                // update data after removing
                useAtAll.keepData(arrayList , context);
                // remove the main list in basket and renew data
                ProductsAdapter.basket_list.clear();
                ProductsAdapter.basket_list.addAll(useAtAll.getDataForList(context));


                Log.e("Size is :" , arrayList.size()+"");

                // handle with text counter
                if (arrayList.size() != 0){
                    textViewCounter.setVisibility(View.VISIBLE);
                    textViewCounter.setText(arrayList.size()+"");
                }else{
                    textViewCounter.setVisibility(View.INVISIBLE);
                }
                /////

                arrayList.size();

                // update after removed
                if (arrayList.size() == 0){
                    AppCompatActivity activity = (AppCompatActivity) viewHolde.itemView.getContext();
                    Fragment myFragment = new HomeFrag();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, myFragment,"home")
                            .addToBackStack(null).commit();
                }else {
                    // remember the last line for refresh fragment not just set it like a first time
                    Fragment myFragment = new Basket();
                    AppCompatActivity activity = (AppCompatActivity) viewHolde.itemView.getContext();
                    FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment,myFragment,"basket");
                    ft.detach(myFragment).attach(myFragment).commit();
                }



            }
        });
    }




}
