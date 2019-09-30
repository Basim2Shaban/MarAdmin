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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class AddProduct extends AppCompatActivity {
private String keyMenu , keyBranch ;
private Button btnAddNewPro ;
private EditText edtArName , edtEnName , edtPrice ;
private Spinner spinnerUnit ;
private ProgressBar progressBar ;
private ImageView imageViewPro ;
private FireBaseVar fireBaseVar = new FireBaseVar();
private HandleWithData handleWithData = new HandleWithData();
private String arabicName , englishName , price ;
private Uri uri_Image , uri_ReadyToSend ;
private int unit ;
private byte [] byteBitmapArray ;
public static long randomNum ;
private ProgressDialog progressDialog ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        Intent getIntent = getIntent();
        keyMenu = getIntent.getStringExtra("menu");
        keyBranch = getIntent.getStringExtra("branch");


        collectViewHere();
        openGallery();
        handleSpinner();

        btnAddNewPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromEdits();
                if (unit != 0 && !arabicName.isEmpty() && !englishName.isEmpty()&& ! price.isEmpty() && uri_ReadyToSend != null){

                    progressDialog = new ProgressDialog(AddProduct.this);
                    progressDialog.setCancelable(false);
                    progressDialog.setTitle("برجاء الانتظار..");
                    progressDialog.setMessage("جار اضافه البيانات الي القائمه");
                    progressDialog.show();
                    randomNum = getRandomCount() ;
                    setEnglishData(randomNum);

                }else{
                    progressDialog.hide();
                    Toast.makeText(AddProduct.this, "يرجي عدم ترك اي خانه فارغه !!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void collectViewHere(){
        btnAddNewPro = (Button)findViewById(R.id.btn_addNewPro);
        edtArName = (EditText)findViewById(R.id.editNameOfProAr);
        edtEnName = (EditText)findViewById(R.id.edtNameOfProEn);
        edtPrice = (EditText)findViewById(R.id.edt_priceOfPro);
        spinnerUnit = (Spinner)findViewById(R.id.spinUnitWieght);
        imageViewPro = (ImageView) findViewById(R.id.img_add_picToPro);
        progressBar = (ProgressBar) findViewById(R.id.progressBarPro__);

    }
    public void getDataFromEdits(){
        arabicName = edtArName.getText().toString().trim();
        englishName = edtEnName.getText().toString().trim();
        price = edtPrice.getText().toString().trim();

    }
    public void handleSpinner(){
        ArrayList<String> ar_string = new ArrayList<>();
        ar_string.add("اختر وحده الوزن");
        ar_string.add(" 100 جم");
        ar_string.add("500 جم");
        ar_string.add("1000 جم");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, ar_string);
        spinnerUnit.setAdapter(arrayAdapter);

        spinnerUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0){
                    switch (spinnerUnit.getSelectedItemPosition()){
                        case 1 :
                            unit = 100 ;
                            break;
                        case 2 :
                            unit = 500 ;
                            break;
                        case 3 :
                            unit = 1000 ;
                    }
                }else{
                    unit = 0 ;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void setArabicData(long m){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("image", String.valueOf(uri_ReadyToSend));
        hashMap.put("arabic_name",arabicName);
        hashMap.put("english_name",englishName);
        hashMap.put("price",price);
        hashMap.put("unit", String.valueOf(unit));

        fireBaseVar.mMainMenuAr.child(keyMenu).child("branch").child(keyBranch).child("products")
                .child(String.valueOf(m)).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @SuppressLint("NewApi")
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    edtArName.setText("");
                    edtEnName.setText("");
                    edtPrice.setText("");
                    spinnerUnit.setSelection(0);
                    price = null ; arabicName = null ; englishName = null ;
                    uri_Image = null ;
                    uri_ReadyToSend = null ;
                    progressBar.setVisibility(View.INVISIBLE);
                    imageViewPro.setImageResource(R.drawable.add);
                    Toast.makeText(AddProduct.this, "تمت اضافه البيانات", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }else {
                    Toast.makeText(AddProduct.this, "يرجي اغاده المحاوله !!", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }
        });
    }
    public void setEnglishData(final long m){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("image", String.valueOf(uri_ReadyToSend));
        hashMap.put("arabic_name",arabicName);
        hashMap.put("english_name",englishName);
        hashMap.put("price",price);
        hashMap.put("unit", String.valueOf(unit));

        fireBaseVar.mMainMenuEn.child(keyMenu).child("branch").child(keyBranch).child("products")
                .child(String.valueOf(m)).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    setArabicData(m);
                }else{
                    Toast.makeText(AddProduct.this, "عذرا لم تتم اضافه البيانات", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void openGallery(){
        imageViewPro.setOnClickListener(new View.OnClickListener() {
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

            Calendar cal = Calendar.getInstance();
            long i = cal.getTimeInMillis();


            imageViewPro.setImageResource(0);
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
                                    Glide.with(AddProduct.this).load(uri_ReadyToSend).into(imageViewPro);
                                }else {
                                    imageViewPro.setImageResource(R.drawable.add);
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
