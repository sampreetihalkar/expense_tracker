package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdateAcitvity extends AppCompatActivity {
    private EditText amt,not;
    private Button up,del;
    private CheckBox ic,ec;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    ImageView back;
    String newType;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_acitvity);
        amt=findViewById(R.id.user_amount_add);
        up=findViewById(R.id.btn_update_transaction);
        del=findViewById(R.id.btn_delete_transaction);
        firebaseFirestore=FirebaseFirestore.getInstance();
        back=findViewById(R.id.bk_btn);
        firebaseAuth=FirebaseAuth.getInstance();
        not=findViewById(R.id.user_note_add);
        ec=findViewById(R.id.expense_check_box);
        ic=findViewById(R.id.income_check_box);
        String id=getIntent().getStringExtra("id");
        String amount=getIntent().getStringExtra("amount");
        String note=getIntent().getStringExtra("note");
        String type=getIntent().getStringExtra("type");
        amt.setText(amount);
        not.setText(note);
        switch(type){
            case "Income":ic.setChecked(true);
            newType="Income";
            break;
            case "Expense": ec.setChecked(true);
            newType="Expense";
            break;
        }
        ic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newType="Income";
                ic.setChecked(true);
                ec.setChecked(false);
            }
        });
        ec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newType="Expense";
                ic.setChecked(false);
                ec.setChecked(true);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UpdateAcitvity.this,DashboardActivity.class));
            }
        });
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount=amt.getText().toString();
                String no=not.getText().toString();
                firebaseFirestore.collection("Expenses").document(firebaseAuth.getUid()).collection("Notes").document(id).update("amount",amount,"note",no,"type",type).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        onBackPressed();
                        Toast.makeText(UpdateAcitvity.this, "Updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(UpdateAcitvity.this,DashboardActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateAcitvity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(UpdateAcitvity.this,DashboardActivity.class));
                    }
                });
            }
        });
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseFirestore.collection("Expenses").document(firebaseAuth.getUid()).collection("Notes").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        onBackPressed();
                        Toast.makeText(UpdateAcitvity.this, "Deleted", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(UpdateAcitvity.this,DashboardActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateAcitvity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
}