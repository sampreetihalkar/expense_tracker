package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private Button signup;
    private Button login;
    private EditText email;
    private EditText password;
    private FirebaseAuth auth;
    private Toolbar toolbar;
    private TextView forgotpassword;

    //@SuppressLint("MissingInflatedId")
        @SuppressLint("MissingInflatedId")
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth=FirebaseAuth.getInstance();
        login=findViewById(R.id.button);
        email=findViewById(R.id.editTextText);
        forgotpassword=findViewById(R.id.textView2);
        password=findViewById(R.id.editTextTextPassword);
        signup=findViewById(R.id.button2);
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Resetpassword.class));

            }
        });
        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null){
                    try {
                        startActivity(new Intent(MainActivity.this,DashboardActivity.class));
                        finish();
                    }catch (Exception e){

                    }
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,SignUp.class);
                startActivity(intent);

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email=email.getText().toString();
                String txt_password=password.getText().toString();
                loginUser(txt_email,txt_password);
            }
        });
    }
    private void loginUser(String email, String password){
        if (email.isEmpty()) {
            Toast.makeText(MainActivity.this, "Email cannot be empty", Toast.LENGTH_SHORT).show();

        }else
        if(password.isEmpty()){
            Toast.makeText(MainActivity.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();

        }else{
            auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, DashboardActivity.class));

                }
            });
        }
    }
}