package com.ssy.edittext

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.widget.addTextChangedListener
import com.ssy.edittext.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            editText.addTextChangedListener { editable -> // 기본값은 it
//                Log.d("에딧텍스트", "입력된 값: ${editable.toString()}")
                textView.text = editable.toString()
            }
        }
    }
}