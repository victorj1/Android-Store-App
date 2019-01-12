package com.example.onlinestoreapplication;

public class Product {
    public String productName;
    public String productCategory;
    public String productPrice;
    public String productImage;
    public String productAmount;
    public String productColor;
    public Product(){}

    public static int compareProducts(Product a, Product b) {
        return a.productPrice.compareTo(b.productPrice);
    }
}
