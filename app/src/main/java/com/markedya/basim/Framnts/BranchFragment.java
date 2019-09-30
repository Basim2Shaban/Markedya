package com.markedya.basim.Framnts;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.markedya.basim.Firebase_Side.FirebaseVar;
import com.markedya.basim.Firebase_Side.HandleWithData;
import com.markedya.basim.MainActivity;
import com.markedya.basim.R;
import com.markedya.basim.UseAtAll;

/**
 * A simple {@link Fragment} subclass.
 */
public class BranchFragment extends Fragment {
    private RecyclerView recyclerView_branch ;
    private LayoutInflater mLayoutInflater;
    private HandleWithData handleWithData = new HandleWithData();
    private MainActivity mainActivity = new MainActivity();
    private FirebaseVar firebaseVar = new FirebaseVar();
    public static String key ;
    private UseAtAll useAtAll = new UseAtAll();
    private TextView textCounter ;
    private Button buttonBasket ;


    public BranchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_branch, container, false);

        key = getArguments().getString("key"); //fetching value by key


        collectViewsHere(view);



        if (mainActivity.direc == 0){
            handleWithData.getDataToBranch(firebaseVar.mMainMenuEn.child(key).child("branch"),getActivity(),recyclerView_branch);
        }else{
            handleWithData.getDataToBranch(firebaseVar.mMainMenuAr.child(key).child("branch"),getActivity(),recyclerView_branch);
        }


        getCrossToTextCounter(view);
        setTextBar(view);

        return view ;
    }



    public void collectViewsHere(View view){
        recyclerView_branch = (RecyclerView) view.findViewById(R.id.recycler_branch);
        recyclerView_branch.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView_branch.setHasFixedSize(true);

    }


    // this method to cross to text counter from here and also can pass it to anywhere
    @SuppressLint("SetTextI18n")
    public void getCrossToTextCounter(View mView){
        View getActView = getActivity().findViewById(R.id.txtCounterBasket);
        if (getActView instanceof TextView){
            textCounter = (TextView)getActView ;

            if (useAtAll.getData(mView.getContext()) >= 1){
                textCounter.setText(""+useAtAll.getData(mView.getContext()));
            }else{
                textCounter.setVisibility(View.INVISIBLE);
            }

        }
    }
    // this method to cross to btn basket from here and also can pass it to anywhere
    public void getCrossToBasket(final View mView){
        View getActView = getActivity().findViewById(R.id.basket_button);
        if (getActView instanceof Button){
            buttonBasket = (Button)getActView ;
            useAtAll.onBasketClicked(buttonBasket , mView);
        }
    }

    @SuppressLint("SetTextI18n")
    public void setTextBar(View mView){
        View getActView = getActivity().findViewById(R.id.textBar);
        if (getActView instanceof TextView){
            TextView textView = (TextView)getActView ;

            textView.setText(getString(R.string.choosePart));

        }
    }



}
