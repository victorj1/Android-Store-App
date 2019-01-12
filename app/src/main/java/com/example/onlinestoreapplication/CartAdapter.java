package com.example.onlinestoreapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CartAdapter extends ArrayAdapter<CartItem> {
    DataBaseHelper db;
    String userName;

    CartAdapter(Context context, CartItem[] items, String username) {
        super(context, R.layout.cart_item, items);
        userName = username;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.cart_item, parent, false);
        db = new DataBaseHelper(getContext());

        final CartItem item = getItem(position);
        TextView productName = customView.findViewById(R.id.cartProductName);
        TextView totalProductCost = customView.findViewById(R.id.totalProductCost);
        TextView productAmount = customView.findViewById(R.id.cartProductAmount);

        if (item != null) {
            productName.setText(item.productName);
            productAmount.setText(item.amount);
            totalProductCost.setText(Integer.toString(Integer.parseInt(item.amount) * Integer.parseInt(item.price)));
        }
        return customView;
    }
}
