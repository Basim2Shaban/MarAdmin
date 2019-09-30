package com.maradmin.basim.Adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.maradmin.basim.Framnts.BranchFragment;
import com.maradmin.basim.Framnts.ProductsFragment;
import com.maradmin.basim.R;
import com.maradmin.basim.firebase_side.FireBaseVar;
import com.maradmin.basim.models.MainMenuModel;

import java.util.ArrayList;

public class BranchAdapter  extends RecyclerView.Adapter<BranchAdapter.VHolder> {
    private ArrayList<MainMenuModel> arrayList ;
    private Context mContext ;
    private FireBaseVar fireBaseVar = new FireBaseVar();


    public BranchAdapter(ArrayList<MainMenuModel> arrayList, Context mContext) {
        this.arrayList = arrayList;
        this.mContext = mContext;
    }



    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view =  LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_view_pager, viewGroup, false);

        return new VHolder(view);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull final VHolder vHolder, final int i) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            vHolder.imageView.setClipToOutline(true);
        }
        Glide.with(mContext).load(arrayList.get(i).getImage()).into(vHolder.imageView);
        vHolder.textViewTitle.setText(arrayList.get(i).getTitle());


        onDeleteCliced(vHolder.deleteBranch,i);





        vHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) vHolder.itemView.getContext();
                Fragment myFragment = new ProductsFragment();
                Bundle bundle=new Bundle();
                bundle.putString("key", arrayList.get(i).getKey()); //key and value
                myFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, myFragment)
                        .addToBackStack(null).commit();
            }
        });
    }



    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class VHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle ;
        private ImageView imageView , deleteBranch;

        @SuppressLint("NewApi")
        public VHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle =(TextView)itemView.findViewById(R.id.text_viewPager);
            imageView = (ImageView)itemView.findViewById(R.id.img_viewPager);
            deleteBranch = (ImageView)itemView.findViewById(R.id.icon_deleteBanch);
        }
    }



    public void onDeleteCliced(ImageView imgDelete , final int i){
        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("هل حقا تريد حذف القائمة ؟");
                builder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fireBaseVar.mMainMenuEn.child(BranchFragment.key).child("branch").child(arrayList.get(i).getKey())
                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    fireBaseVar.mMainMenuAr.child(BranchFragment.key).child("branch").child(arrayList.get(i).getKey())
                                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(mContext, "تم حذف العنصر", Toast.LENGTH_SHORT).show();
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
                dialog.show();
            }
        });
    }
}