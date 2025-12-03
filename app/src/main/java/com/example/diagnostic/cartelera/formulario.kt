package com.example.diagnostic.cartelera

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.diagnostic.R
import android.widget.EditText

class formulario : AppCompatActivity() {

    private lateinit var publicarbtn: Button
    private lateinit var borrarbtn: Button
    private lateinit var atrasbtn: Button

    private lateinit var nombre: EditText
    private lateinit var genero: EditText
    private lateinit var clasificacion: EditText
    private lateinit var duracion: EditText
    private lateinit var sinopsis: EditText
    private lateinit var director: EditText

    companion object {
        val listaPeliculas = mutableListOf<Pelicula>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_formulario)

        publicarbtn = findViewById(R.id.publicarbtn)
        borrarbtn = findViewById(R.id.borrarbtn)
        atrasbtn = findViewById(R.id.atrasbtn)

        nombre = findViewById(R.id.nombreEdit)
        genero = findViewById(R.id.generoEdit)
        clasificacion = findViewById(R.id.clasificacionEdit)
        duracion = findViewById(R.id.duracionEdit)
        sinopsis = findViewById(R.id.sinopsisEdit)
        director = findViewById(R.id.directorEdit)

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
            nombre.text.clear()
            genero.text.clear()
            clasificacion.text.clear()
            duracion.text.clear()
            sinopsis.text.clear()
            director.text.clear()
        }

        atrasbtn.setOnClickListener {
            val intent = Intent(this, sesiones::class.java)
            startActivity(intent)
        }

    }
}