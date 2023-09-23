package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class SplitAmount extends AppCompatActivity {
    double billAmount;
    EditText editTextPhone,editTextMessage;

    int nop;
    //EditText bill;
    //EditText people;
    //TextView display;
    Button split,btnSent;
    ImageView back;
    double individualAmount;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split_amount);
        editTextMessage=findViewById(R.id.editTextText8);
        editTextPhone=findViewById(R.id.editTextPhone);
        btnSent=findViewById(R.id.button6);
        back=findViewById(R.id.bkk_btn);
        split=findViewById(R.id.button5);
        final EditText bill=(EditText) findViewById(R.id.editTextText5);
        final EditText people=(EditText)findViewById(R.id.editTextText6);
        btnSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(SplitAmount.this, Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED){
                    sendSMS();
                }else{
                    ActivityCompat.requestPermissions(SplitAmount.this,new String[]{Manifest.permission.SEND_SMS},100);
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SplitAmount.this,DashboardActivity.class));
            }
        });

        split.setOnClickListener(new View.OnClickListener() {
            final TextView individualCost=(TextView)findViewById(R.id.textView9);
            @Override
            public void onClick(View view) {
                billAmount=Double.parseDouble(bill.getText().toString());
                nop=Integer.parseInt(people.getText().toString());
                individualAmount=(billAmount)/nop;
                DecimalFormat currency=new DecimalFormat("$###,###,##");
                individualCost.setText("Each person has to pay "+String.format("%.2f",individualAmount));
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100 && grantResults.length>0 &&grantResults[0]==PackageManager.PERMISSION_GRANTED){
            sendSMS();
        }else{
            Toast.makeText(this,"Permission denied",Toast.LENGTH_SHORT).show();
        }
    }

    private void sendSMS() {
        String phone=editTextPhone.getText().toString();
        String messaage=editTextMessage.getText().toString();
        if(!phone.isEmpty()&&!messaage.isEmpty()){
            SmsManager smsManager= SmsManager.getDefault();
            smsManager.sendTextMessage(phone,null,messaage,null,null);
            Toast.makeText(this,"SMS sent Successfully",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this,"Please entre phone number and message",Toast.LENGTH_SHORT).show();
        }
    }
}