package com.ssy.kotlinsyntaxstudy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.ssy.kotlinsyntaxstudy.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 변수 선언
        var myName = "이름" // 변수(재할당 가능)
        Log.d(TAG, "my name=$myName")
        myName = "수정"
        Log.d(TAG, "my name=$myName")
        val PI = 3.141592 // 상수(재할당 불가, 대문자)
        Log.d(TAG, "pi=$PI")

        var myNumbers = "1, 2, 3, 4, 5, 6"
//        var thisWeekNumbers = "5, 6, 7, 8, 9, 10"
        var thisWeekNumbers = "1, 2, 3, 4, 5, 6"
        // 조건문
        if (myNumbers == thisWeekNumbers) {
//            Log.d(TAG, "당첨되었습니다")
            binding.textLog.text = "당첨되었습니다"
        } else {
//            Log.d(TAG, "당첨되지 않았습니다")
            binding.textLog.text = "당첨되지 않았습니다"
        }

        for (index in 1..10) {
//            Log.d(TAG, "현재 숫자는 $index 입니다") // 변수를 그대로 쓰면 뒤에 띄어쓰기!
//            Log.d(TAG, "현재 숫자는 ${index}입니다") // 붙여쓰려면 중괄호
            binding.textLog.append("\n현재 숫자는 ${index}입니다")
        }

    }
}