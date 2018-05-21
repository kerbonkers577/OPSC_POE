package com.example.a16002749.opscpoe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {


    private Button btnLogin;
    private Context context;
    private EditText edtUsername;
    private EditText edtPassword;
    private String username;
    private String password;
    private String prefUsername;
    private String prefPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        context = this;
        edtUsername.addTextChangedListener(usernameEdited);
        edtPassword.addTextChangedListener(passwordEdited);
        btnLogin.setOnClickListener(loginAttempt);
        SharedPreferences prefs =  PreferenceManager.getDefaultSharedPreferences(this);
        prefUsername = prefs.getString("editUsername","");
        prefPassword = prefs.getString("editPassword","");
    }

    TextWatcher usernameEdited = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            username = s.toString();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    TextWatcher passwordEdited = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            password = s.toString();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    View.OnClickListener loginAttempt = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            if(username == null)
            {
                username = "";
            }

            if(password == null)
            {
                password = "";
            }

            if(prefPassword == null)
            {
                prefUsername = "";
            }
            if(prefUsername == null)
            {
                prefUsername = "";
            }

            if(prefPassword == null)
            {
                prefUsername = "";
            }

            if(username.equals(prefUsername) && password.equals(prefPassword))
            {
                Intent mainAct = new Intent(context, MainActivity.class);
                startActivity(mainAct);
            }
            else
            {
                Toast.makeText(context, "Login Failed",
                        Toast.LENGTH_LONG).show();
            }
        }
    };

}
