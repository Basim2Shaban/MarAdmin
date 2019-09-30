package com.maradmin.basim.firebase_side;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.maradmin.basim.Adapters.ProductsAdapter;
import com.maradmin.basim.models.ProductsModel;

import java.util.ArrayList;

public class ProductsData {


// i use this method to get all data to products who task from me & there is a text in parameter i use it to send it to
    // adapter bc the handling is there ;
    public void getProducts(final DatabaseReference var  , final Context context , final RecyclerView recyclerView ){
        final ArrayList<ProductsModel> arrayList = new ArrayList<>();
        arrayList.clear();

        var.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    String key = dataSnapshot1.getKey();
                    String image = dataSnapshot1.child("image").getValue().toString();
                    String arabic_name = dataSnapshot1.child("arabic_name").getValue().toString();
                    String english_name = dataSnapshot1.child("english_name").getValue().toString();
                    String price = dataSnapshot1.child("price").getValue().toString();
                    String unit = dataSnapshot1.child("unit").getValue().toString();
                    ProductsModel models = new ProductsModel(key , image , arabic_name , english_name,price,unit);
                    arrayList.add(models);
                }

               ProductsAdapter productsAdapter = new ProductsAdapter( context , arrayList  );
               recyclerView.setAdapter(productsAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
