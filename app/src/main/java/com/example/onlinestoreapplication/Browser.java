package com.example.onlinestoreapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Browser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        WebView wb = findViewById(R.id.webBrowser);
        String url = getIntent().getStringExtra("url");
        wb.setWebViewClient(new WebViewClient());
        wb.loadUrl(url);
    }
}
