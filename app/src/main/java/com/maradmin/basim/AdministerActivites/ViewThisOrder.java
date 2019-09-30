package com.maradmin.basim.AdministerActivites;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.maradmin.basim.AdministerAdapters.AdapterViewThisOrd;
import com.maradmin.basim.R;
import com.maradmin.basim.firebase_side.FireBaseVar;
import com.maradmin.basim.models.ProductsModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewThisOrder extends AppCompatActivity {
private FireBaseVar fireBaseVar = new FireBaseVar();
private TextView textViewInvoice , textViewAdress , textViewNotes ;
private RecyclerView recyclerView ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_this_order);

        collectViewsHere();

        Intent intent = getIntent() ;
        String key = intent.getStringExtra("key");
        String total = intent.getStringExtra("total");
        String adress = intent.getStringExtra("adress");
        String notes = intent.getStringExtra("notes");


        setDataToViews(total,adress,notes);

        setDataToRecycler(key);

        setSeen(key);


    }


    public void setSeen(final String key){
        final HashMap hashMap = new HashMap<>();
        hashMap.put("read_state","yes");
        fireBaseVar.mOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(key)){

                    fireBaseVar.mOrder.child(key).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                }
                else {
                    Toast.makeText(ViewThisOrder.this, "لقد تم حذف هذا الطلب !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void collectViewsHere(){
        textViewAdress = (TextView)findViewById(R.id.txtViewAdress);
        textViewInvoice = (TextView)findViewById(R.id.txtViewInvoice);
        textViewNotes = (TextView)findViewById(R.id.txtViewNotes);
        recyclerView = (RecyclerView) findViewById(R.id.rec_ViewThisOrder);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }


    @SuppressLint("SetTextI18n")
    public void setDataToViews(String  Total , String Adress , String Notes){
        textViewInvoice.setText(Total+ " " + "ج.م");
        textViewAdress.setText(Adress);
        textViewNotes.setText(Notes);
    }


    public void setDataToRecycler(String key){
        fireBaseVar.mOrder.child(key).child("list").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<ProductsModel> arrayList = new ArrayList<>();
                arrayList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ProductsModel productsModel = snapshot.getValue(ProductsModel.class);
                    arrayList.add(productsModel);
                }

                AdapterViewThisOrd adapterViewThisOrd = new AdapterViewThisOrd(arrayList,ViewThisOrder.this);
                recyclerView.setAdapter(adapterViewThisOrd);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
