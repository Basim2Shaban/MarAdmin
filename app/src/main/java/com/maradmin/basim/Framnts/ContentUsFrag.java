package com.maradmin.basim.Framnts;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.maradmin.basim.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContentUsFrag extends Fragment {


    public ContentUsFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_content_us, container, false);
    }

}
