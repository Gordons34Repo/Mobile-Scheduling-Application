package com.example.cmpt395finalproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<String> empID, empFName, empLName, empPhone, empEmail, trained;

    int position;

    CustomAdapter(Context context, ArrayList empID, ArrayList empFName, ArrayList empLName, ArrayList empPhone,
                  ArrayList empEmail, ArrayList training){
        this.context = context;
        this.empID = empID;
        this.empFName = empFName;
        this.empLName = empLName;
        this.empPhone = empPhone;
        this.empEmail = empEmail;
        this.trained = training;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflator = LayoutInflater.from(context);
        View view = inflator.inflate(R.layout.view_colleagues, parent, false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.emp_Fname_txt.setText(String.valueOf(empFName.get(position)));
        holder.emp_Lname_txt.setText(String.valueOf(empLName.get(position)));
        holder.emp_email_txt.setText(String.valueOf(empEmail.get(position)));
        holder.emp_phone_txt.setText(String.valueOf(empPhone.get(position)));
        holder.training_txt.setText(String.valueOf(trained.get(position)));
        holder.EditLayOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditColleagues.class);
                intent.putExtra("empID", String.valueOf(empID.get(position)));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return empFName.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView emp_Fname_txt, emp_Lname_txt, emp_email_txt, emp_phone_txt, training_txt;
        LinearLayout EditLayOut;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            emp_Fname_txt = itemView.findViewById(R.id.emp_Fname_txt);
            emp_Fname_txt.setSingleLine(true);
            emp_Fname_txt.setEllipsize(android.text.TextUtils.TruncateAt.END);
            emp_Lname_txt = itemView.findViewById(R.id.emp_Lname_txt);
            emp_Lname_txt.setSingleLine(true);
            emp_Lname_txt.setEllipsize(android.text.TextUtils.TruncateAt.END);
            emp_email_txt = itemView.findViewById(R.id.emp_email_txt);;
            emp_email_txt.setSingleLine(true);
            emp_email_txt.setEllipsize(android.text.TextUtils.TruncateAt.END);
            emp_phone_txt = itemView.findViewById(R.id.emp_phone_txt);;
            emp_phone_txt.setSingleLine(true);
            emp_phone_txt.setEllipsize(android.text.TextUtils.TruncateAt.END);
            training_txt = itemView.findViewById(R.id.training_txt);;
            training_txt.setSingleLine(true);
            training_txt.setEllipsize(android.text.TextUtils.TruncateAt.END);
            EditLayOut = itemView.findViewById(R.id.EditLayOut);
        }
    }
}
