package com.maradmin.basim;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.maradmin.basim.AdministerAdapters.AdapterOrdersView;
import com.maradmin.basim.AdministerModels.ModelOrders;
import com.maradmin.basim.firebase_side.FireBaseVar;

import java.util.ArrayList;

public class OrdersActivity extends AppCompatActivity {
private RecyclerView recyclerView ;
private FireBaseVar fireBaseVar = new FireBaseVar();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        collectedViewsHere();

        getDataToRecyclerView();

    }

    public void collectedViewsHere(){
        recyclerView = (RecyclerView)findViewById(R.id.RecyOrdersList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

    public void getDataToRecyclerView(){
        final ArrayList<ModelOrders> arrayList = new ArrayList<>();
        fireBaseVar.mOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if (dataSnapshot.getChildrenCount() != 0){
                   arrayList.clear();
                   for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                       String key = snapshot.getRef().getKey();
                       String name = snapshot.child("name").getValue().toString();
                       String mobile1 = snapshot.child("mobile1").getValue().toString();
                       String mobile2 = snapshot.child("mobile2").getValue().toString();
                       String adress = snapshot.child("adress").getValue().toString();
                       String notes = snapshot.child("notes").getValue().toString();
                       String total = snapshot.child("total").getValue().toString();
                       String read_state = snapshot.child("read_state").getValue().toString();
                       if(name != null){
                           ModelOrders modelOrders = new ModelOrders(key,name,mobile1,mobile2,adress,notes,total,read_state);
                           arrayList.add(modelOrders);
                       }
                   }
               }

                AdapterOrdersView adapterOrders = new AdapterOrdersView(arrayList,OrdersActivity.this);
                recyclerView.setAdapter(adapterOrders);
                adapterOrders.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(OrdersActivity.this,MainActivity.class));
    }
}
