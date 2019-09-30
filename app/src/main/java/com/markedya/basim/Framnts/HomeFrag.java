package com.markedya.basim.Framnts;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.markedya.basim.Adapters.MyPagerAdapter;
import com.markedya.basim.Firebase_Side.FirebaseVar;
import com.markedya.basim.Firebase_Side.HandleWithData;
import com.markedya.basim.MainActivity;
import com.markedya.basim.Models.PagerModel;
import com.markedya.basim.R;
import com.markedya.basim.UseAtAll;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFrag extends Fragment {
private HandleWithData handleWithData = new HandleWithData();
private FirebaseVar firebaseVar = new FirebaseVar();
private RecyclerView recyclerView ;
private MainActivity mainActivity = new MainActivity();
private UseAtAll useAtAll = new UseAtAll();
private TextView textCounter ;
private Button buttonBasket ;
private ViewPager viewPager ;
private TabLayout tabLayout ;
private int CURRENTNUMER ;
private LayoutInflater layoutInflater ;
private ArrayList<PagerModel> arrayListPager = new ArrayList<>();




    public HomeFrag() {
        // Required empty public constructor
    }


    @SuppressLint({"NewApi", "WrongConstant", "SetTextI18n"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        collectViewsHere(view);
        getCrossToBasket(view);
        getCrossToTextCounter(view);

        /*

      /// this code for handle with custom dialog
        try {
            if (getOpenCount() == 0){
                customDialog(view);
                keepOpendNewCount(getOpenCount()+1);
            }

            else if (getOpenCount() == 5){
                customDialog(view);
                keepOpendNewCount(1);
            }else{
                keepOpendNewCount(getOpenCount()+1);
            }
        }catch (Exception e){
            Log.e("error because :",e.getMessage());
        }
        ///////////////////////////////

        */

        setTextBar(view);

//        // handle with service here
//        if (getServiceStatus() == false){
//            view.getContext().startService(new Intent(view.getContext(),NotificationService.class));
//            keepingServiceState(true);
//        }


        // view pager and his methods here
        handleWithViewPagerAds();



        // for select language arabic or english
        if (mainActivity.direc == 0){
            handleWithData.getDataToMainMenu(firebaseVar.mMainMenuEn,getActivity(),recyclerView);
        }else {
            handleWithData.getDataToMainMenu(firebaseVar.mMainMenuAr,getActivity(),recyclerView);
        }


        return view ;
    }




    public void collectViewsHere(View v){
        recyclerView = (RecyclerView)v.findViewById(R.id.recycler_main_menu);
        viewPager = (ViewPager)v.findViewById(R.id.viewPager_ads);
        tabLayout = (TabLayout) v.findViewById(R.id.tab_layout);
        layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

    }


    // for importing data from firebase and send them to viewPager
    public void handleWithViewPagerAds(){
        firebaseVar.mPager.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                arrayListPager.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    PagerModel pagerModel = snapshot.getValue(PagerModel.class);
                    arrayListPager.add(pagerModel);
                }

                MyPagerAdapter adapter = new MyPagerAdapter(arrayListPager, getActivity(),layoutInflater);
                viewPager.setAdapter(adapter);
                tabLayout.setupWithViewPager(viewPager, true);
                adapter.notifyDataSetChanged();

                changeCurrentPage(viewPager,arrayListPager);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    // this method for change pages in viewPager AutoMatically and it should be like this
    public void changeCurrentPage(final ViewPager viewPager, final ArrayList<PagerModel> arrayList){
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (getActivity() != null){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (CURRENTNUMER == arrayList.size()) {
                                CURRENTNUMER = 0;
                            }

                            viewPager.setCurrentItem(CURRENTNUMER++, true);
                        }
                    });
                }

            }
        },3000,2000);
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



    // initialisation custom dialog here
    public void customDialog(View view){
        final Dialog dialog = new Dialog(view.getContext());
        Window window = dialog.getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        TextView textTitle = dialog.findViewById(R.id.textName_Dialog);
        ImageView imageOffer = dialog.findViewById(R.id.imgOfferDialog);
        dialog.setCancelable(false);
        dialog.show();
        getDataForDialog(textTitle,imageOffer,view);

        dialog.findViewById(R.id.img_cancelDialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }

    // get data from firebase and set it to custom dialog on start
    public void getDataForDialog(final TextView title , final ImageView imagOffer , final View view){
        firebaseVar.mOffers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String arabic_tit = dataSnapshot.child("arabic_name").getValue().toString();
                String english_tit = dataSnapshot.child("english_name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();

                if (mainActivity.direc == 0){
                    if (image != null){
                        title.setText(english_tit);
                    }
                }else{
                    if (image != null){
                        title.setText(arabic_tit);
                    }
                }

                try {
                    Glide.with(view.getContext()).load(image).into(imagOffer);
                }catch (Exception e){
                    Log.e("ERROR Because : ",e.getMessage());
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // this method for keep how many times did user open this class to show the dialog
    @SuppressLint("ApplySharedPref")
    public void keepOpendNewCount(int value){
        SharedPreferences.Editor preferences = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
        preferences.putInt("count",value);
        preferences.commit();
    }

    // and this is get the keeping
    public int getOpenCount(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return sharedPreferences.getInt("count",0);
    }

    // to check from the service state _ to stay away of the repeating service
    public void keepingServiceState(boolean service){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
        editor.putBoolean("service", service);
        editor.commit();

    }

    public boolean getServiceStatus(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return preferences.getBoolean("service",false);
    }


    @SuppressLint("SetTextI18n")
    public void setTextBar(View mView){
        View getActView = getActivity().findViewById(R.id.textBar);
        if (getActView instanceof TextView){
            TextView textView = (TextView)getActView ;

            textView.setText(getString(R.string.MHome));

        }
    }



}
