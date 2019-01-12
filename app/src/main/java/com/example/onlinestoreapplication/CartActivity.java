package com.example.onlinestoreapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CartActivity extends AppCompatActivity {
    String userName;
    DataBaseHelper db;
    int totalCost = 0;
    int amount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        db = new DataBaseHelper(this);

        SharedPreferences appPrefs = getSharedPreferences( "appPrefenreces", MODE_PRIVATE);
        userName = String.valueOf(appPrefs.getString("userName","null"));
        CartItem[] items = db.getCart(userName);
        totalCost = db.getOrderTotal(userName);

        ListAdapter adapter = new CartAdapter(this, items, userName);
        amount = items.length;
        ListView view = findViewById(R.id.cart_list);
        view.setAdapter(adapter);

        TextView total = findViewById(R.id.total);
        total.setText("Total: " + Integer.toString(totalCost));

        Button checkoutBtn = findViewById(R.id.checkout_btn);
        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedToCheckout();
            }
        });
    }

    public void proceedToCheckout() {
        Intent intent = new Intent(this, CheckoutActivity.class);
        intent.putExtra("totalCost", Integer.toString(totalCost));
        intent.putExtra("amount", Integer.toString(amount));
        startActivity(intent);
    }
}
