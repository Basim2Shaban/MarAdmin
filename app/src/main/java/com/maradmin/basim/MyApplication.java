package com.maradmin.basim;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import com.maradmin.basim.Framnts.AboutAppFrag;
import com.maradmin.basim.Framnts.AboutUsFrag;
import com.maradmin.basim.Framnts.HomeFrag;
import com.maradmin.basim.reside_menu.ResideMenu;
import com.maradmin.basim.reside_menu.ResideMenuItem;

import java.util.Locale;


public class MyApplication extends AppCompatActivity implements View.OnClickListener{
    private static ResideMenu resideMenu;
    private Context mContext;
    private ResideMenuItem itemHome , itemRateApp;
    private ResideMenuItem item_language , itemAboutApp,itemContentUs , itemAboutUs;
    private static boolean lang = true ;
    private static String language  , languagenow;
    public static int direc ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_application);

        mContext = this;

        setUpMenu();

// Elcode Alle Bekhalek teftah elfragment ala tool
        if( savedInstanceState == null ){
            changeFragment(new HomeFrag());
        }

    }

    @SuppressLint("NewApi")
    private void setUpMenu() {

        // attach to current activity;
        resideMenu = new ResideMenu(this);

        resideMenu.setBackgroundColor(getResources().getColor(R.color.bg_reside));
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        resideMenu.setScaleValue(0.6f);

        // create menu items;
        createMenuItemsAndClicks();



        if (lang ){
            direc = ResideMenu.DIRECTION_RIGHT;
            resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);
        }else{
            direc = ResideMenu.DIRECTION_LEFT;
            resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
        }
        putItemsInResideAndEdits(direc);



        // You can disable a direction by setting ->
        // resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

        findViewById(R.id.icon_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (direc == 0){
                    resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);

                }else {
                    resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
                }
            }
        });

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {
        if (view == itemHome){
            onItemClick(itemHome);
            changeFragment(new HomeFrag());
        }
        else if (view == itemAboutUs){
            onItemClick(itemAboutUs);
            changeFragment(new AboutUsFrag());
        }
        else if (view == itemRateApp){

        }
        else if (view == itemAboutApp){
            onItemClick(itemAboutApp);
            changeFragment(new AboutAppFrag());
        }
        else if (view == itemContentUs){
            onItemClick(itemContentUs);
        }

        else if (view == item_language){
            if (lang){
                lang = false ;
                languagenow = "en";
                changeLanguage(this,languagenow);
            }else {
                lang = true ;
                languagenow = "ar";
                changeLanguage(this,languagenow);
            }

        }

        resideMenu.closeMenu();
    }

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {

        }

        @Override
        public void closeMenu() {

        }
    };

    public void changeFragment(Fragment targetFragment){
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    // What good method is to access resideMenu？
    public static ResideMenu getResideMenu(){
        return resideMenu;
    }


    public void createMenuItemsAndClicks(){
        itemHome     = new ResideMenuItem(this, 0,getString(R.string.Home));
        itemAboutUs  = new ResideMenuItem(this, 0,getString(R.string.AboutUs));
        itemRateApp = new ResideMenuItem(this, 0,getString(R.string.RateApp));
        itemAboutApp = new ResideMenuItem(this, 0,getString(R.string.AboutApp));
        itemContentUs = new ResideMenuItem(this, 0,getString(R.string.ConnUs));


        itemHome.setOnClickListener(this);
        itemAboutUs.setOnClickListener(this);
        itemRateApp.setOnClickListener(this);
        itemAboutApp.setOnClickListener(this);
        itemContentUs.setOnClickListener(this);

    }



    public void putItemsInResideAndEdits(int direction){
        resideMenu.addMenuItem(itemHome, direction);
        resideMenu.addMenuItem(itemAboutUs, direction);
        resideMenu.addMenuItem(itemRateApp, direction);
        resideMenu.addMenuItem(itemAboutApp, direction);
        resideMenu.addMenuItem(itemContentUs, direction);


        itemHome.setPadding(0,50,0,0);
        itemHome.setGravity(Gravity.CENTER);
        //  itemAboutUs.setPadding(0,-15,0,0);
        //itemRateApp.setPadding( 0,-15,0,0);
        // itemAboutApp.setPadding( 0,-15,0,0);
        // itemContentUs.setPadding(0,-15,0,0);
        itemContentUs.setPadding(0,0,0,150);
    }


    @SuppressLint("ClickableViewAccessibility")
    public void onItemClick(ResideMenuItem item){
        itemHome.setTextColor(getResources().getColor(R.color.white));
        itemRateApp.setTextColor(getResources().getColor(R.color.white));
        itemAboutUs.setTextColor(getResources().getColor(R.color.white));
        itemAboutApp.setTextColor(getResources().getColor(R.color.white));
        itemContentUs.setTextColor(getResources().getColor(R.color.white));

        item.setTextColor(getResources().getColor(R.color.black));


    }

    @SuppressLint("NewApi")
    public void changeLanguage(Context ctx, String lang){
        Locale mLocale = new Locale(lang);
        Locale.setDefault(mLocale);
        Configuration config = ctx.getResources().getConfiguration();

        config.locale = mLocale;
        config.setLayoutDirection(config.locale);
        ctx.getResources().updateConfiguration(config, null);
        changeFragment(new HomeFrag());
        recreate();
    }




}