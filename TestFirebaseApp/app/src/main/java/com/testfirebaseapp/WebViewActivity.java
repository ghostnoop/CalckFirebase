//package com.testfirebaseapp;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.view.View;
//import android.webkit.ValueCallback;
//import android.webkit.WebChromeClient;
//import android.webkit.WebView;
//
//import androidx.annotation.RequiresApi;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.testfirebaseapp.web.CustomWebViewClient;
//
//import java.util.Objects;
//
//
//public class WebViewActivity extends AppCompatActivity {
//    private WebView webView;
//
//
//    @SuppressLint("SetJavaScriptEnabled")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        settingUI();
//        setContentView(R.layout.activity_web_view);
//
//        webView = findViewById(R.id.webview);
//        webView.setWebViewClient(new CustomWebViewClient());
//
//        String url = Objects.requireNonNull(getIntent().getExtras()).getString("url");
//        //todo cookies
//        this.webView.getSettings().setDomStorageEnabled(true);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.loadUrl(url);
//
//
//    }
//
//
////    fun openFileChooser(uploadMsg:ValueCallback<Uri>, acceptType: String = "") {
////        MainActivity.message = uploadMsg
////        val intent = Intent(Intent.ACTION_GET_CONTENT)
////        intent.addCategory(Intent.CATEGORY_OPENABLE)
////        intent.type = "image/*"
////        activity.startActivityForResult(
////                Intent.createChooser(intent, "Chose"),
////                MainActivity.FILECHOOSER_RESULTCODE
////        )
////    }
//
//    private void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
//        MainActivity.Companion.setMessage(uploadMsg);
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("image/*");
////        startActivityForResult(Intent.createChooser(intent,"Chose"),
//////                MainActivity.);
//        //todo MainActivity.FILECHOOSER_RESULTCODE in java
//    }
//
//
//    private void settingUI() {
//        View decorView = getWindow().getDecorView();
//        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);
//    }
//
//
//    @Override
//    public void onBackPressed() {
//        if (webView.canGoBack()) {
//            webView.goBack();
//        } else {
//            super.onBackPressed();
//        }
//    }
//}
