package com.dh.sayhello

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.dh.sayhello.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    val TAG = "MainActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        var myName = "으하하"
        Log.d(TAG, "my name = $myName")
        myName ="홍길동"
        Log.d(TAG, "my name = $myName")
        val PI = 3.141592
        Log.d(TAG, "pi= $PI")

        var myNumbers = "1,2,3,4,5,6"
        var thisWeekNumbers = "1,2,3,4,5,6"
        if(myNumbers == thisWeekNumbers){
            Log.d(TAG,"당첨?")
        }else{
            Log.d(TAG,"당첨 실패 ㅜㅜ")
        }
        binding.btnSay.setOnClickListener {
            Log.d(TAG, "hello Kotiln!!")
            binding.textSay.text = "hello Kotiln!!!"
        }
    }
}