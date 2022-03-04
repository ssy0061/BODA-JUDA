package com.ssafy.listener

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.ssafy.listener.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Log.d("asf","asf")
            }
        }

        button.setOnClickListener {
            Log.d("asf","asf")
        }
    }
}