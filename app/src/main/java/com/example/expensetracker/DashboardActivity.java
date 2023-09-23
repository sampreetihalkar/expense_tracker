package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    private CardView cv;
    PieChart pi;
    FirebaseAuth firebaseAuth;
    TextView i,e,b;
    RecyclerView historyRecycleView;
    FirebaseFirestore firebaseFirestore;
    int sumExpense=0;
    ImageView im,out;
    int sumIncome=0;
    private CardView split;
    ArrayList<TransactionModel> transactionModelArrayList;
    TransactionAdapter transactionAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dashboard);
        cv=findViewById(R.id.add_floating_btn);
        i=findViewById(R.id.total_income);
        pi=findViewById(R.id.pieChart);
        im=findViewById(R.id.refresh_btn);
        split=findViewById(R.id.split_bill);
        e=findViewById(R.id.total_expense);
        b=findViewById(R.id.totalBalance);
        out=findViewById(R.id.signout_btn);
        historyRecycleView=findViewById(R.id.history_recycler_view);
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        transactionModelArrayList=new ArrayList<>();
        historyRecycleView.setLayoutManager(new LinearLayoutManager(this));
        historyRecycleView.setHasFixedSize(true);
        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    startActivity(new Intent(DashboardActivity.this,MainActivity.class));
                    finish();
                }
            }
        });
        split.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this,SplitAmount.class));
            }
        });
        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createSignOutDialog();
            }
        });

        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    startActivity(new Intent(DashboardActivity.this,AddTransactionAcitvity.class));
//                    int bal=Integer.parseInt(b.getText().toString());
//                    Intent intent=new Intent(DashboardActivity.this,AddTransactionAcitvity.class);
//                    intent.putExtra("balnce",bal);
//                    startActivity(intent);

                }catch(Exception e){

                }
            }
        });
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(DashboardActivity.this,DashboardActivity.class));
                    finish();
                }catch (Exception e){

                }
            }
        });
        //loadData();
    }

    private void createSignOutDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(DashboardActivity.this);
        builder.setTitle("Sign Out").setMessage("Are you sure ").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                firebaseAuth.signOut();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        sumIncome=0;
        sumExpense=0;

        loadData();
    }

    private void loadData(){
        firebaseFirestore.collection("Expenses").document(firebaseAuth.getUid()).collection("Notes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                transactionModelArrayList.clear();
                for (DocumentSnapshot ds:task.getResult()){
                    TransactionModel model=new TransactionModel(
                            ds.getString("id"),
                            ds.getString("note"),
                            ds.getString("amount"),
                            ds.getString("type"),
                            ds.getString("date")
                            );
                    int amount=Integer.parseInt(ds.getString("amount"));
                    if(ds.getString("type").equals("Expense")){
                        sumExpense=sumExpense+amount;
                    }else{
                        sumIncome=sumIncome+amount;
                    }
                    transactionModelArrayList.add(model);
                }

                i.setText(String.valueOf(sumIncome));
                e.setText(String.valueOf(sumExpense));
                b.setText(String.valueOf(sumIncome-sumExpense));

                transactionAdapter=new TransactionAdapter(DashboardActivity.this,transactionModelArrayList);
                historyRecycleView.setAdapter(transactionAdapter);
                setUpGraph();
                int balance = Integer.parseInt(b.getText().toString());
                if (balance <= 1000) {
                    showBalanceNotification(balance);
                }
            }
        });
    }private void showBalanceNotification(int currentBalance) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
        builder.setTitle("Warning")
                .setMessage("Your current balance is " + currentBalance + ". Be careful with your spending.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }


    private void setUpGraph() {
       List<PieEntry> pieEntryList=new ArrayList<>();
       List<Integer> colorsList=new ArrayList<>();
       if(sumIncome!=0){
           pieEntryList.add(new PieEntry(sumIncome,"Income"));
           colorsList.add(getResources().getColor(R.color.green));
       }
        if(sumExpense!=0){
            pieEntryList.add(new PieEntry(sumExpense,"Expense"));
            colorsList.add(getResources().getColor(R.color.red));
        }
        PieDataSet pieDataSet=new PieDataSet(pieEntryList,String.valueOf(sumIncome=sumExpense));
        pieDataSet.setColors(colorsList);
        PieData pieDat=new PieData(pieDataSet);
        pi.setData(pieDat);
        pi.invalidate();



    }
}