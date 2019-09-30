package com.markedya.basim.Firebase_Side;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.markedya.basim.Adapters.BranchAdapter;
import com.markedya.basim.Adapters.MainMenuAdapter;
import com.markedya.basim.Models.MainMenuModel;

import java.util.ArrayList;

public class HandleWithData {
    public void getDataToMainMenu(final DatabaseReference var  , final Context context , final RecyclerView recyclerView){
        final ArrayList<MainMenuModel> arrayList = new ArrayList<>();
        arrayList.clear();

        var.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    String key = dataSnapshot1.getKey();
                    String image = dataSnapshot1.child("image").getValue().toString();
                    String title = dataSnapshot1.child("title").getValue().toString();
                    MainMenuModel models = new MainMenuModel(key , image , title);
                    arrayList.add(models);
                }

                MainMenuAdapter adapter = new MainMenuAdapter(arrayList,context);
                recyclerView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    public void getDataToBranch(final DatabaseReference var  , final Context context , final RecyclerView recyclerView ){
        final ArrayList<MainMenuModel> arrayList = new ArrayList<>();
        arrayList.clear();

        var.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    String key = dataSnapshot1.getKey();
                    String image = dataSnapshot1.child("image").getValue().toString();
                    String title = dataSnapshot1.child("title").getValue().toString();
                    MainMenuModel models = new MainMenuModel(key , image , title);
                    arrayList.add(models);
                }

                BranchAdapter viewPagerAdapter = new BranchAdapter( arrayList , context );
                recyclerView.setAdapter(viewPagerAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
