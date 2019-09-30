package com.maradmin.basim.Framnts;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.maradmin.basim.AddProduct;
import com.maradmin.basim.MyApplication;
import com.maradmin.basim.R;
import com.maradmin.basim.firebase_side.FireBaseVar;
import com.maradmin.basim.firebase_side.ProductsData;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductsFragment extends Fragment {
    public static String key_ofBranch , key_ofMenu;
    private Button btn_AddNewPro ;
    private BranchFragment branchFragment = new BranchFragment();
    private MyApplication myApplication = new MyApplication();
    private ProductsData productsData = new ProductsData();
    private FireBaseVar firebaseVar = new FireBaseVar();
    private RecyclerView recyclerView ;



    public ProductsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        collectViewsHere(view);


        key_ofBranch = getArguments().getString("key"); //fetching value by key
        key_ofMenu = branchFragment.key ;


        if (myApplication.direc == 0){
            productsData.getProducts(firebaseVar.mMainMenuEn.child(key_ofMenu).child("branch").child(key_ofBranch).child("products"),view.getContext(),recyclerView );
        }else{
            productsData.getProducts(firebaseVar.mMainMenuAr.child(key_ofMenu).child("branch").child(key_ofBranch).child("products"),view.getContext(),recyclerView );

        }

        btn_AddNewPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),AddProduct.class);
                intent.putExtra("menu",key_ofMenu);
                intent.putExtra("branch",key_ofBranch);
                startActivity(intent);
            }
        });





        return view ;
    }



    public void collectViewsHere(View view){
        btn_AddNewPro = (Button)view.findViewById(R.id.btn_add_newProduct);

        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerProducts);
        GridLayoutManager mLayoutManager = new GridLayoutManager(view.getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
    }





}
