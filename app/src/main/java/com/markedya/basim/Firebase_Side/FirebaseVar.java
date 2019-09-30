package com.markedya.basim.Firebase_Side;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseVar {
    public DatabaseReference mMainMenuEn = FirebaseDatabase.getInstance().getReference().child("english");
    public DatabaseReference mMainMenuAr = FirebaseDatabase.getInstance().getReference().child("arabic");
    public DatabaseReference mOrders = FirebaseDatabase.getInstance().getReference().child("orders");
    public DatabaseReference mOffers = FirebaseDatabase.getInstance().getReference().child("offers");
    public DatabaseReference mNotification = FirebaseDatabase.getInstance().getReference().child("notification");
    public DatabaseReference mPager = FirebaseDatabase.getInstance().getReference().child("ads");
}
