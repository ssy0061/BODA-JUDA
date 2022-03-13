package com.aeye.thirdeye.vibrator

import android.app.Activity
import android.content.Context
import android.os.Build
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.annotation.RequiresApi
import java.util.*

class TextToSpeechUtil(val activity: Activity, val txt: String): TextToSpeech.OnInitListener {

    val ttsCustom = TextToSpeech(activity, this)

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = ttsCustom.setLanguage(Locale.KOREA)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.d("TTS", "unsupported language")
            } else {
                speakTxt(txt)
            }
        } else {
            Log.d("TTS", "failed initialization")
        }
    }

    private fun speakTxt(txt: String) {
        ttsCustom.speak(txt, TextToSpeech.QUEUE_FLUSH, null, null)
    }
}