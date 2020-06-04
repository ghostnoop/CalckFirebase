package com.testfirebaseapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.webkit.CookieManager
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.testfirebaseapp.web.CustomWebViewClient
import kotlinx.android.synthetic.main.activity_web_view.*


class WebViewActivity : AppCompatActivity() {




    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        webView.webViewClient = CustomWebViewClient()

        CookieManager.getInstance().acceptCookie()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView,true)
        }

        val url = intent.extras!!.getString("url")

        webView.settings.domStorageEnabled = true
        webView.settings.javaScriptEnabled = true
        webView.settings.setSupportZoom(false)
        webView.settings.allowFileAccess = true
        webView.settings.allowFileAccess = true
        webView.settings.allowContentAccess = true
        webView.settings.setAppCacheEnabled(true)

        if (savedInstanceState == null) {
            val prefs = this.getSharedPreferences("cookies", Context.MODE_PRIVATE)
            val auth_cookie = prefs.getString("cookies", "null")

            if (auth_cookie != "null") {
                webView.loadUrl(url, mutableMapOf("Cookie" to auth_cookie))
                Log.d("cookie", "cookie $auth_cookie set.")
            } else
                webView.loadUrl(url)
        }

        webView.webChromeClient = object : WebChromeClient() {
            fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
                view.loadUrl(url)
                return true
            }
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            override fun onShowFileChooser(
                view: WebView,
                filePath: ValueCallback<Array<Uri>>,
                fileChooserParams: FileChooserParams
            ): Boolean {
                if (message != null) {
                    message!!.onReceiveValue(null)
                    message = null
                }
                messageArray = filePath
                val intent = fileChooserParams.createIntent()
                intent.type = "image/*"
                try {
                    startActivityForResult(intent, REQUEST_SELECT_FILE)
                } catch (e: Exception) {
                    message = null
                    return false
                }
                return true
            }

            fun openFileChooser(uploadMsg: ValueCallback<Uri>, acceptType: String = "") {
                message = uploadMsg
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "image/*"
                startActivityForResult(
                    Intent.createChooser(intent, "Chose"),
                    FILECHOOSER_RESULTCODE
                )
            }
        }


    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        webView.saveState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        webView.restoreState(savedInstanceState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode == REQUEST_SELECT_FILE) {
                if (messageArray == null)
                    return
                messageArray!!.onReceiveValue(
                    WebChromeClient.FileChooserParams.parseResult(
                        resultCode,
                        data
                    )
                )
                messageArray = null
            }
        } else if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == message)
                return
            val result =
                if (data == null || resultCode != RESULT_OK) null else data.data
            message!!.onReceiveValue(result)
            message = null
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        var message: ValueCallback<Uri>? = null
        var messageArray: ValueCallback<Array<Uri>>? = null
        val REQUEST_SELECT_FILE = 100
        val FILECHOOSER_RESULTCODE = 1
    }



    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
