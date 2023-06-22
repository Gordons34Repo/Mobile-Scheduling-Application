package com.example.cmpt395finalproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

//public class EditColleagues{ extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    public class EditColleagues extends AppCompatActivity {

        TabLayout tabLayout;
        ViewPager2 viewPager2;
        EditViewAdapter editViewAdapter;



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            String ID = getIntent().getStringExtra("empID");
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_edit_colleagues);
            tabLayout = findViewById(R.id.tabLayout);
            viewPager2 = findViewById(R.id.viewPager);
            editViewAdapter = new EditViewAdapter(this, ID);
            viewPager2.setAdapter(editViewAdapter);
            viewPager2.setUserInputEnabled(false);

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager2.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

            viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    tabLayout.getTabAt(position).select();
                }
            });
        }

        @Override
        public void onBackPressed() {
            Intent intent = new Intent(this, ViewColleagues.class);
            startActivity(intent);
        }
    }
