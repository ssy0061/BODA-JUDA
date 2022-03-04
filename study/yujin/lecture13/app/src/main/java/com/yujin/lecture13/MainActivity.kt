package com.yujin.lecture13

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.yujin.lecture13.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val binding by lazy{ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        val listner = object:View.OnClickListener {
//            override fun onClick(p0: View?) {
//                Log.d("ㅇㅇ", "리스너")
//            }
//        }

        // 람다 표현식으로 코드를 줄일 수 있다.
        binding.button.setOnClickListener{
            Log.d("dd", "dddddd")
        }


    }
}