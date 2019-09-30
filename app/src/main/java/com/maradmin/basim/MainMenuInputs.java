package com.maradmin.basim;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

public class MainMenuInputs extends AppCompatActivity {
    private EditText edt_ArTitle , edt_EnTitle ;
    private ImageView img_addTitlePic ;
    private Button btn_AddData ;
    private ProgressBar progressBar ;
    private String arabic_title , english_title ;
    private FireBaseVar fireBaseVar = new FireBaseVar();
    private Uri  uri_Image , uri_ReadyToSend ;
    private byte [] byteBitmapArray ;
    private ProgressDialog progressDialog ;
    public static long randomNum ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_inputs);

        collectViewshere();
        openGallery();


        btn_AddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromEdits();
                sendData();
            }
        });

    }



    public void collectViewshere(){
        edt_ArTitle = (EditText)findViewById(R.id.edt_arabic_msin_menu);
        edt_EnTitle = (EditText)findViewById(R.id.edt_english_main_menu);
        img_addTitlePic = (ImageView)findViewById(R.id.img_add_pic_main_menu);
        btn_AddData = (Button) findViewById(R.id.btn_add_data_main_menu);
        progressBar =(ProgressBar)findViewById(R.id.progressBar_MainMenu);
    }
    public void getDataFromEdits(){
        arabic_title = edt_ArTitle.getText().toString().trim();
        english_title = edt_EnTitle.getText().toString().trim();
    }
    public void openGallery(){
        img_addTitlePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToGallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGallery, 10);
            }
        });
    }
    public void sendData(){
        if(uri_ReadyToSend != null && ! arabic_title.isEmpty() && !english_title.isEmpty()){
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setTitle("برجاء الانتظار...");
            progressDialog.setMessage("جار اضافه البيانات الي القائمه الرئيسيه");
            progressDialog.show();
            randomNum = getRandomCount() ;
            setEnglishData(randomNum);
        }else {
            progressDialog.hide();
            Toast.makeText(this, "هناك خانه ما زالت فارغه !!", Toast.LENGTH_SHORT).show();
        }
    }
    public void setEnglishData(final long m){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("image",uri_ReadyToSend.toString());
        hashMap.put("title",english_title);

        fireBaseVar.mMainMenuEn.child(String.valueOf(m)).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    setArabicData(m);
                }
            }
        });
    }
    public void setArabicData(long m){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("image",uri_ReadyToSend.toString());
        hashMap.put("title",arabic_title);

        fireBaseVar.mMainMenuAr.child(String.valueOf(m)).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    edt_ArTitle.setText("");
                    edt_EnTitle.setText("");
                    uri_Image = null ;
                    uri_ReadyToSend = null ;
                    progressBar.setVisibility(View.INVISIBLE);
                    img_addTitlePic.setImageResource(R.drawable.add);
                    Toast.makeText(MainMenuInputs.this, "تم اضافه العنصر الي القائمة", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
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


            img_addTitlePic.setImageResource(0);
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
                                    Glide.with(MainMenuInputs.this).load(uri_ReadyToSend).into(img_addTitlePic);
                                }else {
                                    img_addTitlePic.setImageResource(R.drawable.add);
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            }
                        });
                    }
                }
            });


        }
    }



    public long getRandomCount(){
        Calendar calendar = Calendar.getInstance();
        long millisec = calendar.getTimeInMillis();
        long ran = new Random().nextInt(50000) + 101 ;

        return millisec * ran + 6 ;
    }

}
