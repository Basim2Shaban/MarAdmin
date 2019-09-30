package com.maradmin.basim;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.maradmin.basim.R;

public class MainActivity extends AppCompatActivity {
    private FrameLayout btn_Orders , btn_MainMenu , btn_ForkedMenu , btn_AddNewProduct ,
            btn_AddOffers  , btnNotification;
    private Intent intentToMainMenuInputs , intentToFrokedMenuInputs , intentToAddNewProduct,
            intentToAddNewOffer,intentToActivityOrders , intentToNotification;
    private FrameLayout btnAddNewAdd ;
    private FirebaseAuth mAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            signIn();
        }else{
            Log.e("Email is ",currentUser.getEmail());
        }



        collectViewshere();

        intentToActivityOrders = new Intent(this,OrdersActivity.class);
        startIntentTo(intentToActivityOrders,btn_Orders);


        intentToMainMenuInputs = new Intent(this,MainMenuInputs.class);
        startIntentTo(intentToMainMenuInputs,btn_MainMenu);


        intentToFrokedMenuInputs = new Intent(this,FrokedMenuInputs.class);
        startIntentTo(intentToFrokedMenuInputs,btn_ForkedMenu);


        intentToAddNewProduct = new Intent(this,MyApplication.class);
        startIntentTo(intentToAddNewProduct,btn_AddNewProduct);


        intentToAddNewOffer = new Intent(this,AddNewOffer.class);
        startIntentTo(intentToAddNewOffer,btn_AddOffers);

        intentToNotification = new Intent(this,PushNotification.class);
        startIntentTo(intentToNotification,btnNotification);


        btnAddNewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AddNewAd.class));
            }
        });
    }


    @SuppressLint("CutPasteId")
    public void collectViewshere(){
        btn_Orders = (FrameLayout)findViewById(R.id.btn_orders);
        btn_AddNewProduct = (FrameLayout)findViewById(R.id.btn_add_new_product);
        btn_AddOffers = (FrameLayout)findViewById(R.id.btn_add_offers);
        btn_ForkedMenu = (FrameLayout)findViewById(R.id.btn_input_main_froked);
        btn_MainMenu = (FrameLayout)findViewById(R.id.btn_input_main_menu);
        btnNotification = (FrameLayout)findViewById(R.id.btn_notification);
        btnAddNewAdd = (FrameLayout) findViewById(R.id.btnAddNewAd);
    }

    public void startIntentTo(final Intent intent , FrameLayout btn ){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });

    }


    public void signIn(){
        mAuth.signInWithEmailAndPassword("basm0590@gmail.com","Asd1996")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.e("Login State",": done");
                        }else{
                            Log.e("Login State",": sorry");
                        }
                    }
                });
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }///
}
