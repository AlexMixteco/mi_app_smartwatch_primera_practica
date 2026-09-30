package com.example.miapp.presentation

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.miapp.R

class MainActivity : ComponentActivity() {

    private lateinit var vibrator: Vibrator
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        // Requiere un archivo en res/raw llamado "archivo" (solo minúsculas y letras)
        mediaPlayer = MediaPlayer.create(this, R.raw.archivo)

        // 1) Botón con Toast
        findViewById<Button>(R.id.boton).setOnClickListener {
            Toast.makeText(this, "Alerta", Toast.LENGTH_SHORT).show()
        }

        // 2) Vibración (500 ms)
        findViewById<Button>(R.id.botonVibrar).setOnClickListener {
            if (vibrator.hasVibrator()) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
                )
            }
        }

        // 3) Reproducir audio
        findViewById<Button>(R.id.botonAudio).setOnClickListener {
            mediaPlayer?.let { if (!it.isPlaying) it.start() }
        }

        // 4) Abrir segunda ventana
        findViewById<Button>(R.id.botonVentana2).setOnClickListener {
            startActivity(Intent(this@MainActivity, Clase2::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
