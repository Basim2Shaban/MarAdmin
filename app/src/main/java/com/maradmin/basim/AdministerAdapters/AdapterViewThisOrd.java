package com.maradmin.basim.AdministerAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.maradmin.basim.R;
import com.maradmin.basim.models.ProductsModel;

import java.util.ArrayList;

public class AdapterViewThisOrd extends RecyclerView.Adapter<AdapterViewThisOrd.HolderThis>  {
ArrayList<ProductsModel> arrayList ;
Context context ;

    public AdapterViewThisOrd(ArrayList<ProductsModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }



    @NonNull
    @Override
    public HolderThis onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view_this_order,viewGroup,false);

        return new HolderThis(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HolderThis holderThis, int i) {
        holderThis.setIsRecyclable(false);

        holderThis.txtWeight.setText(arrayList.get(i).getUnit()+ " "+ "كجم");
        holderThis.txtName.setText(arrayList.get(i).getArabic_name());
        holderThis.txtKgmPrice.setText(arrayList.get(i).getPrice() + " "+"ج.م");
        float value = Float.parseFloat(arrayList.get(i).getUnit()) * Float.parseFloat(arrayList.get(i).getPrice()) ;
        holderThis.txtTotalItem.setText(value+ " " + "ج.م");


    }


    @Override
    public int getItemViewType(int position) {
        return position ;
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class HolderThis extends RecyclerView.ViewHolder{
        TextView txtWeight , txtName , txtKgmPrice , txtTotalItem ;

        public HolderThis(@NonNull View itemView) {
            super(itemView);

            txtWeight = (TextView)itemView.findViewById(R.id.txt_wight);
            txtName = (TextView)itemView.findViewById(R.id.txt_oName);
            txtKgmPrice = (TextView)itemView.findViewById(R.id.txtPrice1Kgm);
            txtTotalItem = (TextView)itemView.findViewById(R.id.txtTotalItem);
        }
    }
}
