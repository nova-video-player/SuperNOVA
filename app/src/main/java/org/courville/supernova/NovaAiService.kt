package org.courville.supernova

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.app.Service
import java.io.File
import androidx.core.net.toUri

import org.courville.supernova.ai.SpeechSeparator

class NovaAiService : Service() {
    fun l(s: String) {
        android.util.Log.d("NovaAiService", s)
    }

    fun l(s: String, e: Throwable) {
        android.util.Log.e("NovaAiService", s, e)
    }

    private val binder = object : IAdditionalService.Stub() {
        override fun transformAudio(audio: FloatArray): FloatArray {
            l("transformAudio called with audio of length ${audio.size}")
            return audio
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        val modelFile = File(filesDir, "model.onnx")
	l("onCreate")
        if (!modelFile.exists()) {
		l("Downloading model");
            val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val request = DownloadManager.Request("https://github.com/phhusson/SpeechSeparation/raw/refs/heads/master/model.onnx".toUri())
            request.setDestinationInExternalFilesDir(this, null, "model.onnx")
            downloadManager.enqueue(request)
        }
    }
}
