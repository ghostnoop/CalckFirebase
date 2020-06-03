package com.testfirebaseapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import androidx.appcompat.app.AppCompatActivity;
import com.testfirebaseapp.web.CustomWebViewClient;
import java.util.Objects;



public class WebViewActivity extends AppCompatActivity {
    private WebView webView;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingUI();
        setContentView(R.layout.activity_web_view);

        webView = findViewById(R.id.webview);
        webView.setWebViewClient(new CustomWebViewClient());


        String url = Objects.requireNonNull(getIntent().getExtras()).getString("url");
        //todo cookies
        this.webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);


    }

    private void settingUI() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }


    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
