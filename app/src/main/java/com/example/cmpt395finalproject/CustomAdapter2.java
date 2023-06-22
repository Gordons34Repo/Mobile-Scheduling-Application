package com.example.cmpt395finalproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter2 extends RecyclerView.Adapter<CustomAdapter2.MyViewHolder> {

    private Context context;

    private ArrayList<String> EID, FName, LName, Email, trained;
    private String DID, Slot, month, year, day, source;
    private Boolean WeekDay;

    CustomAdapter2(Context context,ArrayList EID, ArrayList FName, ArrayList LName, ArrayList Email, ArrayList trained, boolean WeekDay, String DID, String Slot, String month, String year, String day, String source){
        this.context = context;
        this.EID = EID;
        this.FName = FName;
        this.LName = LName;
        this.Email = Email;
        this.trained = trained;
        this.WeekDay = WeekDay;
        this.DID = DID;
        this.Slot = Slot;
        this.month = month;
        this.year = year;
        this.day = day;
        this.source = source;
        Log.d("THIS VALEUS ARE STORED", "CustomAdapter: "+ EID + FName + LName + Email + trained + DID + Slot);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_schedules, parent, false);

        return new MyViewHolder(view);
    }



    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // Sort training
        String TrainingStatus = "Not trained";
            if (trained.get(position).equals("B")){
                TrainingStatus = "Both trained";
            } else if (trained.get(position).equals("O")){
                TrainingStatus = "Opening trained";
            } else if (trained.get(position).equals("C")){
                TrainingStatus = "Closing trained";
            }

        /*Display the info about the employee*/
        Log.d("THIS IS WORKING RIGHT", "onBindViewHolder: called.)");
        ShiftDatabaseHelper db = new ShiftDatabaseHelper(context);
        EmployeeMonthlyAvailabilityDatabaseHelper db2 = new EmployeeMonthlyAvailabilityDatabaseHelper(context);
        holder.Name_id_txt.setText(String.valueOf(FName.get(position)));
        holder.LName_id_text.setText(String.valueOf(LName.get(position)));
        holder.Email_id_txt.setText(String.valueOf(Email.get(position)));
        holder.Training_id_txt.setText(String.valueOf(TrainingStatus));
        holder.EditLayOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (WeekDay){
                    Intent intent = new Intent(context, weekdayScheduler.class);
                    db.addShift(DID, EID.get(position), Slot, trained.get(position));
                    db2.markAsModified(EID.get(position), year, month);
                    intent.putExtra("day", day);
                    intent.putExtra("month", month);
                    intent.putExtra("year", year);
                    intent.putExtra("source", source);
                    context.startActivity(intent);
                }
                else{
                    Intent intent = new Intent(context, weekendScheduler.class);
                    db.addShift(DID, EID.get(position), Slot, trained.get(position));
                    db2.markAsModified(EID.get(position), year, month);
                    intent.putExtra("day", day);
                    intent.putExtra("month", month);
                    intent.putExtra("year", year);
                    intent.putExtra("source", source);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
      return EID.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Name_id_txt, LName_id_text, Training_id_txt, Email_id_txt;
        LinearLayout EditLayOut;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Name_id_txt = itemView.findViewById(R.id.Shift_Name);
            Name_id_txt.setSingleLine(true);
            Name_id_txt.setEllipsize(android.text.TextUtils.TruncateAt.END);
            LName_id_text = itemView.findViewById(R.id.shift_Name2);
            LName_id_text.setSingleLine(true);
            LName_id_text.setEllipsize(android.text.TextUtils.TruncateAt.END);
            Training_id_txt = itemView.findViewById((R.id.ShiftTraining));
            Training_id_txt.setSingleLine(true);
            Training_id_txt.setEllipsize(android.text.TextUtils.TruncateAt.END);
            Email_id_txt = itemView.findViewById(R.id.Shift_Email);
            Email_id_txt.setSingleLine(true);
            Email_id_txt.setEllipsize(android.text.TextUtils.TruncateAt.END);
            EditLayOut = itemView.findViewById(R.id.EditLayOut);
        }
    }
}
