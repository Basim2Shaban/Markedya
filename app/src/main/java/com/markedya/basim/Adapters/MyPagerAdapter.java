package com.markedya.basim.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.markedya.basim.Models.PagerModel;
import com.markedya.basim.R;

import java.util.ArrayList;

public class MyPagerAdapter extends PagerAdapter {
    private ArrayList<PagerModel> arrayList ;
    private Context mContext ;
    private LayoutInflater layoutInflater ;


    public MyPagerAdapter(ArrayList<PagerModel> arrayList, Context mContext , LayoutInflater layoutInflater) {
        this.arrayList = arrayList;
        this.mContext = mContext;
        this.layoutInflater = layoutInflater ;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == ((LinearLayout) o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.item_view_pager,container,false);

        ImageView imageView = (ImageView)view.findViewById(R.id.pager_image);
        TextView textView = (TextView)view.findViewById(R.id.pager_text);

        if (arrayList.get(position).getTitle().equals("false")){
            textView.setText("");
        }else{
            textView.setText(arrayList.get(position).getTitle());
        }
        Glide.with(mContext).load(arrayList.get(position).getImage()).into(imageView);


        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);

        return view ;
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }





    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}
