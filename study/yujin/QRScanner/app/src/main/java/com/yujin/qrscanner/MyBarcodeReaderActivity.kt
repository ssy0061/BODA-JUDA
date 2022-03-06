package com.yujin.qrscanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.journeyapps.barcodescanner.CaptureManager
import com.yujin.qrscanner.databinding.ActivityMyBarcodeReaderBinding

class MyBarcodeReaderActivity : AppCompatActivity() {
    private lateinit var capture: CaptureManager
    val binding by lazy { ActivityMyBarcodeReaderBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        capture = CaptureManager(this, binding.barcodeScanner)
        capture.initializeFromIntent(intent, savedInstanceState)
        capture.decode()
    }

    override fun onResume() {
        super.onResume()
        capture.onResume()
    }

    override fun onPause() {
        super.onPause()
        capture.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        capture.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        capture.onSaveInstanceState(outState)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}