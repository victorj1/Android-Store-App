package com.example.onlinestoreapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Arrays;

public class OrderActivity extends AppCompatActivity {
    DataBaseHelper db;
    String userName;
    private Product[] products;
    static boolean isSorted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        db = new DataBaseHelper(this);

        SharedPreferences appPrefs = getSharedPreferences( "appPrefenreces", MODE_PRIVATE);
        userName = String.valueOf(appPrefs.getString("userName","null"));

        products = db.getAllProducts();
        ListAdapter adapter = new ProductAdapter(this, products, userName);
        ListView view = findViewById(R.id.products);
        view.setAdapter(adapter);

        Button sortByPrice = findViewById(R.id.sort_by_price);
        final Button clearCart = findViewById(R.id.clear_cart);
        Button proceedToCart = findViewById(R.id.procee_to_cart);
        Button stopMusic = findViewById(R.id.stop_music);

        sortByPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortListView();
            }
        });
        clearCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCart();
            }
        });
        proceedToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedToCart();
            }
        });
        stopMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopMusic();
            }
        });

        Spinner categories = findViewById(R.id.categories_spinner);
        categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] list = getResources().getStringArray(R.array.spinnerItems);
                String item = list[position];
                changeCategoryOfListView(item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                changeCategoryOfListView("All");
            }
        });
    }

    public void sortListView() {
        Product[] sortedProducts;
        if (!isSorted) {
            sortedProducts = products.clone();
            Arrays.sort(sortedProducts, new SortByPrice());
        } else {
            sortedProducts = products.clone();
        }
        ListAdapter adapter = new ProductAdapter(this, sortedProducts, userName);
        ListView view = findViewById(R.id.products);
        view.setAdapter(adapter);
        isSorted = !isSorted;
    }

    public void changeCategoryOfListView(String type) {
        Product[] categoriesProducts;
        if (type.equals("All")) {
            categoriesProducts = db.getAllProducts();
        } else {
            categoriesProducts = db.getProductsByType(type);
        }
        ListAdapter adapter = new ProductAdapter(this, categoriesProducts, userName);
        ListView view = findViewById(R.id.products);
        view.setAdapter(adapter);
    }

    public void clearCart() {
        db.deleteCart(userName);
        Toast.makeText(this, "You successfully cleared the cart!", Toast.LENGTH_SHORT).show();
    }

    public void proceedToCart() {
        Intent intent = new Intent(this, CartActivity.class);
        startActivity(intent);
    }

    public void stopMusic() {
        stopService(new Intent(OrderActivity.this, MusicService.class));
    }
}
