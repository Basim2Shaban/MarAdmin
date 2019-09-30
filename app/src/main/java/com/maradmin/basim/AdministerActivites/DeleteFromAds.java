package com.maradmin.basim.AdministerActivites;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.maradmin.basim.AddNewAd;
import com.maradmin.basim.AdministerAdapters.AdsAdapter;
import com.maradmin.basim.AdministerModels.ModelAds;
import com.maradmin.basim.R;
import com.maradmin.basim.firebase_side.FireBaseVar;

import java.util.ArrayList;

public class DeleteFromAds extends AppCompatActivity {
    private RecyclerView recyclerView ;
    private FireBaseVar fireBaseVar = new FireBaseVar() ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_from_ads);

        recyclerView = (RecyclerView)findViewById(R.id.rec_viewAds);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);



        getAdsForRec();

    }

    public void getAdsForRec(){
        final ArrayList<ModelAds> arrayList = new ArrayList<>();
        fireBaseVar.mPager.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                if (dataSnapshot.getChildrenCount() != 0){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        String key = snapshot.getKey();
                        String title = snapshot.child("title").getValue().toString();
                        String image = snapshot.child("image").getValue().toString();
                        ModelAds modelAds = new ModelAds(key,title,image);
                        arrayList.add(modelAds);
                    }
                    AdsAdapter adsAdapter = new AdsAdapter(arrayList,DeleteFromAds.this);
                    recyclerView.setAdapter(adsAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(DeleteFromAds.this,AddNewAd.class));
    }
}
