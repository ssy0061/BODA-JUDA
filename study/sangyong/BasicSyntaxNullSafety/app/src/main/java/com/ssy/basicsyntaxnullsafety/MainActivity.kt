package com.ssy.basicsyntaxnullsafety

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var myName:String = "메시"
        var number:Int? = null // nullable(자료형 뒤에 ? 추가)
        var newVariable:Activity? = null

        Log.d("NullTest", "문자열의 길이는 ${myName.length}")

        var number2:Int = 30
        var result2 = number2.plus(50)
        Log.d("NullTest", "Null값이 아니면 계산 가능: ${result2}")
        // null값인 경우 plus가 없기 때문에 Null Pointer Exception 발생
        // -> 방지하기 위해 null safety 사용
        // Safe Call
//        var result = number?.plus(40) // null값이면 ? 뒤가 실행되지 않음
        // result를 사용하면 같은 상황 발생
        // 어떤 변수에 값이 무조건 들어가야 한다면 '?: 기본값' 추가 (Elvis Expression)
        var result = number?.plus(40) ?: 100
        Log.d("NullTest", "결과: ${result}")
    }
}