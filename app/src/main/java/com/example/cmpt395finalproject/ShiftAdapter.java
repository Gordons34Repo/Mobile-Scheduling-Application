package com.example.cmpt395finalproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ShiftAdapter extends RecyclerView.Adapter<ShiftAdapter.MyViewHolder> {

    private Context context;

    private ArrayList<String> EID, FName, LName, Email, trained;
    private String DID, Slot, year, month;
    private Boolean WeekDay;

    ShiftAdapter(Context context, ArrayList EID, ArrayList FName, ArrayList LName, ArrayList Email, ArrayList trained, boolean WeekDay, String DID, String Slot, String Month, String Year){
        this.context = context;
        this.EID = EID;
        this.FName = FName;
        this.LName = LName;
        this.Email = Email;
        this.trained = trained;
        this.WeekDay = WeekDay;
        this.DID = DID;
        this.Slot = Slot;
        this.year = Year;
        this.month = Month;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.shifts, parent, false);



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
        EmployeeMonthlyAvailabilityDatabaseHelper db1 = new EmployeeMonthlyAvailabilityDatabaseHelper(context);
        ShiftDatabaseHelper db = new ShiftDatabaseHelper(context);
        holder.Name_id_txt.setText(String.valueOf(FName.get(position)));
        holder.LName_id_txt.setText(String.valueOf(LName.get(position)));
        holder.Email_id_txt.setText(String.valueOf(Email.get(position)));
        holder.Training_id_txt.setText(String.valueOf(TrainingStatus));
        holder.EditLayOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (WeekDay){
                    int POS;
                    Log.d("DOES THSI UPDATE", "onClick: "+getItemCount()+position);
                    if (getItemCount() == 2){
                         POS = position;
                    }else {
                        POS = 0;
                    }
                    db.deleteShift(DID, EID.get(POS), Slot);
                    db1.markAsModified(EID.get(POS),year,month);
                    EID.remove(POS);
                    FName.remove(POS);
                    LName.remove(POS);
                    Email.remove(POS);
                    trained.remove(POS);
                    notifyItemRemoved(POS);
                }
                else{
                    int POS;
                    Log.d("DOES THSI UPDATE", "onClick: "+getItemCount()+position);
                    if (getItemCount() == 2){
                        POS = position;
                    }else {
                        POS = 0;
                    }
                    db.deleteShift(DID, EID.get(POS), Slot);
                    db1.markAsModified(EID.get(POS),year,month);
                    EID.remove(POS);
                    FName.remove(POS);
                    LName.remove(POS);
                    Email.remove(POS);
                    trained.remove(POS);
                    notifyItemRemoved(POS);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return EID.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Name_id_txt, LName_id_txt, Training_id_txt, Email_id_txt;
        LinearLayout EditLayOut;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Name_id_txt = itemView.findViewById(R.id.Shift_Name);
            Name_id_txt.setSingleLine(true);
            Name_id_txt.setEllipsize(android.text.TextUtils.TruncateAt.END);
            LName_id_txt = itemView.findViewById(R.id.shift_Name);
            LName_id_txt.setSingleLine(true);
            LName_id_txt.setEllipsize(android.text.TextUtils.TruncateAt.END);
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
