package com.example.onlinestoreapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ProductAdapter extends ArrayAdapter<Product> {
    DataBaseHelper db;
    String userName;

    ProductAdapter(Context context, Product[] products, String username) {
        super(context, R.layout.product_layout, products);
        userName = username;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.product_layout, parent, false);
        db = new DataBaseHelper(getContext());

        final Product product = getItem(position);
        final TextView productName = customView.findViewById(R.id.product_name);
        TextView productPrice = customView.findViewById(R.id.product_price);
        Button viewBtn = customView.findViewById(R.id.view_btn);
        Button addToCartBtn = customView.findViewById(R.id.add_to_cart);
        final EditText amount = customView.findViewById(R.id.amount_to_add);

        if (product != null) {
            productName.setText(product.productName);
            productPrice.setText(product.productPrice);
        }
        amount.setText("1");

        final Intent intent = new Intent(getContext(), ProductOverview.class);

        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("productName", product.productName);
                getContext().startActivity(intent);
            }
        });

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int am = Integer.parseInt(product.productAmount);
                String fromEditText = amount.getText().toString();
                am = am - Integer.parseInt(fromEditText);
                db.decreaseProductAmount(Integer.toString(am), product.productName);

                db.addToCart(userName, product.productName, fromEditText);
                Toast.makeText(getContext(), product.productName + " was added to your cart!", Toast.LENGTH_SHORT).show();
            }
        });

        return customView;
    }
}
