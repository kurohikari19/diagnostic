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
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast // Opcional, para feedback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.diagnostic.R
import kotlin.math.sqrt

class login2 : AppCompatActivity(), SensorEventListener {

    private lateinit var ingresarbtn2: Button
    private lateinit var atrasbtn2: Button
    private lateinit var passwdEdit2: EditText

    // Variables UI para el Tema
    private lateinit var mainLayout: View
    private lateinit var titulo: TextView
    private lateinit var usuario: TextView

    // Variables Sensores
    private lateinit var sensorManager: SensorManager
    private var acelerometro: Sensor? = null
    private var sensorLuz: Sensor? = null

    // Variables Matemáticas Acelerómetro
    private var aceleracionActual = 0f
    private var aceleracionAnterior = 0f
    private var aceleracionTotal = 0f
    private val umbralAgitacion = 14

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login2)

        // Inicializar vistas
        mainLayout = findViewById(R.id.main)
        titulo = findViewById(R.id.titulo)
        usuario = findViewById(R.id.usuario)

        ingresarbtn2 = findViewById(R.id.ingresarbtn2)
        atrasbtn2 = findViewById(R.id.atrasbtn2)
        passwdEdit2 = findViewById(R.id.passwdEdit2)

        // Inicializar Sensores
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        acelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorLuz = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        // Inicializar aceleración
        aceleracionActual = SensorManager.GRAVITY_EARTH
        aceleracionAnterior = SensorManager.GRAVITY_EARTH


        ingresarbtn2.setOnClickListener {
            val password = passwdEdit2.text.toString()

            if (password == "1234") {
                val intent = Intent(this, filtro::class.java)
                startActivity(intent)
            } else {
                passwdEdit2.error = "Contraseña incorrecta"
            }
        }

        atrasbtn2.setOnClickListener {
            val intent = Intent(this, sesiones::class.java)
            startActivity(intent)
        }
    }

    // Función para cambiar Tema (Sensor de Luz)
    private fun cambiarTema(modoOscuro: Boolean) {
        val colorTexto: Int
        val colorFondo: Int
        val colorHint: Int

        if (modoOscuro) {
            // Tema Oscuro
            colorFondo = Color.parseColor("#121212")
            colorTexto = Color.WHITE
            colorHint = Color.LTGRAY
        } else {
            // Tema Claro
            colorFondo = Color.WHITE
            colorTexto = Color.BLACK
            colorHint = Color.DKGRAY
        }

        mainLayout.setBackgroundColor(colorFondo)
        titulo.setTextColor(colorTexto)
        usuario.setTextColor(colorTexto)

        passwdEdit2.setTextColor(colorTexto)
        passwdEdit2.setHintTextColor(colorHint)
    }

    override fun onResume() {
        super.onResume()
        acelerometro?.also { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }
        sensorLuz?.also { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    // Lógica de sensores
    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        when (event.sensor.type) {
            // 1. ACELERÓMETRO: Agitar para borrar contraseña
            Sensor.TYPE_ACCELEROMETER -> {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                aceleracionAnterior = aceleracionActual
                aceleracionActual = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
                val delta = aceleracionActual - aceleracionAnterior
                aceleracionTotal = aceleracionTotal * 0.9f + delta

                if (aceleracionTotal > umbralAgitacion) {
                    if (passwdEdit2.text.isNotEmpty()) {
                        passwdEdit2.text.clear()
                        Toast.makeText(this, "Contraseña borrada", Toast.LENGTH_SHORT).show()
                    }
                    aceleracionTotal = 0f
                }
            }

            // 2. LUZ: Cambiar tema visual
            Sensor.TYPE_LIGHT -> {
                val luzActual = event.values[0]
                cambiarTema(luzActual < 10)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No necesario
    }
}