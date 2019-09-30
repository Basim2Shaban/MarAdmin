package com.maradmin.basim.Framnts;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.maradmin.basim.MyApplication;
import com.maradmin.basim.R;
import com.maradmin.basim.firebase_side.FireBaseVar;
import com.maradmin.basim.firebase_side.HandleWithData;

/**
 * A simple {@link Fragment} subclass.
 */
public class BranchFragment extends Fragment {
    private RecyclerView recyclerView_branch ;
    private LayoutInflater mLayoutInflater;
    private HandleWithData handleWithData = new HandleWithData();
    private MyApplication myApplication = new MyApplication();
    private FireBaseVar firebaseVar = new FireBaseVar();
    public static String key ;


    public BranchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_branch, container, false);

        key = getArguments().getString("key"); //fetching value by key

        collectViewsHere(view);



        if (myApplication.direc == 0){
            handleWithData.getDataToBranch(firebaseVar.mMainMenuEn.child(key).child("branch"),getActivity(),recyclerView_branch);
        }else{
            handleWithData.getDataToBranch(firebaseVar.mMainMenuAr.child(key).child("branch"),getActivity(),recyclerView_branch);
        }



        return view ;
    }



    public void collectViewsHere(View view){
        recyclerView_branch = (RecyclerView) view.findViewById(R.id.recycler_branch);
        recyclerView_branch.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView_branch.setHasFixedSize(true);

    }

}
