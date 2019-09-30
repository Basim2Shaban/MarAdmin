package com.maradmin.basim.AdministerAdapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.maradmin.basim.AdministerActivites.DeleteFromAds;
import com.maradmin.basim.AdministerModels.ModelAds;
import com.maradmin.basim.R;
import com.maradmin.basim.firebase_side.FireBaseVar;

import java.util.ArrayList;

public class AdsAdapter extends RecyclerView.Adapter<AdsAdapter.HolderAds> {
    ArrayList<ModelAds> arrayList ;
    Context context ;
    private FireBaseVar fireBaseVar = new FireBaseVar();

    public AdsAdapter(ArrayList<ModelAds> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public HolderAds onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_remove_ads,viewGroup,false);
        return new HolderAds(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderAds holderAds, final int i) {
        holderAds.txtTitle.setText(arrayList.get(i).getTitle());
        Glide.with(context).load(arrayList.get(i).getImage()).into(holderAds.imgAds);


        holderAds.imgDeleteAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteClicked(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class HolderAds extends RecyclerView.ViewHolder{
        TextView txtTitle ;
        ImageView imgAds , imgDeleteAds;

        public HolderAds(@NonNull View itemView) {
            super(itemView);

            txtTitle = (TextView)itemView.findViewById(R.id.textAdsTitle);
            imgAds = (ImageView)itemView.findViewById(R.id.imge_Ads);
            imgDeleteAds = (ImageView)itemView.findViewById(R.id.imge_delete);
        }
    }


    public void onDeleteClicked(final int i){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("هل تريد حذف هذا الاعلان ؟");
        builder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fireBaseVar.mPager.child(arrayList.get(i).getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(context, "تم حذف الاعلان", Toast.LENGTH_SHORT).show();
                            context.startActivity(new Intent(context,DeleteFromAds.class));
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
}
