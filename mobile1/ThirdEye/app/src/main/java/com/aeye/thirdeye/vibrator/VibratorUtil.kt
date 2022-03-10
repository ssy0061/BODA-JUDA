package com.aeye.thirdeye.vibrator

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresApi

class VibratorUtil {
    companion object {
        private const val SHORT = 100L
        private const val NO_REPEAT = -1
        private val twiceArray: LongArray = longArrayOf(0L, 100L, 0L, 100L)

        fun vibrate(context: Context) {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(SHORT, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrator.vibrate(SHORT)
            }
        }

        fun vibrateTwice(context: Context) {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(twiceArray,  NO_REPEAT))
            } else {
                vibrator.vibrate(twiceArray, NO_REPEAT)
            }
        }
    }
}