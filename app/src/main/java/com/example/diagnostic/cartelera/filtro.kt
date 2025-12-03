package com.example.diagnostic.cartelera

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.diagnostic.R

// No necesitas importar esto si no lo usas directamente
// import androidx.core.view.ViewCompat
// import androidx.core.view.WindowInsetsCompat

class filtro : AppCompatActivity() {

    private lateinit var filtroEdit: EditText
    private lateinit var buscarBtn: Button
    private lateinit var atrasbtn: Button

    private lateinit var nombreView: TextView
    private lateinit var generoView: TextView
    private lateinit var clasificacionView: TextView
    private lateinit var duracionView: TextView
    private lateinit var sinopsisView: TextView
    private lateinit var directorView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_filtro)

        // Inicialización de vistas
        filtroEdit = findViewById(R.id.filtroEdit)
        buscarBtn = findViewById(R.id.buscarbtn)
        atrasbtn = findViewById(R.id.atrasbtn)

        nombreView = findViewById(R.id.nombreView)
        generoView = findViewById(R.id.generoView)
        clasificacionView = findViewById(R.id.clasificacionView)
        duracionView = findViewById(R.id.duracionView)
        sinopsisView = findViewById(R.id.sinopsisView)
        directorView = findViewById(R.id.directorView)

        // Listeners de los botones
        buscarBtn.setOnClickListener {
            buscarPelicula()
        }

        atrasbtn.setOnClickListener {
            val intent = Intent(this, sesiones::class.java)
            startActivity(intent)
        }



    }

    private fun buscarPelicula() {
        val nombreBuscado = filtroEdit.text.toString().trim()

        if (nombreBuscado.isEmpty()) {
            nombreView.text = "Por favor, ingresa un nombre para buscar."
            generoView.text = ""
            clasificacionView.text = ""
            duracionView.text = ""
            sinopsisView.text = ""
            directorView.text = ""
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
}