package com.maradmin.basim.AdministerAdapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.maradmin.basim.AdministerActivites.ViewThisOrder;
import com.maradmin.basim.AdministerModels.ModelOrders;
import com.maradmin.basim.OrdersActivity;
import com.maradmin.basim.R;
import com.maradmin.basim.firebase_side.FireBaseVar;

import java.util.ArrayList;

public class AdapterOrdersView extends RecyclerView.Adapter<AdapterOrdersView.Holder> {
    ArrayList<ModelOrders> arrayList ;
    Context context ;
    private FireBaseVar fireBaseVar = new FireBaseVar();


    public AdapterOrdersView(ArrayList<ModelOrders> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_new_order,viewGroup,false);

        return new Holder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {

        holder.setIsRecyclable(false);



        holder.txtName.setText(arrayList.get(i).getName());
        holder.txtMobileO1.setText(arrayList.get(i).getMobile1());
        holder.txtMobileO2.setText(arrayList.get(i).getMobile2());
        holder.txtInvoicePrice.setText(arrayList.get(i).getTotal()+" " + "ج.م");


        String adress = arrayList.get(i).getAdress();
        String notes = arrayList.get(i).getNotes();
        String total = arrayList.get(i).getTotal();
        String seen = arrayList.get(i).getRead_state();
        String key = arrayList.get(i).getKey();


        if (seen.equals("yes")){
            holder.btnReadedOrder.setTextColor(Color.parseColor("#FF7B7B7B"));
            holder.btnReadedOrder.setTextSize(18);
            holder.btnReadedOrder.setText("جار التنفيذ");
            holder.btnReadedOrder.setEnabled(false);
        }

        onClickedDelete(holder.btnDeleteOrder , key);
        onClickedMakeItReaded(holder.btnReadedOrder);
        onClickedViewListOrdered(holder.btnViewListOrderd,key,adress,notes , total);




    }


    @Override
    public int getItemViewType(int position) {
        return position ;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        TextView txtName , txtMobileO1 , txtMobileO2 , txtInvoicePrice ;
        Button btnReadedOrder , btnViewListOrderd ;
        ImageView btnDeleteOrder ;

        public Holder(@NonNull View itemView) {
            super(itemView);
            txtName = (TextView)itemView.findViewById(R.id.userNameTxt);
            txtMobileO1 = (TextView)itemView.findViewById(R.id.orderMob1);
            txtMobileO2 = (TextView)itemView.findViewById(R.id.orderMob2);
            txtInvoicePrice = (TextView)itemView.findViewById(R.id.invoicePrice);
            btnDeleteOrder = (ImageView)itemView.findViewById(R.id.deleteOrderBtn);
            btnReadedOrder = (Button)itemView.findViewById(R.id.selectedDoneBtn);
            btnViewListOrderd = (Button)itemView.findViewById(R.id.viewOrderdListBtn);
        }
    }




    public void onClickedDelete(ImageView button , final String key){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setMessage("هل تريد حذف هذا الطلب ؟");
                dialog.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                fireBaseVar.mOrder.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(context, "تم حذف الطلب", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                             context.startActivity(new Intent(context,OrdersActivity.class));
                        }else{
                            Toast.makeText(context, "نواجه مشكله في حذف الطلب", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                    }
                });

                dialog.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });


                AlertDialog alertDialog = dialog.create();
                alertDialog.setCancelable(true);
                alertDialog.show();
            }
        });
    }


    public void onClickedMakeItReaded(Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "يرجي فتح الطلب وسوف يتم تحديث الوضع الي جار التنفيذ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onClickedViewListOrdered(Button button , final String key , final String adress , final String notes , final String tot){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ViewThisOrder.class);
                intent.putExtra("key",key);
                intent.putExtra("adress",adress);
                intent.putExtra("notes",notes);
                intent.putExtra("total",tot);
                context.startActivity(intent);
            }
        });
    }
}
