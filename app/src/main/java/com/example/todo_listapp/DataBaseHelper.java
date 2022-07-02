package com.example.todo_listapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DataBaseHelper extends android.database.sqlite.SQLiteOpenHelper{

    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE USERS(email TEXT PRIMARY KEY, firstName TEXT, lastName TEXT, password TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE TASKS(ID INTEGER PRIMARY KEY AUTOINCREMENT, taskName TEXT, taskDescription TEXT, taskYear INTEGER, taskMonth INTEGER, taskDay INTEGER, " +
                "userEmail TEXT, completed INTEGER, FOREIGN KEY (userEmail) REFERENCES USERS(email))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public void addUser(User user){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", user.getEmail());
        contentValues.put("firstName", user.getFirstName());
        contentValues.put("lastName", user.getLastName());
        contentValues.put("password", user.getPassword());
        sqLiteDatabase.insert("USERS", null, contentValues);
    }

    public Cursor getAllUsers(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM USERS", null);
    }

    public Cursor getUser(String email){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM USERS WHERE email = '" + email + "'", null);
    }

    public Cursor checkLogIn(String email, String password){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("Select email From USERS Where email = '"+ email  +"' " +
                "And password = '" + password +"'", null);
    }


    public void addTask(Task task){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("taskName", task.getName());
        contentValues.put("taskDescription", task.getDescription());
        contentValues.put("taskYear", task.getYear());
        contentValues.put("taskMonth", task.getMonth());
        contentValues.put("taskDay", task.getDay());
        contentValues.put("userEmail", task.getUserEmail());
        contentValues.put("completed", 0);
        sqLiteDatabase.insert("TASKS", null, contentValues);
    }


    public Cursor searchTask(Date startDate, Date endDate){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM TASKS WHERE Date >= "+startDate+" AND Date <= "+endDate, null);
    }

    public Cursor getUserTasks(String email) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM TASKS where userEmail = '" + email + "'", null);
    }

    public Cursor getTodayTasks(String email){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear();
        int day = currentDate.getDayOfMonth();
        int month = currentDate.getMonthValue();
        return sqLiteDatabase.rawQuery("SELECT * FROM TASKS WHERE userEmail = '"+email+"' AND taskYear ="+year+" " +
                "AND taskMonth ="+month+ " AND taskDay="+day, null);
    }

    public void deleteTask(Task task){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete("TASKS", "ID = " + task.getId(),null);
    }

    public Cursor getWeekTasks(String email){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        LocalDate date = LocalDate.now();
        int year = date.getYear();
        int day = date.getDayOfMonth();
        int month = date.getMonthValue();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM TASKS WHERE " +
                "userEmail = '"+email+"' AND taskYear = "+year+
                " AND taskMonth = " + month + " AND taskDay >= " + day +
                " AND taskDay < "+ (day + 7),null);
        return cursor;
    }
}