package org.courville.supernova

import android.content.Intent
import android.os.IBinder
import android.app.Service
import org.courville.supernova.IAdditionalService

class NovaAiService : Service() {

    private val binder = object : IAdditionalService.Stub() {
        override fun transformAudio(audio: FloatArray): FloatArray {
            return audio
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }
}