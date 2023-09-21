package com.example.services

import android.app.Service
import android.content.Intent
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.IBinder
import android.os.Handler
import android.os.Looper

class BeepService : Service() {
    private var isPlayingTone = false
    private lateinit var toneGenerator: ToneGenerator
    private var isBeeping = false
    private val beepHandler = Handler(Looper.getMainLooper())

    override fun onCreate() {
        super.onCreate()
        // Initialize the ToneGenerator for beeping
        toneGenerator = ToneGenerator(AudioManager.STREAM_ALARM, 20)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!isBeeping) {
            // Start beeping every second
            isBeeping = true
            startBeeping()
        } else if (!isPlayingTone) {
            // Play a different tone
            isPlayingTone = true
            playDifferentTone()
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopBeeping()
    }

    private fun startBeeping() {
        beepHandler.post(object : Runnable {
            override fun run() {
                if (isBeeping) {
                    // Play a beep sound
                    toneGenerator.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 70)
                    beepHandler.postDelayed(this, 500) // Beep every second
                }
            }
        })
    }

    private fun stopBeeping() {
        isBeeping = false
        toneGenerator.stopTone()
        beepHandler.removeCallbacksAndMessages(null)
    }

    private fun playDifferentTone() {
        // Play a different tone (e.g., notification sound)
        toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 100)
        // Stop playing the different tone after 2 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            stopPlayingTone()
        }, 1000)
    }

    private fun stopPlayingTone() {
        isPlayingTone = false
        toneGenerator.stopTone()
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
