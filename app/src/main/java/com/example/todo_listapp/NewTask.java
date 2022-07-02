package com.example.todo_listapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class NewTask extends AppCompatActivity {
    Task task = new Task();
    DatePickerDialog.OnDateSetListener setListener;
    DrawerLayout drawerLayout;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        drawerLayout = findViewById(R.id.drawer_layout);
        TextView bar_txt = findViewById(R.id.bar_view);
        bar_txt.setText("Add New Task");

        EditText editTextName = (EditText)findViewById(R.id.editTextName);
        EditText editTextDate = (EditText)findViewById(R.id.editTextDate);
        EditText editTextDescription = (EditText)findViewById(R.id.editTextDescription);
        Button buttonAdd = (Button)findViewById(R.id.buttonAdd);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        NewTask.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        setListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                //month = month + 1;
                String date = day + "/" + month + "/" + year;
                editTextDate.setText(date);
            }
        };

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editTextName.getText().toString().isEmpty()) {
                    task.setName(editTextName.getText().toString());
                    if(!editTextDescription.getText().toString().isEmpty()) {
                        task.setDescription(editTextDescription.getText().toString());
                        if (!editTextDate.getText().toString().isEmpty()) {
                            String[] split = editTextDate.getText().toString().split("/");
                            task.setDay(Integer.parseInt(split[0]));
                            task.setMonth(Integer.parseInt(split[1]));
                            task.setYear(Integer.parseInt(split[2]));

                            sharedPrefManager = SharedPrefManager.getInstance(NewTask.this);
                            String email = sharedPrefManager.readString("email","Does Not Exist");
                            task.setUserEmail(email);

                            DataBaseHelper dataBaseHelper = new DataBaseHelper(NewTask.this, "db", null, 1);
                            dataBaseHelper.addTask(task);
                        }
                        else fillAllFields();
                    }
                    else fillAllFields();
                }
                else fillAllFields();
                Toast toast = Toast.makeText(NewTask.this, "a new task is added", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

    }
    public void clickMenu(View view){
        MainActivity.openDrawer(drawerLayout);
    }
    public void add_new_task(View view){
        recreate();
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
        MainActivity.logOut(NewTask.this);
    }
    public void fillAllFields(){
        Toast toast = Toast.makeText(NewTask.this, "Please fill all fields", Toast.LENGTH_SHORT);
        toast.show();
    }
}