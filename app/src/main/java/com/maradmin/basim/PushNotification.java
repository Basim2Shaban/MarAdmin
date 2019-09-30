package com.maradmin.basim;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.maradmin.basim.firebase_side.FireBaseVar;

import java.util.HashMap;
import java.util.Random;

public class PushNotification extends AppCompatActivity {
    private EditText edtNotificationTital , edtNotificationMessage ;
    private Button btnSendNotification ;
    private String notificationTital , notificationMessage ;
    private FireBaseVar fireBaseVar = new FireBaseVar();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_notification);


        collectViewshere();


        btnSendNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromEdits();
                ProgressDialog progressDialog = new ProgressDialog(PushNotification.this);
                if (!notificationTital.isEmpty() && !notificationMessage.isEmpty()){
                    progressDialog.setTitle("برجاء الانتظار..");
                    progressDialog.setMessage("جار ارسال الاشعار الي المستخدمين");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    sendNotificationNow(progressDialog);
                }
                else {
                    Toast.makeText(PushNotification.this, "يرجي عدم ترك الحقل فارغ", Toast.LENGTH_SHORT).show();
                    progressDialog.hide();
                }
            }
        });

    }


    public void collectViewshere(){
        edtNotificationTital = (EditText)findViewById(R.id.edtNotificationTital);
        edtNotificationMessage = (EditText)findViewById(R.id.edtNotificationMessage);
        btnSendNotification = (Button) findViewById(R.id.btnSendNotification);
    }

    public void getDataFromEdits(){
        notificationTital = edtNotificationTital.getText().toString().trim();
        notificationMessage = edtNotificationMessage.getText().toString().trim();
    }

    public void sendNotificationNow(final ProgressDialog progressDialog){
        Random random = new Random();
        int number = random.nextInt(20);
        HashMap<String , String> hashMap = new HashMap<>();
        hashMap.put("title",notificationTital);
        hashMap.put("message",notificationMessage);
        hashMap.put("number", String.valueOf(number));

        fireBaseVar.mNotification.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    edtNotificationTital.setText("");
                    edtNotificationMessage.setText("");
                    notificationTital = null ;
                    notificationMessage = null ;
                    progressDialog.dismiss();
                    Toast.makeText(PushNotification.this, "تم ارسال الاشعار الي المستخدمين", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
