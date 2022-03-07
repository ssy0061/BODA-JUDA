package com.ssy.listener

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.ssy.listener.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // OnClickListener 입력하면서 자동완성
        val listener = object : View.OnClickListener {
            // 리스너 앞에 object 추가 후
            // ctrl + i 누르고 onClick 선택(더블클릭)
            override fun onClick(p0: View?) {
                Log.d("리스너", "클릭되었습니다.")
            }
        }

        binding.button.setOnClickListener(listener)

        // 리스너의 함수가 하나일 때는 중괄호로 코드를 줄일 수 있음
        // (리스너의 함수가 onClick 외에 더 있으면(2개 이상이면) 못함)
        // listener 없이 한줄로 표현
        binding.button2.setOnClickListener {
            Log.d("리스너", "함수 하나는 리스너 없이 한 줄로 쓰자")
        }
    }
}