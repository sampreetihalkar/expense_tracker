package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText email,password;
    private Button signp;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        auth=FirebaseAuth.getInstance();
        email=findViewById(R.id.editTextText2);
        password=findViewById(R.id.editTextTextPassword2);
        signp=findViewById(R.id.button3);
        signp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pattern lowercase = Pattern.compile("^.*[a-z].*$");
                Pattern uppercase = Pattern.compile("^.*[A-Z].*$");
                Pattern number = Pattern.compile("^.*[0-9].*$");
                Pattern specialCharacter = Pattern.compile("^.*[^a-zA-Z0-9].*$");

                String mail=email.getText().toString();
                String pass=password.getText().toString();
                if(mail.isEmpty()){
                    email.setError("Email cannot be empty");
                }else
                if(!lowercase.matcher(pass).matches()){
                    password.setError("Password must contain a lowercase character");

                }else
                if(pass.isEmpty()){
                    password.setError("Password cannot be empty");
                }else
                if (!uppercase.matcher(pass).matches()) {
                    password.setError("Password must contain a uppercase character");
                }else
                if (!number.matcher(pass).matches()) {
                    password.setError("Password must contain a number");
                }else
                if (!specialCharacter.matcher(pass).matches()) {
                    password.setError("Password must contain a special character");
                }else{
                    auth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(SignUp.this, "Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUp.this,MainActivity.class));
                            }else{
                                Toast.makeText(SignUp.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

    }
}