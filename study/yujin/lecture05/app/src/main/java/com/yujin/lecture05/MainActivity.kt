package com.yujin.lecture05

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.yujin.lecture05.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val binding by lazy{ActivityMainBinding.inflate(layoutInflater)}
    val TAG = "MainActivity" // 주로 Activity 이름을 많이 사용한다.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var myName = "김유진"

        val myAge = 31
        val PI = 3.141592 // 상수는 대문자로

        Log.d(TAG, "my name = ${myName}")
        myName = "ddd"
        Log.d(TAG, "my name = ${myName}")

        Log.d(TAG, "PI = ${PI}")


        var myNumbers = "1,2,3,4,5,6"
//        var thisWeekNumers = "5,6,7,8,9,10"
        var thisWeekNumers = "1,2,3,4,5,6"

        if(myNumbers == thisWeekNumers){
            Log.d(TAG, "당첨되었습니다.")
            binding.textLog.text="당첨되었습니다."
        }else{
            Log.d(TAG, "당첨되지 않았습니다.")
            binding.textLog.text="당첨되지 않았습니다."
        }

        for(a in 1..10){
            binding.textLog.append("\n${a}a");
            Log.d(TAG, "num = ${a}입니다")
        }




    }
}