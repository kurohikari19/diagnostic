package com.example.diagnostic.cartelera

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.diagnostic.R

class login2 : AppCompatActivity() {

    private lateinit var ingresarbtn2: Button
    private lateinit var atrasbtn2: Button
    private lateinit var passwdEdit2: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login2)

        ingresarbtn2 = findViewById(R.id.ingresarbtn2)
        atrasbtn2 = findViewById(R.id.atrasbtn2)
        passwdEdit2 = findViewById(R.id.passwdEdit2)


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
}