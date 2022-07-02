package com.example.todo_listapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    String Email, Password;
    SharedPrefManager sharedPrefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPrefManager = SharedPrefManager.getInstance(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        EditText editEmail = (EditText) findViewById(R.id.etEmail);
        EditText editPassword = (EditText) findViewById(R.id.etPassword);

        DataBaseHelper dataBaseHelper = new DataBaseHelper(LoginActivity.this, "db", null, 1);
        String savedEmail = sharedPrefManager.readString("SavedEmail", "No Email Saved");
        Cursor cursor = dataBaseHelper.getAllUsers();
        String savedPassword = "";
        while (cursor.moveToNext()){
            if(cursor.getString(0).equals(savedEmail)){
                savedPassword = cursor.getString(3);
                break;
            }
        }
        if(savedPassword != ""){
            editEmail.setText(savedEmail);
            editPassword.setText(savedPassword);
        }

        CheckBox checkBoxRememberMe = (CheckBox) findViewById(R.id.checkBoxRememberMe);
        findViewById(R.id.btnSignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editEmail.getText().toString().isEmpty() || editPassword.getText().toString().isEmpty()){
                    Toast toast = Toast.makeText(LoginActivity.this, "Email or Password is missing", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    Email = editEmail.getText().toString();
                    Password = editPassword.getText().toString();
                    Cursor userExistsCursor = dataBaseHelper.checkLogIn(Email, Password);
                    if(userExistsCursor.getCount() == 0){   //user doesn't exist in database
                        Toast toast =Toast.makeText(LoginActivity.this,"Email or Password is wrong",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else{
                        if(checkBoxRememberMe.isChecked()){
                            sharedPrefManager.writeString("SavedEmail", Email);
                        }
                        sharedPrefManager.writeString("email", Email);  //save email to show tasks for this specific email
                        Intent signInIntent = new Intent(LoginActivity.this, Dashboard.class);
                        startActivity(signInIntent);
                    }
                }
            }
        });

        findViewById(R.id.tvSignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(LoginActivity.this, Signup.class);
                startActivity(signUpIntent);
            }
        });

    }
}