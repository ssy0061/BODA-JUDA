package com.ssafy.lotto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import com.ssafy.lotto.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        with(binding){
            val lotteryNumbers = arrayListOf(number1,number2,number3,number4,number5,number6)

            val countDownTimer = object: CountDownTimer(3000, 100){
                override fun onTick(p0: Long) {
                    lotteryNumbers.forEach {
                        val randomNumber = (Math.random() * 45 + 1).toInt()
                        it.text = "${randomNumber}"
                    }
                }

                override fun onFinish() {
                }
            }
            lotteryButton.setOnClickListener {
                if(lotteryButton.isAnimating){
                    lotteryButton.cancelAnimation()
                    countDownTimer.cancel()
                }else{
                    lotteryButton.playAnimation()
                    countDownTimer.start()
                }
            }
        }
    }
}