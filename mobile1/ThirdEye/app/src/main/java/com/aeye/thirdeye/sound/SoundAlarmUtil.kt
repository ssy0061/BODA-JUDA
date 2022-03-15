package com.aeye.thirdeye.sound

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.aeye.thirdeye.R

object SoundAlarmUtil {

    private const val MAX_STREAM = 1;
    private const val PRIORITY = 8;

    private val audioAttributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()
    private val soundPool = SoundPool.Builder().setMaxStreams(MAX_STREAM).setAudioAttributes(audioAttributes).build()
    var loadId: Any? = null

    fun load(context: Context) {
        loadId = soundPool.load(context, R.raw.alarm, PRIORITY)
    }

    fun play() {
        loadId?.let {
            soundPool.play(it as Int, 1f, 1f, PRIORITY, 0, 1f)
        }
    }

    fun release() {
        soundPool.release()
    }




}