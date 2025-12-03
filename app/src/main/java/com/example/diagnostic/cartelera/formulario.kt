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
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.diagnostic.R
import kotlin.math.sqrt

class formulario : AppCompatActivity(), SensorEventListener {

    private lateinit var publicarbtn: Button
    private lateinit var borrarbtn: Button
    private lateinit var atrasbtn: Button

    // Referencias de UI para cambiar colores
    private lateinit var mainLayout: View
    private lateinit var titulo: TextView // Variable para el título

    private lateinit var nombre: EditText
    private lateinit var genero: EditText
    private lateinit var clasificacion: EditText
    private lateinit var duracion: EditText
    private lateinit var sinopsis: EditText
    private lateinit var director: EditText

    private lateinit var sensorManager: SensorManager

    // Variables Acelerómetro
    private var acelerometro: Sensor? = null
    private var aceleracionActual = 0f
    private var aceleracionAnterior = 0f
    private var aceleracionTotal = 0f
    private val umbralAgitacion = 14

    // Variables Proximidad
    private var sensorProximidad: Sensor? = null

    // Variables Luz
    private var sensorLuz: Sensor? = null

    companion object {
        val listaPeliculas = mutableListOf<Pelicula>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_formulario)

        // 1. Inicializar Vistas
        mainLayout = findViewById(R.id.mainLayout)

        titulo = findViewById(R.id.tituloFormulario)

        publicarbtn = findViewById(R.id.publicarbtn)
        borrarbtn = findViewById(R.id.borrarbtn)
        atrasbtn = findViewById(R.id.atrasbtn)

        nombre = findViewById(R.id.nombreEdit)
        genero = findViewById(R.id.generoEdit)
        clasificacion = findViewById(R.id.clasificacionEdit)
        duracion = findViewById(R.id.duracionEdit)
        sinopsis = findViewById(R.id.sinopsisEdit)
        director = findViewById(R.id.directorEdit)

        // 2. Inicializar Sensores
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        acelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorProximidad = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        sensorLuz = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        aceleracionActual = SensorManager.GRAVITY_EARTH
        aceleracionAnterior = SensorManager.GRAVITY_EARTH

        // 3. Listeners de Botones
        publicarbtn.setOnClickListener {
            val nuevaPelicula = Pelicula(
                nombre.text.toString(),
                genero.text.toString(),
                clasificacion.text.toString(),
                duracion.text.toString(),
                sinopsis.text.toString(),
                director.text.toString()
            )
            listaPeliculas.add(nuevaPelicula)

            val intent = Intent(this, formulario::class.java)
            startActivity(intent)
        }

        borrarbtn.setOnClickListener {
            limpiarCampos()
        }

        atrasbtn.setOnClickListener {
            val intent = Intent(this, login::class.java)
            startActivity(intent)
        }
    }

    // Función para limpiar textos
    private fun limpiarCampos() {
        nombre.text.clear()
        genero.text.clear()
        clasificacion.text.clear()
        duracion.text.clear()
        sinopsis.text.clear()
        director.text.clear()
    }

    // Función para ir al inicio (Sesiones)
    private fun irInicio() {
        val intent = Intent(this, sesiones::class.java)
        startActivity(intent)
        finish()
    }

    // Función para cambiar el tema
    private fun cambiarTema(modoOscuro: Boolean) {
        val colorTexto: Int
        val colorFondo: Int
        val colorHint: Int

        if (modoOscuro) {
            // Configuración para oscuridad (Cine)
            colorFondo = Color.parseColor("#121212") // Negro suave
            colorTexto = Color.WHITE
            colorHint = Color.LTGRAY
        } else {
            // Configuración para luz (Día)
            colorFondo = Color.WHITE
            colorTexto = Color.BLACK
            colorHint = Color.DKGRAY
        }

        // Aplicar cambios
        mainLayout.setBackgroundColor(colorFondo)
        titulo.setTextColor(colorTexto)

        // Lista de todos los campos para aplicarles el cambio en bucle
        val listaCampos = listOf(nombre, genero, clasificacion, duracion, sinopsis, director)

        for (campo in listaCampos) {
            campo.setTextColor(colorTexto)
            campo.setHintTextColor(colorHint)
        }
    }

    override fun onResume() {
        super.onResume()
        acelerometro?.also { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }
        sensorProximidad?.also { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }
        sensorLuz?.also { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        when (event.sensor.type) {
            // 1. ACELERÓMETRO (Agitar -> Borrar)
            Sensor.TYPE_ACCELEROMETER -> {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                aceleracionAnterior = aceleracionActual
                aceleracionActual = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
                val delta = aceleracionActual - aceleracionAnterior
                aceleracionTotal = aceleracionTotal * 0.9f + delta

                if (aceleracionTotal > umbralAgitacion) {
                    limpiarCampos()
                    aceleracionTotal = 0f
                }
            }

            // 2. PROXIMIDAD (Acercar -> Ir a Inicio)
            Sensor.TYPE_PROXIMITY -> {
                val distancia = event.values[0]
                if (distancia < (sensorProximidad?.maximumRange ?: 0f)) {
                    irInicio()
                }
            }

            // 3. LUZ (Cambio de ambiente -> Cambiar Colores)
            Sensor.TYPE_LIGHT -> {
                val luzActual = event.values[0]
                // Si hay menos de 10 luxes, activamos modo oscuro
                cambiarTema(luzActual < 10)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No requerido
    }
}