package com.yujin.lottonumbermaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.yujin.lottonumbermaker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val lotteryNumbers = arrayListOf(
            binding.number1,
            binding.number2,
            binding.number3,
            binding.number4,
            binding.number5,
            binding.number6
        )

        val countDownTimer = object : CountDownTimer(1000, 100) {
            override fun onTick(p0: Long) {
                lotteryNumbers.forEach {
                    val randomNumber = (Math.random() * 45 + 1).toInt()
                    it.text = "${randomNumber}"

                }
            }

            override fun onFinish() {}
        }

        binding.btnStart.setOnClickListener {
            if (binding.btnStart.isAnimating) {
                binding.btnStart.cancelAnimation()
                countDownTimer.cancel()
            } else {
                binding.btnStart.playAnimation()
                countDownTimer.start()
            }
        }
    }
}