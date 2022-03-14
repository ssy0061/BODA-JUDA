package com.aeye.thirdeye

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.ArrayList

class MainActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TODO: 버튼 선택에 따른 인터페이스 종류 intent로 넘기기
        val button = findViewById<Button>(R.id.button_main_use_button).apply {
            setOnClickListener {
                startActivity(Intent(this@MainActivity, LivePreviewActivity::class.java))
//                startActivity(Intent(this@MainActivity, CameraXSourceActivity::class.java))
            }
        }

        val gestureButton = findViewById<Button>(R.id.button_main_use_gesture).apply {
            isEnabled = false
        }

        if (!allRuntimePermissionsGranted()) {
            getRuntimePermissions()
        }
    }


}