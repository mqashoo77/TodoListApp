package com.example.todo_listapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Week extends AppCompatActivity {
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
        setContentView(R.layout.activity_week);
        ly = (LinearLayout) findViewById(R.id.linearLayoutWeek);
    }


    public void onResume(){
        super.onResume();
        drawerLayout = findViewById(R.id.drawer_layout);
        TextView bar_txt = findViewById(R.id.bar_view);
        bar_txt.setText("Week Tasks");
        ArrayList<Integer> completedTasksList = new ArrayList<>();
        DataBaseHelper dataBaseHelper = new DataBaseHelper(Week.this,"db", null,1);
        sharedPrefManager = SharedPrefManager.getInstance(Week.this);
        String email = sharedPrefManager.readString("email","Does Not Exist");
        Cursor weekTasksCursor = dataBaseHelper.getWeekTasks(email);
        while (weekTasksCursor.moveToNext()) {
            //completedTasksList.add(todayTasksCursor.getInt(6));
            Task task = new Task();
            task.setId(weekTasksCursor.getInt(0));
            task.setName(weekTasksCursor.getString(1));
            task.setDescription(weekTasksCursor.getString(2));
            task.setYear(weekTasksCursor.getInt(3));
            task.setMonth(weekTasksCursor.getInt(4));
            task.setDay(weekTasksCursor.getInt(5));
            task.setUserEmail(weekTasksCursor.getString(6));
            task.setCompleted(weekTasksCursor.getInt(7));
            tasks.add(task);

            TextView textView = new TextView(Week.this);
            textView.setTextSize(18);
            textView.setText(
                    "Task Name = " + weekTasksCursor.getString(1)
                            + "\nDescription = " + weekTasksCursor.getString(2)
                            + "\nDate = " + weekTasksCursor.getString(5)
                            + "/" + weekTasksCursor.getString(4)
                            + "/" + weekTasksCursor.getString(3));

            LinearLayout horizontalLinearLayout = new LinearLayout(this);
            horizontalLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
            CheckBox completedCheckBox = new CheckBox(this);
            Button buttonDeleteTask = new Button(this);
            buttonDeleteTask.setText("Delete Task");
            deleteButtons.add(buttonDeleteTask);
            if(task.getCompleted() == 1)
                completedCheckBox.setChecked(true);
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

        for(int i = 0; i<deleteButtons.size(); i++){
            final int ind = i;
            deleteButtons.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DataBaseHelper dataBaseHelper = new DataBaseHelper(Week.this,"db", null,1);
                    dataBaseHelper.deleteTask(tasks.get(ind));
                    tasks.remove(tasks.get(ind));
                    ly.removeView(verticalLa.get(ind));
                }
            });
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
        MainActivity.logOut(Week.this);
    }
}