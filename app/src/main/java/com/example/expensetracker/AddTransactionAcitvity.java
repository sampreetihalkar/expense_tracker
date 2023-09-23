package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class AddTransactionAcitvity extends AppCompatActivity {
    private Button addtr;
    FirebaseFirestore fStore;
    AlertDialog.Builder builder;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private CheckBox expense,income;
    ImageView bkp;
    String type="";
    private EditText userAmountAdd,userNoteAdd;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction_acitvity);
        addtr=findViewById(R.id.btn_add_transaction);
        userAmountAdd=findViewById(R.id.user_amount_add);
        fStore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        bkp=findViewById(R.id.bck_btn);
        firebaseUser=firebaseAuth.getCurrentUser();
        userNoteAdd=findViewById(R.id.user_note_add);
        expense=findViewById(R.id.expense_check_box);
        income=findViewById(R.id.income_check_box);
        builder=new AlertDialog.Builder(this);
        bkp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddTransactionAcitvity.this,DashboardActivity.class));
            }
        });
        expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type="Expense";
                expense.setChecked(true);
                income.setChecked(false);
            }
        });
        income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type="Income";
                expense.setChecked(false);
                income.setChecked(true);
            }
        });
        addtr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//              int blance=Integer.parseInt(getIntent().getStringExtra("balnce"));
                String amount = userAmountAdd.getText().toString().trim();
                String note = userNoteAdd.getText().toString().trim();
//                int amt=Integer.parseInt(amount);
//                if(amt<=0){
//                    Toast.makeText(AddTransactionAcitvity.this, " Enter valid amount", Toast.LENGTH_SHORT).show();
//                }
//                if(amt>blance){
//                    Toast.makeText(AddTransactionAcitvity.this, " Insufficient balance", Toast.LENGTH_SHORT).show();
//                }
                if (amount.length()<=0) {
                    //Toast.makeText(AddTransactionAcitvity.this, " Enter valid amount", Toast.LENGTH_SHORT).show();
                    builder.setTitle("Alert")
                            .setMessage("Enter the valid amount")
                            .setCancelable(true)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startActivity(new Intent(AddTransactionAcitvity.this,AddTransactionAcitvity.class));
                                }
                            }).show();
                } else if (type.length() <= 0) {
                    Toast.makeText(AddTransactionAcitvity.this, "Select transaction type", Toast.LENGTH_SHORT).show();
                }
                else {
                    //if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy_HH:mm", Locale.getDefault());
                    String currentDateandTime = sdf.format(new Date());
                    //}
                    String id = UUID.randomUUID().toString();
                    Map<String, Object> transaction = new HashMap<>();
                    transaction.put("id", id);
                    transaction.put("amount", amount);
                    transaction.put("note", note);
                    transaction.put("type", type);
                    transaction.put("date", currentDateandTime);

                    fStore.collection("Expenses").document(firebaseAuth.getUid()).collection("Notes").document(id).set(transaction).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(AddTransactionAcitvity.this, "Added", Toast.LENGTH_SHORT).show();
                            userNoteAdd.setText(" ");
                            userAmountAdd.setText(" ");
                            startActivity(new Intent(AddTransactionAcitvity.this, DashboardActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddTransactionAcitvity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}