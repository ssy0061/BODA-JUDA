package com.yujin.lottonumbermaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.yujin.lottonumbermaker.databinding.ActivityMainBinding
import com.yujin.lottonumbermaker.databinding.ActivitySlpashBinding

class SplashActivity : AppCompatActivity() {
    val binding by lazy { ActivitySlpashBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        val handler = Handler(Looper.getMainLooper())
        val runnable = Runnable {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        handler.postDelayed(runnable, 3000)

        binding.animationView.setOnClickListener {
            handler.removeCallbacks(runnable)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}