package com.testfirebaseapp;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

//import butterknife.BindView;
//import butterknife.ButterKnife;

public class WebViewActivity extends AppCompatActivity {

    public static final String TAG = WebViewActivity.class.getSimpleName();

    @BindView(R.id.textView)
    TextView textView;

    @BindView(R.id.webview)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        String url = Objects.requireNonNull(getIntent().getExtras()).getString("url");

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);

//        db.collection("data")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData().get("value"));
//                                textView.setText("Data from Firestore: " + document.getData().get("value"));
//                            }
//                        } else {
//                            Log.w(TAG, "Error getting documents.", task.getException());
//                        }
//                    }
//                });

    }
}
