package com.example.cmpt395finalproject;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.cmpt395finalproject.EditScreens.EditColleaguesDayOff;
import com.example.cmpt395finalproject.EditScreens.EditColleaguesMonthlySchedule;
import com.example.cmpt395finalproject.EditScreens.GeneralEmployee;

public class EditViewAdapter extends FragmentStateAdapter {
    private String ID;

    public EditViewAdapter(@NonNull FragmentActivity fragmentActivity, String ID) {
        super(fragmentActivity);
        this.ID = ID;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position){
            case 0:
                return new GeneralEmployee(ID);
            case 1:
                return new EditColleaguesMonthlySchedule(ID);
            case 2:
                return new EditColleaguesDayOff(ID);
            default:
                return new GeneralEmployee(ID);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
