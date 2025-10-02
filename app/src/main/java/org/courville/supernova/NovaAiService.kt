package org.courville.supernova

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
            val s = speechSeparator
            if (s == null) return audio
            val left_in = FloatArray(audio.size/2)
            val right_in = FloatArray(audio.size/2)
            val left_out = FloatArray(audio.size/2)
            val right_out = FloatArray(audio.size/2)
            for(i in 0 until audio.size/2) {
                left_in[i] = audio[2*i]
                right_in[i] = audio[2*i + 1]
            }
            s.run(audio.size/2, left_in, right_in, left_out, right_out)

            for(i in 0 until audio.size/2) {
                audio[2*i] = left_out[i]
                audio[2*i+1] = right_out[i]
            }

            return audio
        }
        override fun setControl(value: Float) {
            speechSeparator?.setControl(value)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    var speechSeparator: SpeechSeparator? = null
    var downloadId: Long = -1

    private val downloadReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id == downloadId) {
                l("Model download completed")
                val modelFile = File(getExternalFilesDir(null), "model.onnx")
                if (modelFile.exists()) {
                    speechSeparator = SpeechSeparator(modelFile.canonicalPath)
                    speechSeparator?.setControl(1.0f)
                }
            }
        }
    }
    override fun onCreate() {
        super.onCreate()
        registerReceiver(downloadReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE), RECEIVER_EXPORTED)
        val modelFile = File(getExternalFilesDir(null), "model.onnx")
        if (!modelFile.exists()) {
            val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val request = DownloadManager.Request("https://github.com/phhusson/SpeechSeparation/raw/refs/heads/master/model.onnx".toUri())
            request.setDestinationInExternalFilesDir(this, null, "model.onnx")
            downloadId = downloadManager.enqueue(request)
        } else {
            speechSeparator = SpeechSeparator(modelFile.canonicalPath)
            speechSeparator?.setControl(1.0f);
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(downloadReceiver)
    }
}
