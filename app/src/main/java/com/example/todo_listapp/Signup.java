package com.example.todo_listapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.Character;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Signup extends AppCompatActivity {

    User user;
    TextInputLayout layoutFirstName, layoutLastName, layoutEmail, layoutPass, layoutConfirmPass;
    TextInputEditText editTextFirstName, editTextLastName, editTextEmail, editTextPass, editTextConfirmPass;
    Button buttonSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextFirstName = (TextInputEditText) findViewById(R.id.f_name);
        editTextLastName = (TextInputEditText) findViewById(R.id.l_name);
        editTextEmail = (TextInputEditText) findViewById(R.id.u_email);
        editTextPass = (TextInputEditText) findViewById(R.id.u_password);
        editTextConfirmPass = (TextInputEditText) findViewById(R.id.u_c_password);
        buttonSignUp = (Button) findViewById(R.id.btnSignUp);

        layoutFirstName = (TextInputLayout) findViewById(R.id.f_name_layout);
        layoutLastName = (TextInputLayout) findViewById(R.id.l_name_layout);
        layoutEmail = (TextInputLayout) findViewById(R.id.u_email_layout);
        layoutPass = (TextInputLayout) findViewById(R.id.u_password_layout);
        layoutConfirmPass = (TextInputLayout) findViewById(R.id.u_c_password_layout);

        user = new User();

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean validPassword = false;
                layoutFirstName.setErrorEnabled(true);
                if (editTextFirstName.getText().toString().isEmpty()) {
                    layoutFirstName.setError("Required");
                } else {
                    if (editTextFirstName.getText().toString().length() < 3)
                        layoutFirstName.setError("More than 3 characters required");
                    else if (editTextFirstName.getText().toString().length() > 20)
                        layoutFirstName.setError("Less than 20 characters required");
                    else {
                        layoutFirstName.setErrorEnabled(false);
                        user.setFirstName(editTextFirstName.getText().toString());
                    }
                }

                if (editTextLastName.getText().toString().isEmpty()) {
                    layoutLastName.setErrorEnabled(true);
                    layoutLastName.setError("Required");
                } else {
                    layoutFirstName.setErrorEnabled(true);
                    if (editTextLastName.getText().toString().length() < 3)
                        layoutLastName.setError("More than 3 characters required");
                    else if (editTextLastName.getText().toString().length() > 20)
                        layoutLastName.setError("Less than 20 characters required");
                    else {
                        layoutLastName.setErrorEnabled(false);
                        user.setLastName(editTextLastName.getText().toString());
                    }
                }


                if (editTextEmail.getText().toString().isEmpty()) {
                    layoutEmail.setErrorEnabled(true);
                    layoutEmail.setError("Required");
                }
                else {
                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                    String email = editTextEmail.getText().toString().trim();
                    if(email.matches(emailPattern)) {
                        layoutEmail.setErrorEnabled(false);
                        user.setEmail(email);
                    }
                    else{
                        layoutEmail.setErrorEnabled(true);
                        layoutEmail.setError("Not correct Email format");
                    }
                }

                if (editTextPass.getText().toString().isEmpty()) {
                    layoutPass.setErrorEnabled(true);
                    layoutPass.setError("Required");
                } else {
                    String password = editTextPass.getText().toString();
                    boolean doesContainNumber = false;
                    boolean doesContainLowercase = false;
                    boolean doesContainUppercase = false;
                    for(int i=0; i<password.length(); i++){
                        if(Character.isDigit(password.charAt(i)))
                            doesContainNumber = true;
                        else if(Character.isLowerCase(password.charAt(i)))
                            doesContainLowercase = true;
                        else if(Character.isUpperCase(password.charAt(i)))
                            doesContainUppercase = true;
                    }
                    layoutPass.setErrorEnabled(true);
                    if (password.length() < 8)
                        layoutPass.setError("More than 8 characters required");
                    else if (password.length() > 15)
                        layoutPass.setError("Less than 15 characters required");
                    else if(!doesContainNumber)
                        layoutPass.setError("At least one number is required");
                    else if(!doesContainLowercase)
                        layoutPass.setError("At least one lowercase character is required");
                    else if(!doesContainUppercase)
                        layoutPass.setError("At least one uppercase character is required");
                    else {
                        layoutPass.setErrorEnabled(false);
                        user.setPassword(editTextPass.getText().toString());
                        validPassword = true;
                    }
                }

                if (editTextConfirmPass.getText().toString().isEmpty()) {
                    layoutConfirmPass.setErrorEnabled(true);
                    layoutConfirmPass.setError("Required");
                } else {
                    layoutConfirmPass.setErrorEnabled(true);
                    if (editTextPass.getText().toString().isEmpty()) {
                        layoutConfirmPass.setError("Fill Password Field First");
                    } else {
                        if (validPassword && user.getPassword().equals(editTextConfirmPass.getText().toString())) {
                            layoutConfirmPass.setErrorEnabled(false);
                            user.setConfirmPassword(editTextConfirmPass.getText().toString());
                        }
                        else {
                            layoutConfirmPass.setErrorEnabled(true);
                            layoutConfirmPass.setError("Password doesn't match");
                        }
                    }
                }

                if(user.getFirstName() != null && user.getLastName() != null && user.getEmail() != null &&
                        user.getPassword() != null && user.getConfirmPassword() != null){
                    if(!userExists(user.getEmail())){
                        DataBaseHelper dataBaseHelper = new DataBaseHelper(Signup.this, "db", null, 1);
                        dataBaseHelper.addUser(user);
                        Toast toast = Toast.makeText(Signup.this, "A new user has been added", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else{
                        layoutEmail.setErrorEnabled(true);
                        layoutEmail.setError("User with this Email address already exists");
                    }
                }
            }

        });

    }

    public boolean userExists(String Email){
        DataBaseHelper dataBaseHelper = new DataBaseHelper(Signup.this, "db", null, 1);
        Cursor cursor = dataBaseHelper.getAllUsers();
        while (cursor.moveToNext()){
            if(cursor.getString(0).equals(Email))
                return true;
        }
        return false;
    }
}