package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Resetpassword extends AppCompatActivity {
    private EditText resetmail;
    private FirebaseAuth auth;
    private Button reset;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);
        auth=FirebaseAuth.getInstance();
        resetmail=findViewById(R.id.editTextText3);
        reset=findViewById(R.id.button4);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail=resetmail.getText().toString();
                if(TextUtils.isEmpty(userEmail)&&!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                    Toast.makeText(Resetpassword.this,"Enter your Registered mail id",Toast.LENGTH_SHORT).show();

                }else {
                    auth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Resetpassword.this,"Check your email",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Resetpassword.this,MainActivity.class));
                            }
                            else{
                                Toast.makeText(Resetpassword.this,"Unable to send",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}