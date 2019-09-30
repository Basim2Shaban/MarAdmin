package com.maradmin.basim;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.maradmin.basim.AdministerActivites.DeleteFromAds;
import com.maradmin.basim.firebase_side.FireBaseVar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

public class AddNewAd extends AppCompatActivity {
    private Button btn_AddData , btn_ViewAds ;
    private EditText editNum , editTitle ;
    private ImageView imageView ;
    private Uri uri_Image , uri_ReadyToSend ;
    private byte [] byteBitmapArray ;
    private ProgressBar progressBar ;
    private FireBaseVar fireBaseVar = new FireBaseVar();
    private String title ;
    private int number ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_ad);

        collectViewsHere();
        openGallery();
        onClickSend();


        btn_ViewAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddNewAd.this,DeleteFromAds.class));
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

            Calendar cal = Calendar.getInstance();
            long i = cal.getTimeInMillis();


            imageView.setImageResource(0);
            progressBar.setVisibility(View.VISIBLE);
            final StorageReference mSRef  = fireBaseVar.mStorageRef.child("pictures/" + "myPic"+i + ".jpg");
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
                                    Glide.with(AddNewAd.this).load(uri_ReadyToSend).into(imageView);
                                }else {
                                    imageView.setImageResource(R.drawable.add);
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
        number = Integer.parseInt(editNum.getText().toString().trim());
        title = editTitle.getText().toString().trim();
    }

    public void sendData(final ProgressDialog progressDialog){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("title",title);
        hashMap.put("image", String.valueOf(uri_ReadyToSend));

        fireBaseVar.mPager.child(String.valueOf(number)).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @SuppressLint("NewApi")
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    editNum.setText("");
                    editTitle.setText("");
                    number = 0 ;
                    title = null ;
                    uri_ReadyToSend = null ;
                    progressBar.setVisibility(View.INVISIBLE);
                    imageView.setImageResource(R.drawable.add);
                    Toast.makeText(AddNewAd.this, "تم اضافة الاعلان ", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }else {
                    Toast.makeText(AddNewAd.this, "حدث حطا اثناء اضافه البيانات", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }
        });
    }

    public void onClickSend(){
        btn_AddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromEdits();
                if (number!= 0 &&number <= 6 && !title.isEmpty() && uri_ReadyToSend != null){
                    ProgressDialog progressDialog = new ProgressDialog(AddNewAd.this);
                    progressDialog.setTitle("برجاء الانتظار");
                    progressDialog.setMessage("جاري رفع البيانات الي السيرفر");
                    progressDialog.show();
                    sendData(progressDialog);
                }else {
                    Toast.makeText(AddNewAd.this, "متسبش خانه فاضيه , ومتكتبش 0 في خانه الرقم ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void openGallery(){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromEdits();
                if (number != 0 && number <= 6){
                    Intent intentToGallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intentToGallery, 10);
                }else{
                    Toast.makeText(AddNewAd.this, "يرجي اضافه الرقم اولا !1", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void collectViewsHere(){
        editNum = (EditText)findViewById(R.id.AdEditNum);
        editTitle = (EditText)findViewById(R.id.AdEditTitle);
        imageView = (ImageView)findViewById(R.id.adImage);
        btn_AddData = (Button)findViewById(R.id.bsendTheAdd);
        btn_ViewAds = (Button)findViewById(R.id.btn_viewAds);
        progressBar =(ProgressBar)findViewById(R.id.progressBarAd);
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(AddNewAd.this,MainActivity.class));
    }
}
