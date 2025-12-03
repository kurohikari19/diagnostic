package com.example.diagnostic.cartelera

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.diagnostic.R

class sesiones : AppCompatActivity(), SensorEventListener {

    private lateinit var adminButton: Button
    private lateinit var userButton: Button
    private lateinit var exitButton: Button

    private lateinit var mainLayout: View
    private lateinit var titleText: TextView
    private lateinit var adminLabel: TextView
    private lateinit var userLabel: TextView

    // Variables Sensor Luz
    private lateinit var sensorManager: SensorManager
    private var sensorLuz: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sesiones)

        // Inicializar Vistas
        mainLayout = findViewById(R.id.main)
        titleText = findViewById(R.id.titleText)
        adminLabel = findViewById(R.id.adminLabel)
        userLabel = findViewById(R.id.userLabel)

        adminButton = findViewById(R.id.adminButton)
        userButton = findViewById(R.id.userButton)
        exitButton = findViewById(R.id.exitButton)

        // Inicializar Sensor de Luz
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorLuz = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        // Listeners Botones
        adminButton.setOnClickListener {
            val intent = Intent(this, login::class.java)
            startActivity(intent)
        }

        userButton.setOnClickListener {
            val intent = Intent(this, login2::class.java)
            startActivity(intent)
        }

        exitButton.setOnClickListener {
            finishAffinity()
        }
    }

    // Función para cambiar Tema
    private fun cambiarTema(modoOscuro: Boolean) {
        val colorTexto: Int
        val colorFondo: Int

        if (modoOscuro) {
            // Modo Oscuro
            colorFondo = Color.parseColor("#121212")
            colorTexto = Color.WHITE
        } else {
            // Modo Claro
            colorFondo = Color.WHITE
            colorTexto = Color.BLACK
        }

        // Aplicar cambios
        mainLayout.setBackgroundColor(colorFondo)
        titleText.setTextColor(colorTexto)
        adminLabel.setTextColor(colorTexto)
        userLabel.setTextColor(colorTexto)
    }

    // Ciclo de vida del Sensor
    override fun onResume() {
        super.onResume()
        sensorLuz?.also { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        if (event.sensor.type == Sensor.TYPE_LIGHT) {
            val luzActual = event.values[0]
            // Si hay menos de 10 luxes, activamos modo oscuro
            cambiarTema(luzActual < 10)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No necesario
    }
}