package com.example.onlinestoreapplication;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class CheckoutActivity extends AppCompatActivity {
    DataBaseHelper db;
    String userName;
    String totalCostString;
    String totalAmount;
    String deliveryType;
    Client client;
    String messageToSend;

    int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    String SENT = "SMS_SENT";
    String DELIVERED = "SMS_DELIVERED";

    PendingIntent sentPI, deliveredPI;
    BroadcastReceiver smsSentReceiver, smsDeliveredReceiver;

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(smsSentReceiver);
        unregisterReceiver(smsDeliveredReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        db = new DataBaseHelper(this);

        Button uberEats = findViewById(R.id.uberEats);
        Button skipTheDishes = findViewById(R.id.skipTheDishes);
        Button orderButton = findViewById(R.id.orderButton);

        SharedPreferences appPrefs = getSharedPreferences( "appPrefenreces", MODE_PRIVATE);
        userName = String.valueOf(appPrefs.getString("userName","null"));
        totalCostString = getIntent().getStringExtra("totalCost");
        totalAmount = getIntent().getStringExtra("amount");

        client = db.getClient(userName);

        TextView clientFirstName = findViewById(R.id.clientFirstName);
        TextView clientLastName = findViewById(R.id.clientLastName);
        TextView clientCardNumber = findViewById(R.id.clientCardNumber);
        TextView cardExpiryDate = findViewById(R.id.cardExpiryDate);
        TextView totalCost = findViewById(R.id.totalCost);
        clientFirstName.setText(client.firstName);
        clientLastName.setText(client.lastName);
        clientCardNumber.setText(client.card);
        cardExpiryDate.setText(client.expiryDate);
        totalCost.setText("Total: " + totalCostString);

        messageToSend = "Thank you for order with our application! \n" +
                "You ordered " + totalAmount + " items on " + totalCostString + " CAD. \n " +
                "Your order will arrive in 50 minutes!";

        final Intent menuIntent = new Intent(this, ClientMenu.class);
        Spinner deliverySpinner = findViewById(R.id.deliverySpinner);
        deliverySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] types = getResources().getStringArray(R.array.spinnerDelivery);
                deliveryType = types[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                String[] types = getResources().getStringArray(R.array.spinnerDelivery);
                deliveryType = types[0];
            }
        });

        final Intent intent = new Intent(this, Browser.class);
        uberEats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("url", "https://en.wikipedia.org/wiki/Uber_Eats");
                startActivity(intent);
            }
        });
        skipTheDishes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("url", "https://en.wikipedia.org/wiki/SkipTheDishes");
                startActivity(intent);
            }
        });
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.addOrder(userName, Calendar.getInstance().getTime().toString(), totalCostString, deliveryType, totalAmount);
                sendMessage();
                Toast.makeText(CheckoutActivity.this, "You successfully added an order!", Toast.LENGTH_SHORT).show();
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                startActivity(menuIntent);
                            }
                        }, 3000);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        smsSentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch(getResultCode()) {
                    case Activity
                            .RESULT_OK:
                        Toast.makeText(CheckoutActivity.this, "SMS sent!", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(CheckoutActivity.this, "Generic failure!", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(CheckoutActivity.this, "No Service!", Toast.LENGTH_SHORT).show();
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(CheckoutActivity.this, "Null PDU!", Toast.LENGTH_SHORT).show();
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(CheckoutActivity.this, "Radio off!", Toast.LENGTH_SHORT).show();
                }
            }
        };

        smsDeliveredReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch(getResultCode()) {
                    case Activity
                            .RESULT_OK:
                        Toast.makeText(CheckoutActivity.this, "SMS sent!", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(CheckoutActivity.this, "SMS was cancelled!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        registerReceiver(smsSentReceiver, new IntentFilter(SENT));
        registerReceiver(smsDeliveredReceiver, new IntentFilter(DELIVERED));
    }

    public void sendMessage() {
        String message = messageToSend;
        String telNumber = client.phoneNumber;

        if (ContextCompat.checkSelfPermission(CheckoutActivity.this, Manifest.permission.SEND_SMS) !=
                PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(CheckoutActivity.this, new String[] { Manifest.permission.SEND_SMS },
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        } else
        {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(telNumber, null, message, sentPI, deliveredPI);
        }
    }
}
