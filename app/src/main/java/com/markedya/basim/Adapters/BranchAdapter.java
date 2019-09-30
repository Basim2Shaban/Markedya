package com.markedya.basim.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.markedya.basim.Framnts.ProductsFragment;
import com.markedya.basim.Models.MainMenuModel;
import com.markedya.basim.R;

import java.util.ArrayList;

public class BranchAdapter  extends RecyclerView.Adapter<BranchAdapter.VHolder> {
    private ArrayList<MainMenuModel> arrayList ;
    private Context mContext ;


    public BranchAdapter(ArrayList<MainMenuModel> arrayList, Context mContext) {
        this.arrayList = arrayList;
        this.mContext = mContext;
    }



    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view =  LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.raw_branch, viewGroup, false);

        return new VHolder(view);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull final VHolder vHolder, final int i) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            vHolder.imageView.setClipToOutline(true);
        }
        Glide.with(mContext).load(arrayList.get(i).getImage()).into(vHolder.imageView);
        vHolder.textViewTitle.setText(arrayList.get(i).getTitle());


        vHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) vHolder.itemView.getContext();
                Fragment myFragment = new ProductsFragment();
                Bundle bundle=new Bundle();
                bundle.putString("key", arrayList.get(i).getKey()); //key and value
                myFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, myFragment)
                        .addToBackStack(null).commit();
            }
        });
    }



    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class VHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle ;
        private ImageView imageView ;

        @SuppressLint("NewApi")
        public VHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle =(TextView)itemView.findViewById(R.id.text_viewPager);
            imageView = (ImageView)itemView.findViewById(R.id.img_viewPager);
        }
    }
}