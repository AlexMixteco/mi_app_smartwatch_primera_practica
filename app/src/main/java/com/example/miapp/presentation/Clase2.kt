package com.example.miapp.presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.activity.result.contract.ActivityResultContracts
import com.example.miapp.R

class Clase2 : ComponentActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null

    // Cambiar según el sensor a usar (ver tabla de códigos en la presentación):
    // TYPE_HEART_RATE, TYPE_ACCELEROMETER, TYPE_GYROSCOPE, TYPE_PRESSURE, TYPE_LIGHT...
    private val sensorType = Sensor.TYPE_HEART_RATE

    private lateinit var textoLectura: TextView

    private val permisoLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { concedido ->
            if (concedido) startSensor()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ventana2)

        textoLectura = findViewById(R.id.lectura)
        findViewById<Button>(R.id.botonVolver).setOnClickListener { finish() }

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(sensorType)

        if (sensor == null) textoLectura.text = "Sensor no disponible"
        startSensor()
    }

    private fun startSensor() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BODY_SENSORS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            permisoLauncher.launch(Manifest.permission.BODY_SENSORS)
            return
        }
        sensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }


    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == sensorType) {
            val lectura = event.values[0]
            Log.d("onSensorChanged", "Lectura: $lectura")
            textoLectura.text = "Sensor: $lectura"
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BODY_SENSORS)
            == PackageManager.PERMISSION_GRANTED
        ) {
            sensor?.also {
                sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
            }
        }
    }
}
