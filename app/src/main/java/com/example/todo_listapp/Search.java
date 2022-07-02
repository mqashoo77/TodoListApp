package com.example.todo_listapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Search extends AppCompatActivity {
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        drawerLayout = findViewById(R.id.drawer_layout);
        TextView bar_txt = findViewById(R.id.bar_view);
        bar_txt.setText("Search");
    }
    public void clickMenu(View view){
        MainActivity.openDrawer(drawerLayout);
    }

    public void clickLogo(View view){
        MainActivity.closeDrawer(drawerLayout);
    }

    public void add_new_task(View view){
        MainActivity.redirectActicity(this, NewTask.class);
    }
    public void today_task(View view){
        MainActivity.redirectActicity(this, Dashboard.class);
    }
    public void weekly_task(View view){
        MainActivity.redirectActicity(this, Week.class);
    }

    public void all_task(View view){
        MainActivity.redirectActicity(this, All.class);
    }
    public void navSearch(View view){
        MainActivity.redirectActicity(this, Search.class);
    }
    public void navProfile(View view){
        MainActivity.redirectActicity(this, Profile.class);
    }

    public void navLogout(View view) {
        MainActivity.logOut(Search.this);
    }
}