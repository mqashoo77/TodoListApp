package com.example.todo_listapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity {
    DrawerLayout drawerLayout;
    SharedPrefManager sharedPrefManager;
    ArrayList<CheckBox> completeCheckBoxes = new ArrayList<>();
    ArrayList<Button> deleteButtons = new ArrayList<>();
    ArrayList<LinearLayout> verticalLa = new ArrayList<>();
    ArrayList<Task> tasks = new ArrayList<>();
    LinearLayout ly;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ly = (LinearLayout) findViewById(R.id.linearLayoutDashboard);


    }

    public void onResume(){
        super.onResume();
        drawerLayout = findViewById(R.id.drawer_layout);
        TextView bar_txt = findViewById(R.id.bar_view);
        bar_txt.setText("Today Tasks");
        ArrayList<Integer> completedTasksList = new ArrayList<>();
        DataBaseHelper dataBaseHelper = new DataBaseHelper(Dashboard.this,"db", null,1);
        sharedPrefManager = SharedPrefManager.getInstance(Dashboard.this);
        String email = sharedPrefManager.readString("email","Does Not Exist");
        Cursor todayTasksCursor = dataBaseHelper.getTodayTasks(email);
        while (todayTasksCursor.moveToNext()) {
            //completedTasksList.add(todayTasksCursor.getInt(6));
            Task task = new Task();
            task.setName(todayTasksCursor.getString(0));
            task.setDescription(todayTasksCursor.getString(1));
            task.setYear(todayTasksCursor.getInt(2));
            task.setMonth(todayTasksCursor.getInt(3));
            task.setDay(todayTasksCursor.getInt(4));
            task.setUserEmail(todayTasksCursor.getString(5));
            task.setCompleted(todayTasksCursor.getInt(6));
            tasks.add(task);

            TextView textView = new TextView(Dashboard.this);
            textView.setTextSize(18);
            textView.setText(
                    "Task Name = " + todayTasksCursor.getString(1)
                            + "\nDescription = " + todayTasksCursor.getString(2)
                            + "\nDate = " + todayTasksCursor.getString(5)
                            + "/" + todayTasksCursor.getString(4)
                            + "/" + todayTasksCursor.getString(3));

            LinearLayout horizontalLinearLayout = new LinearLayout(this);
            horizontalLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
            CheckBox completedCheckBox = new CheckBox(this);
            Button buttonDeleteTask = new Button(this);
            buttonDeleteTask.setText("Delete Task");
            deleteButtons.add(buttonDeleteTask);
            completedCheckBox.setText("Completed");

            LinearLayout verticalLayout = new LinearLayout(this);
            verticalLayout.setOrientation(LinearLayout.VERTICAL);
            verticalLayout.addView(textView);
            horizontalLinearLayout.addView(completedCheckBox);
            horizontalLinearLayout.addView(buttonDeleteTask);
            verticalLayout.addView(horizontalLinearLayout);

            ly.addView(verticalLayout);

            verticalLa.add(verticalLayout);
            completeCheckBoxes.add(completedCheckBox);
        }


        for(int i = 0; i<completeCheckBoxes.size(); i++){
            if(completeCheckBoxes.get(i).isChecked()){
                tasks.get(i).setCompleted(1);
            }
        }

        for(int i=0; i<completeCheckBoxes.size(); i++){
            final int ind = i;
            completeCheckBoxes.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(completeCheckBoxes.get(ind).isChecked()){
                        tasks.get(ind).setCompleted(1);
                    }
                    else{
                        tasks.get(ind).setCompleted(0);
                    }
                    if(areTodayTasksCompleted(tasks)){
                        Toast toast = Toast.makeText(Dashboard.this, "Congrats! All tasks are completed for today", Toast.LENGTH_LONG);
                        toast.show();

                        ImageView imageView = (ImageView) findViewById(R.id.imageView2);
                        imageView.setVisibility(View.VISIBLE);
                        //imageView.startAnimation(AnimationUtils.loadAnimation(Dashboard.this, R.anim.translate));
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setVisibility(View.GONE);
                            }
                        }, 3000);

                    }
                }
            });
        }

        for(int i=0; i<deleteButtons.size(); i++){
            final int ind = i;
            deleteButtons.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DataBaseHelper dataBaseHelper = new DataBaseHelper(Dashboard.this,"db", null,1);
                    dataBaseHelper.deleteTask(tasks.get(ind));
                    tasks.remove(tasks.get(ind));
                    ly.removeView(verticalLa.get(ind));
                }
            });
        }

    }

    @Override
    protected void onPause(){
        super.onPause();
        MainActivity.closeDrawer(drawerLayout);
    }

    public boolean areTodayTasksCompleted(ArrayList<Task> list){
        boolean areAllTasksCompleted = false;
        for(int i=0; i < list.size(); i++){
            if(list.get(i).getCompleted() == 0){
                areAllTasksCompleted = false;
                break;
            }
            areAllTasksCompleted = true;
        }
        return areAllTasksCompleted;
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
        recreate();
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
        MainActivity.logOut(Dashboard.this);
    }


}