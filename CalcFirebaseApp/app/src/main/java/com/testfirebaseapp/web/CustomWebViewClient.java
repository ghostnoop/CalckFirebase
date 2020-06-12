package com.testfirebaseapp.web;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class CustomWebViewClient extends WebViewClient {
    ProgressBar progressBar;

    public CustomWebViewClient(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        view.loadUrl(request.getUrl().toString());
        return true;
    }

    // Для старых устройств
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        String cookies = CookieManager.getInstance().getCookie(url);
        if (cookies != null)
            Log.d("save cookies:", cookies);
        prefs = view.getContext().getSharedPreferences("cookies", Context.MODE_PRIVATE);
        editor = prefs.edit();
        editor.putString("cookies", cookies);
        editor.apply();
        progressBar.setProgress(0);
        view.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }


}