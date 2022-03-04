package com.dh.spinner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.dh.spinner.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var data = listOf("-선택하세요", "1월", "2월", "3월")

        var adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data)

        binding.spinner1.adapter = adapter

        binding.spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selected = data.get(p2)
                binding.result.text = selected
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                }

        }


    }
}