package com.example.onlinestoreapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public DataBaseHelper db;
    private Intent intent;
    SharedPreferences.Editor prefEditor;
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);
        db = new DataBaseHelper(this);
        settings = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        prefEditor = settings.edit();

        final Button loginBtn = findViewById(R.id.login);
        final Button registerBtn = findViewById(R.id.register);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(v);
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    public void register() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void toSS(String username) {
        SharedPreferences.Editor prefEditor = getSharedPreferences("appPrefenreces", MODE_PRIVATE).edit();
        prefEditor.putString("userName", username);
        prefEditor.apply();
    }

    public void login(View view) {
        EditText username = findViewById(R.id.userName);
        EditText password = findViewById(R.id.password);
        String usernameText = username.getText().toString();
        String passwordText = password.getText().toString();
        int id = db.login(usernameText, passwordText);
        toSS(usernameText);
        intent = new Intent(this, ClientMenu.class);
        Log.i("id", Integer.toString(id));
        if (id == -1) {
            Toast.makeText(getApplicationContext(), "The login or password is invalid!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "You are successfully logged in!", Toast.LENGTH_SHORT).show();
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            startActivity(intent);
                        }
                    }, 2000);
        }
    }
}
