package com.maradmin.basim.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.maradmin.basim.Framnts.ProductsFragment;
import com.maradmin.basim.MyApplication;
import com.maradmin.basim.R;
import com.maradmin.basim.firebase_side.FireBaseVar;
import com.maradmin.basim.models.ProductsModel;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHold> {
    Context context ;
    ArrayList<ProductsModel> arrayList ;
    private MyApplication mainActivity = new MyApplication();
    public static ArrayList<ProductsModel> basket_list = new ArrayList();
    private static ArrayList<ProductsModel> getBasketList = new ArrayList();
    public static double unitWieght ;
    private FireBaseVar fireBaseVar = new FireBaseVar();


    public ProductsAdapter(Context context, ArrayList<ProductsModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHold onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_products , viewGroup , false);
        return new ViewHold(view);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull final ViewHold viewHold, final int position) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            viewHold.imageViewPro.setClipToOutline(true);
        }

        // this for check from the language before set the data
        if (mainActivity.direc == 0){
            viewHold.textViewName.setText(arrayList.get(position).getEnglish_name());
        }else{
            viewHold.textViewName.setText(arrayList.get(position).getArabic_name());
        }



        Glide.with(context).load(arrayList.get(position).getImage()).into(viewHold.imageViewPro);
        viewHold.textViewPrice.setText(arrayList.get(position).getPrice()+ " " + "جنيه");


        onDeleteClicked(viewHold.btnDeleteProduct,position);



        // to get unit weight from data and convert it to value i can use it in the basket accounts
        switch (arrayList.get(position).getUnit()){
            case "100":
                unitWieght = 0.1 ;
                break;
            case "500":
                unitWieght = 0.5 ;
                break;
            case "1000":
                unitWieght = 1 ;
                break;
        }


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }



    class ViewHold extends RecyclerView.ViewHolder{
       public TextView textViewName , textViewPrice  ;
       public ImageView imageViewPro ;
       public Button btnDeleteProduct ;


        private ViewHold(@NonNull View itemView) {
            super(itemView);
            textViewName = (TextView)itemView.findViewById(R.id.produc_name);
            textViewPrice = (TextView)itemView.findViewById(R.id.produc_price);
           // textViewUnit = (TextView)itemView.findViewById(R.id.produc_howMuch_txt);
            imageViewPro = (ImageView) itemView.findViewById(R.id.produc_image);
            btnDeleteProduct = (Button) itemView.findViewById(R.id.produc_removeBtn);
        }
    }


    public void onDeleteClicked(Button button , final int i){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("هل تريد ان تحذف هذا العنصر ؟");
                builder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fireBaseVar.mMainMenuEn.child(ProductsFragment.key_ofMenu).child("branch").child(ProductsFragment.key_ofBranch)
                                .child("products").child(arrayList.get(i).getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){

                                    fireBaseVar.mMainMenuAr.child(ProductsFragment.key_ofMenu).child("branch").child(ProductsFragment.key_ofBranch)
                                            .child("products").child(arrayList.get(i).getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(context, "تم حذف العنصر", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }
                            }
                        });
                    }
                });


                builder.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });


                Dialog dialog = builder.create();
                dialog.setCancelable(true);
                dialog.show();
            }
        });
    }


}
