package com.maradmin.basim;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.maradmin.basim.firebase_side.FireBaseVar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class AddNewOffer extends AppCompatActivity {
    private FireBaseVar fireBaseVar = new FireBaseVar();
    private EditText edtArabicName , edtEnglishName ;
    private ImageView imgOffer ;
    private Button btnAddOfferData ;
    private ProgressBar progressBar ;
    private Uri uri_Image , uri_ReadyToSend ;
    private String arabicName , englishName ;
    private byte [] byteBitmapArray ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_offer);

        collectViewsHere();


        openGallery();


        btnAddOfferData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromEdits();
                ProgressDialog progressDialog = new ProgressDialog(AddNewOffer.this);
                if (!arabicName.isEmpty() &&! englishName.isEmpty() && uri_ReadyToSend != null){
                    progressDialog.setCancelable(false);
                    progressDialog.setTitle("برجاء الانتظار ..");
                    progressDialog.setMessage("جار اضافة العرض الجديد");
                    progressDialog.show();
                    onClickSendData(progressDialog);
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(AddNewOffer.this, "يرجي عدم ترك اي خانة فارغة", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



    public void collectViewsHere(){
        edtArabicName = (EditText)findViewById(R.id.edit_ArabicName_Offer);
        edtEnglishName = (EditText)findViewById(R.id.edit_EnglishName_Offer);
        imgOffer = (ImageView)findViewById(R.id.AddImageOffer);
        btnAddOfferData = (Button)findViewById(R.id.BtnAddDataOffer);
        progressBar =(ProgressBar)findViewById(R.id.progressBarOffer);
    }

    // is name it explain to you what i do by use it
    public void openGallery(){
        imgOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToGallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGallery, 10);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==10 && resultCode==RESULT_OK){
            try {
                uri_Image = data.getData();


                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri_Image);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                byteBitmapArray = out.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }



            imgOffer.setImageResource(0);
            progressBar.setVisibility(View.VISIBLE);
            final StorageReference mSRef  = fireBaseVar.mStorageRef.child("offers/" + "offer_pic"+ ".jpg");
            mSRef.putBytes(byteBitmapArray).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()){
                        mSRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @SuppressLint("NewApi")
                            @Override
                            public void onSuccess(Uri uri) {
                                uri_ReadyToSend = uri ;
                                if (uri_ReadyToSend != null){
                                    Glide.with(AddNewOffer.this).load(uri_ReadyToSend).into(imgOffer);
                                }else {
                                    imgOffer.setImageResource(R.drawable.add);
                                    progressBar.setVisibility(View.INVISIBLE);
                                }

                            }
                        });
                    }
                }
            });


        }
    }


    public void getDataFromEdits(){
        arabicName = edtArabicName.getText().toString().trim();
        englishName = edtEnglishName.getText().toString().trim();
    }

    public void onClickSendData(final ProgressDialog dialog){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("arabic_name",arabicName);
        hashMap.put("english_name",englishName);
        hashMap.put("image", String.valueOf(uri_ReadyToSend));

        fireBaseVar.mOffers.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @SuppressLint("NewApi")
            @Override
            public void onComplete(@NonNull Task<Void> task) {
               if (task.isSuccessful()){
                   edtArabicName.setText("");
                   edtEnglishName.setText("");
                   progressBar.setVisibility(View.INVISIBLE);
                   imgOffer.setImageResource(R.drawable.add);
                   arabicName = null ;
                   englishName = null ;
                   uri_ReadyToSend = null ;
                   Toast.makeText(AddNewOffer.this, "تم اضافة العرض", Toast.LENGTH_LONG).show();
                   dialog.dismiss();
               }else {
                   Toast.makeText(AddNewOffer.this, "حدث خطا , يرجي المحاولة لاحقا", Toast.LENGTH_LONG).show();
                   dialog.dismiss();
               }
            }
        });

    }

}
