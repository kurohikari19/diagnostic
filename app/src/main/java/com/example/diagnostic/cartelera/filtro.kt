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

class filtro : AppCompatActivity(), SensorEventListener {

    private lateinit var filtroEdit: EditText
    private lateinit var buscarBtn: Button
    private lateinit var atrasbtn: Button

    // Referencias para cambiar colores (Tema Oscuro)
    private lateinit var mainLayout: View
    private lateinit var tituloFiltro: TextView

    private lateinit var nombreView: TextView
    private lateinit var generoView: TextView
    private lateinit var clasificacionView: TextView
    private lateinit var duracionView: TextView
    private lateinit var sinopsisView: TextView
    private lateinit var directorView: TextView

    // Variables de Sensores
    private lateinit var sensorManager: SensorManager
    private var acelerometro: Sensor? = null
    private var sensorProximidad: Sensor? = null
    private var sensorLuz: Sensor? = null

    // Variables del Acelerómetro
    private var aceleracionActual = 0f
    private var aceleracionAnterior = 0f
    private var aceleracionTotal = 0f
    private val umbralAgitacion = 14

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_filtro)

        // Inicialización de vistas UI
        mainLayout = findViewById(R.id.main)
        tituloFiltro = findViewById(R.id.tituloFiltro)

        filtroEdit = findViewById(R.id.filtroEdit)
        buscarBtn = findViewById(R.id.buscarbtn)
        atrasbtn = findViewById(R.id.atrasbtn)

        nombreView = findViewById(R.id.nombreView)
        generoView = findViewById(R.id.generoView)
        clasificacionView = findViewById(R.id.clasificacionView)
        duracionView = findViewById(R.id.duracionView)
        sinopsisView = findViewById(R.id.sinopsisView)
        directorView = findViewById(R.id.directorView)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        acelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorProximidad = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        sensorLuz = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        aceleracionActual = SensorManager.GRAVITY_EARTH
        aceleracionAnterior = SensorManager.GRAVITY_EARTH

        buscarBtn.setOnClickListener {
            buscarPelicula()
        }

        atrasbtn.setOnClickListener {
            val intent = Intent(this, login2::class.java)
            startActivity(intent)
        }
    }

    private fun buscarPelicula() {
        val nombreBuscado = filtroEdit.text.toString().trim()

        if (nombreBuscado.isEmpty()) {
            limpiarBusqueda() //
            nombreView.text = "Por favor, ingresa un nombre para buscar."
            return
        }

        var peliculaEncontrada: Pelicula? = null

        for (pelicula in formulario.listaPeliculas) {
            if (pelicula.nombre.equals(nombreBuscado, ignoreCase = true)) {
                peliculaEncontrada = pelicula
                break
            }
        }

        if (peliculaEncontrada != null) {
            nombreView.text = "Nombre: ${peliculaEncontrada.nombre}"
            generoView.text = "Género: ${peliculaEncontrada.genero}"
            clasificacionView.text = "Clasificación: ${peliculaEncontrada.clasificacion}"
            duracionView.text = "Duración: ${peliculaEncontrada.duracion}"
            sinopsisView.text = "Sinopsis: ${peliculaEncontrada.sinopsis}"
            directorView.text = "Director: ${peliculaEncontrada.director}"

        } else {
            nombreView.text = "No se encontró ninguna película llamada '$nombreBuscado'."
            generoView.text = ""
            clasificacionView.text = ""
            duracionView.text = ""
            sinopsisView.text = ""
            directorView.text = ""
        }
    }

    // 1. Función para limpiar (Acelerómetro)
    private fun limpiarBusqueda() {
        filtroEdit.text.clear()
        nombreView.text = ""
        generoView.text = ""
        clasificacionView.text = ""
        duracionView.text = ""
        sinopsisView.text = ""
        directorView.text = ""
    }

    // 2. Función para navegar atrás (Proximidad)
    private fun irInicio() {
        val intent = Intent(this, sesiones::class.java)
        startActivity(intent)
        finish()
    }

    // 3. Función para cambiar tema (Luz)
    private fun cambiarTema(modoOscuro: Boolean) {
        val colorTexto: Int
        val colorFondo: Int
        val colorHint: Int

        if (modoOscuro) {
            colorFondo = Color.parseColor("#121212") // Negro
            colorTexto = Color.WHITE
            colorHint = Color.LTGRAY
        } else {
            colorFondo = Color.WHITE
            colorTexto = Color.BLACK
            colorHint = Color.DKGRAY
        }

        mainLayout.setBackgroundColor(colorFondo)
        tituloFiltro.setTextColor(colorTexto)

        filtroEdit.setTextColor(colorTexto)
        filtroEdit.setHintTextColor(colorHint)

        val listaResultados = listOf(nombreView, generoView, clasificacionView, duracionView, sinopsisView, directorView)
        for (vista in listaResultados) {
            vista.setTextColor(colorTexto)
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
            // ACELERÓMETRO: Agitar para borrar búsqueda
            Sensor.TYPE_ACCELEROMETER -> {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                aceleracionAnterior = aceleracionActual
                aceleracionActual = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
                val delta = aceleracionActual - aceleracionAnterior
                aceleracionTotal = aceleracionTotal * 0.9f + delta

                if (aceleracionTotal > umbralAgitacion) {
                    limpiarBusqueda()
                    aceleracionTotal = 0f
                }
            }

            // PROXIMIDAD: Acercar para volver atrás
            Sensor.TYPE_PROXIMITY -> {
                val distancia = event.values[0]
                if (distancia < (sensorProximidad?.maximumRange ?: 0f)) {
                    irInicio()
                }
            }

            // LUZ: Cambiar colores
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