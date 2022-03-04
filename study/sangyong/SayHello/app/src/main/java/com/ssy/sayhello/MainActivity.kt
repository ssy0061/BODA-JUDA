package com.ssy.sayhello

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ssy.sayhello.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root) // 수정

        binding.btnSay.setOnClickListener {
            binding.textSay.text = "Hello Kotlin~!!!"
        }
    }
}