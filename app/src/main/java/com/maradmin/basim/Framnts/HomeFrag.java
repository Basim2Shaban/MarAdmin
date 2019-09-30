package com.maradmin.basim.Framnts;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.maradmin.basim.MyApplication;
import com.maradmin.basim.R;
import com.maradmin.basim.firebase_side.FireBaseVar;
import com.maradmin.basim.firebase_side.HandleWithData;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFrag extends Fragment {
private HandleWithData handleWithData = new HandleWithData();
private FireBaseVar firebaseVar = new FireBaseVar();
private RecyclerView recyclerView ;
private MyApplication myApplication = new MyApplication();
private LinearLayoutManager layoutManager;


    public HomeFrag() {
        // Required empty public constructor
    }


    @SuppressLint({"NewApi", "WrongConstant"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        collectViewsHere(view);




        if (myApplication.direc == 0){
            handleWithData.getDataToMainMenu(firebaseVar.mMainMenuEn,getActivity(),recyclerView);
        }else {
            handleWithData.getDataToMainMenu(firebaseVar.mMainMenuAr,getActivity(),recyclerView);
        }

        return view ;
    }


    public void collectViewsHere(View v){
        recyclerView = (RecyclerView)v.findViewById(R.id.recycler_main_menu);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

    }

}
