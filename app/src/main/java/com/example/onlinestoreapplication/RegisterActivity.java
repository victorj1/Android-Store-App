package com.example.onlinestoreapplication;

import android.content.Intent;
import android.hardware.camera2.TotalCaptureResult;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    private boolean isFormValid = true;
    DataBaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        db = new DataBaseHelper(this);
        final Button registerBtn = findViewById(R.id.register_button);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    public void register() {
        isFormValid = true;
        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);
        EditText firstName = findViewById(R.id.firstName);
        EditText lastName = findViewById(R.id.lastName);
        EditText phoneNumber = findViewById(R.id.phoneNumber);
        EditText address = findViewById(R.id.address);
        EditText city = findViewById(R.id.city);
        EditText postalCode = findViewById(R.id.postalCode);
        EditText card = findViewById(R.id.card);
        EditText expiryDate = findViewById(R.id.expiryDate);
        EditText email = findViewById(R.id.email);

        Client client = new Client();
        if (isValid(username))client.userName = getText(username);
        if (isValid(password)) client.password = getText(password);
        if (isValid(firstName)) client.firstName = getText(firstName);
        if (isValid(lastName)) client.lastName = getText(lastName);
        if (isValid(phoneNumber)) client.phoneNumber = getText(phoneNumber);
        if (isValid(address)) client.address = getText(address);
        if (isValid(city)) client.city = getText(city);
        if (isValid(postalCode)) client.postalCode = getText(postalCode);
        if (isValid(card)) client.card = getText(card);
        if (isValid(expiryDate)) client.expiryDate = getText(expiryDate);
        if (isValid(email)) client.email = getText(email);

        if (isFormValid) {
            long id = db.register(client);
            if (id > -1) {
                final Intent intent = new Intent(this, MainActivity.class);
                Toast.makeText(this, "You are successfully registered!", Toast.LENGTH_SHORT).show();
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                startActivity(intent);
                            }
                        }, 2500);
            } else {
                Toast.makeText(this, "Internal Server Error! Try later.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please, fix errors in the form before registration!", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isValid(EditText et) {
        if (et.getText().toString().length() == 0) {
            et.setError("Required!");
            isFormValid = false;
            return false;
        } else {
            return true;
        }
    }

    public String getText(EditText et) {
        return et.getText().toString();
    }
}
