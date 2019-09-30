package com.maradmin.basim.firebase_side;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FireBaseVar {

    public DatabaseReference mMainMenuAr = FirebaseDatabase.getInstance().getReference().child("arabic");
    public DatabaseReference mMainMenuEn = FirebaseDatabase.getInstance().getReference().child("english");
    public DatabaseReference mOrder = FirebaseDatabase.getInstance().getReference().child("orders");
    public DatabaseReference mOffers = FirebaseDatabase.getInstance().getReference().child("offers");
    public DatabaseReference mNotification = FirebaseDatabase.getInstance().getReference().child("notification");
    public DatabaseReference mPager = FirebaseDatabase.getInstance().getReference().child("ads");
    public StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

}
