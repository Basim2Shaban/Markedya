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
import com.markedya.basim.Framnts.BranchFragment;
import com.markedya.basim.Models.MainMenuModel;
import com.markedya.basim.R;

import java.util.ArrayList;

public class MainMenuAdapter extends RecyclerView.Adapter<MainMenuAdapter.VHolder> {
private ArrayList<MainMenuModel> arrayList ;
private Context mContext ;


public MainMenuAdapter(ArrayList<MainMenuModel> arrayList, Context mContext) {
        this.arrayList = arrayList;
        this.mContext = mContext;
        }



@NonNull
@Override
public VHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view =  LayoutInflater.from(viewGroup.getContext())
        .inflate(R.layout.row_main_menu, viewGroup, false);

        return new VHolder(view);
        }

@SuppressLint("NewApi")
@Override
public void onBindViewHolder(@NonNull final VHolder vHolder, @SuppressLint("RecyclerView") final int i) {
        Glide.with(mContext).load(arrayList.get(i).getImage()).into(vHolder.imageViewMain);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
    {
        vHolder.imageViewMain.setClipToOutline(true);
    }
        vHolder.textViewTitle.setText(arrayList.get(i).getTitle());


        vHolder.itemView.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        AppCompatActivity activity = (AppCompatActivity) vHolder.itemView.getContext();
        Fragment myFragment = new BranchFragment();
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
    private ImageView imageViewMain ;

    @SuppressLint("NewApi")
    public VHolder(@NonNull View itemView) {
        super(itemView);
        textViewTitle =(TextView)itemView.findViewById(R.id.raw_main_title);
        imageViewMain = (ImageView)itemView.findViewById(R.id.raw_image_title);
    }
}

}
