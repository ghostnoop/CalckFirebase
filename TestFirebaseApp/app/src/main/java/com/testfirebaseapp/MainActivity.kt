package com.testfirebaseapp

import android.Manifest
import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


class MainActivity : AppCompatActivity() {


    var hasFlash = false
    var isFlashOn = false
    var i: ImageView? = null
    var cameraManager: CameraManager? = null
    var camerId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermission()
        loadNative()
    }

    fun checkPermission() {

    }

    fun loadWebView() {

    }


    fun loadNative() {
        i = findViewById(R.id.iv)

        hasFlash = this.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
        if (!hasFlash) {
//            AlertDialog(this@MainActivity)
//                .setIcon(R.drawable.ic_launcher_foreground)
//                .setTitle("Error")
//                .setMessage("Sorry, your device doesn't support camera flash")
//                .setPositiveButton("OK",
//                    DialogInterface.OnClickListener { dialog, which -> finish() }).show()
        }

        GetCamera()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                1
            )
        }
    }

    fun OnOffButton(view: View?) {
        if (isFlashOn) {
            TurnOffFlash()
//            i.setImageResource(R.drawable.off)
        } else {
            TurnOnFlash()
//            i.setImageResource(R.drawable.on)
        }
    }

    private fun GetCamera() {
        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            camerId = cameraManager!!.cameraIdList[0]
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun TurnOnFlash() {
        try {
            cameraManager!!.setTorchMode(camerId!!, true)
            isFlashOn = true
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun TurnOffFlash() {
        try {
            cameraManager!!.setTorchMode(camerId!!, false)
            isFlashOn = false
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

}
