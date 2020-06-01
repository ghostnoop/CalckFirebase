package com.testfirebaseapp

import android.Manifest.permission
import android.R.id
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.testfirebaseapp.calculator.MainRepository
import com.testfirebaseapp.calculator.ViewModelMain
import com.testfirebaseapp.siminfo.acces_to_sim
import kotlinx.android.synthetic.main.calculator_view.*


class MainActivity : AppCompatActivity() {
    private lateinit var viewModelMain: ViewModelMain


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        checkPermission()
        if (checkPermission()) openWebView() else loadNative()
    }

    fun checkPermission(): Boolean {
        if(simCardInfo()) {
            // get from db
            return true
        }
    }


    fun openWebView(url:String) {
//        val i = Intent(this, TheNextActivity::class.java)
//        i.putExtra("url", url)
//        startActivity(i)

//        Bundle b = getIntent().getExtras();
//int id = b.getInt("id");
    }


    fun loadNative() {
        val factory: ViewModelMain.Factory = ViewModelMain.Factory(MainRepository.getInstance())
        viewModelMain = ViewModelProviders.of(this, factory).get(ViewModelMain::class.java)
        viewModelMain.click(this, framer)

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
            return list[0].equals("") && list[1].equals("ru")
        }
    }


}
