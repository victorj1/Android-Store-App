package com.example.onlinestoreapplication;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductOverview extends AppCompatActivity {
    Product product;
    DataBaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_overview);
        db = new DataBaseHelper(this);

        String productName = getIntent().getStringExtra("productName");
        product = db.getProduct(productName);

        TextView productNameTxt = findViewById(R.id.cartProductName);
        TextView productCategoryTxt = findViewById(R.id.productCategory);
        TextView productColorTxt = findViewById(R.id.productColor);
        TextView productAmountTxt = findViewById(R.id.cartProductAmount);
        TextView productPriceTxt = findViewById(R.id.productPrice);
        ImageView productImageIv = findViewById(R.id.productImage);

        new ImageUtils(productImageIv).execute(product.productImage);

        productNameTxt.setText(product.productName);
        productCategoryTxt.setText(product.productCategory);
        productColorTxt.setText(product.productColor);
        productAmountTxt.setText(product.productAmount);
        productPriceTxt.setText(product.productPrice);
    }
}
