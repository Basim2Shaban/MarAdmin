package com.maradmin.basim.firebase_side;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.maradmin.basim.Adapters.BranchAdapter;
import com.maradmin.basim.Adapters.MainMenuAdapter;
import com.maradmin.basim.models.MainMenuModel;
import com.maradmin.basim.models.MenuDataModels;

import java.util.ArrayList;

public class HandleWithData {
String arabicKey ;
String englishKey ;

    // this method is going to get all data from fire base and put it in list and there is a part to handle with data in spinner
    public void getDataTOSpinner(final DatabaseReference var  , final Context context , final Spinner spinner , final String typ , final ProgressDialog mpro){
        final ArrayList<MenuDataModels> arrayList = new ArrayList<>();

        var.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    String key = dataSnapshot1.getKey();
                    String image = dataSnapshot1.child("image").getValue().toString();
                    String title = dataSnapshot1.child("title").getValue().toString();
                    MenuDataModels models = new MenuDataModels(key,image,title);
                    arrayList.add(models);
                }

            //start handle with spinner from here
                ArrayList<String> ar_string = new ArrayList<>();
                ar_string.clear();

                ar_string.add("حدد عنصر من القائمه");

                for (int i = 0 ; i < arrayList.size() ; i++ ){
                    String title = arrayList.get(i).getTitle();
                    ar_string.add(title);
                }
                ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, ar_string);
                spinner.setAdapter(arrayAdapter);
                mpro.dismiss();

                getKeySpnOnSelectedChanged(spinner , arrayList , typ);

            //To here ;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // this method , it and the method in under i use them to get key in another activity ;
    public String getSpinnerArKey(){
        return arabicKey;
    }

    public String getSpinnerEnKey(){
        return englishKey;
    }

    // here i used the methods in above to get key on item selected is change
    public void getKeySpnOnSelectedChanged(Spinner spinner, final ArrayList<MenuDataModels> arrayList, final String typ){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0){
                    if (typ == "Ar"){
                        arabicKey = arrayList.get(position -1).getKey();
                    }else{
                        englishKey = arrayList.get(position -1).getKey();
                    }
                }else{
                    arabicKey = null ;
                    englishKey = null ;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }



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
