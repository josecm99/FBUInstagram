package com.example.fbuinstagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnSignup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);

        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();

                login(username, password);
            }// end onClick
        });
        
        btnSignup = (Button) findViewById(R.id.btnSignup);
        
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();
                
                signup(username, password);
            }
        });

        ParseUser currentUser = ParseUser.getCurrentUser();
        //If the current user is not null when the Activity is being created, just send them
            //to their HomeActivity directly. No need to login again :)
        if (currentUser != null){
            Intent homeIntent = new Intent(this, HomeActivity.class);
            startActivity(homeIntent);
        }// end if

        getSupportActionBar().hide();


    }// end onCreate

    private void signup(String username, String password) {

        ParseUser user = new ParseUser();

        //Set core properties of User
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail("jcruz99@fb.com");

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    Log.d("LoginActivity", "Signup Successful! :)");

                    //If the login was successful, let us move to the HomeActivity
                    Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(homeIntent);
                    finish();

                }else{
                    Log.e("LoginActivity","Something went poopy in the sign up :' )");
                    e.printStackTrace();
                }
            }
        });

    }


    private void login(String username, String password){

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null){
                    Log.d("LoginActivity", "Login successful :)");

                    //If the login was successful, let us move to the HomeActivity
                    Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(homeIntent);

                    etUsername.setText("");
                    etPassword.setText("");


                }// end if
                else{
                    Log.e("LoginActivity", "Login failed :(");
                    e.printStackTrace();
                }
            }
        });

    }


}// end class
