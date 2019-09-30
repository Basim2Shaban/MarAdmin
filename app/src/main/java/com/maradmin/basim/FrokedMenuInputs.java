package com.maradmin.basim;

import android.annotation.SuppressLint;
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
import android.widget.Spinner;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.maradmin.basim.firebase_side.FireBaseVar;
import com.maradmin.basim.firebase_side.HandleWithData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class FrokedMenuInputs extends AppCompatActivity {
    private Spinner spin_chooseArMTitle , spin_chooseEnMTitle ;
    private EditText edt_ArFroked , edt_EnFroked ;
    private ImageView img_FrokedMain ;
    private Button btn_AddData ;
    private ProgressBar progressBar ;
    private FireBaseVar fireBaseVar = new FireBaseVar();
    private HandleWithData handleWithData = new HandleWithData();
    private String arabic_title , english_title  ;
    private static  String  mainKey_spinnerAR  ,mainKey_spinnerEN ;
    private Uri uri_Image , uri_ReadyToSend ;
    private byte [] byteBitmapArray ;
    private ProgressDialog progressDialog ,mProgress;
    public static long randomNum ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.froked_menu_inputs);


        collectViewsHere();
        openGallery();

        btn_AddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromEdits();
                if (mainKey_spinnerAR != null && mainKey_spinnerEN != null &&
                        !arabic_title.isEmpty() && !english_title.isEmpty() && uri_ReadyToSend != null){

                    progressDialog = new ProgressDialog(FrokedMenuInputs.this);
                    progressDialog.setCancelable(false);
                    progressDialog.setTitle("برجاء الانتظار..");
                    progressDialog.setMessage("جار اضافه البيانات الي القائمه");
                    progressDialog.show();
                    randomNum = getRandomCount() ;
                    setEnglishData(randomNum);

                }else{
                    Toast.makeText(FrokedMenuInputs.this, "يرجي عدم ترك اي خانه فارغه !!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    // of course this method for collect all views in it
    public void collectViewsHere(){
        spin_chooseArMTitle = (Spinner)findViewById(R.id.spin_choose_main_arabic);
        spin_chooseEnMTitle = (Spinner)findViewById(R.id.spin_choose_main_english);
        edt_ArFroked = (EditText)findViewById(R.id.edt_arabic_title_froked);
        edt_EnFroked = (EditText)findViewById(R.id.edt_english_title_froked);
        img_FrokedMain = (ImageView)findViewById(R.id.img_add_froked);
        btn_AddData = (Button)findViewById(R.id.button_add_data_froked);
        progressBar =(ProgressBar)findViewById(R.id.dialogParFroked);
    }
    // and this for call method get english data and the other in below for arabic data
    public void getEnglishData(){
        handleWithData.getDataTOSpinner(fireBaseVar.mMainMenuEn ,this ,spin_chooseEnMTitle , "En",mProgress);
    }
    public void getArabicData(){
        handleWithData.getDataTOSpinner(fireBaseVar.mMainMenuAr ,this ,spin_chooseArMTitle, "Ar" , mProgress);
    }
    // here i collected the all data from views to make it fast and ready to use
    public void getDataFromEdits(){
        mainKey_spinnerAR = handleWithData.getSpinnerArKey();
        mainKey_spinnerEN = handleWithData.getSpinnerEnKey();
        arabic_title = edt_ArFroked.getText().toString().trim();
        english_title = edt_EnFroked.getText().toString().trim();
    }
    // is name it explain to you what i do by use it
    public void openGallery(){
        img_FrokedMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToGallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGallery, 10);
            }
        });
    }
    // and those are to set data to database
    public void setEnglishData(final long m){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("image",uri_ReadyToSend.toString());
        hashMap.put("title",english_title);

        fireBaseVar.mMainMenuEn.child(mainKey_spinnerEN).child("branch").child(String.valueOf(m)).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
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

        fireBaseVar.mMainMenuAr.child(mainKey_spinnerAR).child("branch").child(String.valueOf(m)).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    edt_ArFroked.setText("");
                    edt_EnFroked.setText("");
                    uri_Image = null ;
                    uri_ReadyToSend = null ;
                    progressBar.setVisibility(View.INVISIBLE);
                    img_FrokedMain.setImageResource(R.drawable.add);
                    progressDialog.dismiss();
                    Toast.makeText(FrokedMenuInputs.this, "تمت اضافه البيانات", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(FrokedMenuInputs.this, "يرجي اغاده المحاوله !!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("برجاء الانتظار..");
        mProgress.setMessage("جار تحضير بيانات الصفحه");
        mProgress.show();

        getEnglishData();
        getArabicData();

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


            img_FrokedMain.setImageResource(0);
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
                                    Glide.with(FrokedMenuInputs.this).load(uri_ReadyToSend).into(img_FrokedMain);
                                }else {
                                    img_FrokedMain.setImageResource(R.drawable.add);
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
