package com.example.todo_listapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Profile extends AppCompatActivity {
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        drawerLayout = findViewById(R.id.drawer_layout);
        TextView bar_txt = findViewById(R.id.bar_view);
        bar_txt.setText("Profile Info");
        SharedPrefManager sharedPrefManager;

        TextView textViewName = (TextView) findViewById(R.id.textViewName);
        TextView textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        TextView textViewPassword = (TextView) findViewById(R.id.textViewPassword);

        sharedPrefManager = SharedPrefManager.getInstance(Profile.this);
        String email = sharedPrefManager.readString("email","Does Not Exist");
        DataBaseHelper dataBaseHelper = new DataBaseHelper(Profile.this,"db", null,1);
        Cursor cursorAllUsers = dataBaseHelper.getAllUsers();
        User user = new User();
        while (cursorAllUsers.moveToNext()){
            if(cursorAllUsers.getString(0).equals(email)){
                user.setEmail(cursorAllUsers.getString(0));
                user.setFirstName(cursorAllUsers.getString(1));
                user.setLastName(cursorAllUsers.getString(2));
                user.setPassword(cursorAllUsers.getString(3));

                textViewName.setText(user.getFirstName() + " " + user.getLastName());
                textViewEmail.setText(user.getEmail());
                textViewPassword.setText(user.getPassword());
                break;
            }
        }

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
        MainActivity.logOut(Profile.this);
    }
}