package io.mityukov.simpla.core.data

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class SoundManager @Inject constructor(
    @param:ApplicationContext private val applicationContext: Context,
) {
    private var soundPool: SoundPool
    private var soundId: Int = 0

    init {
        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setAudioAttributes(attributes)
            .setMaxStreams(1)
            .build()

        soundId = soundPool.load(applicationContext, R.raw.beep, 1)
    }

    fun playBeep() {
        soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f)
    }
}