package com.example.onlinestoreapplication;

import java.util.Comparator;

class SortByPrice implements Comparator<Product>
{
    public int compare(Product a, Product b)
    {
        return a.productPrice.compareTo(b.productPrice);
    }
}