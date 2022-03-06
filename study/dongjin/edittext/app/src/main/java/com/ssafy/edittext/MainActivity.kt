package com.ssafy.edittext

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.widget.addTextChangedListener
import com.ssafy.edittext.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding){
            editText.addTextChangedListener {
//                Log.d("에딧텍스트","입력된값${it.toString()}")
                textView.text = it.toString()
            }
        }
    }
}