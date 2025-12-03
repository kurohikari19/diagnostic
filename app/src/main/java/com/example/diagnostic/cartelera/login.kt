package com.example.diagnostic.cartelera

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.diagnostic.R
import android.content.Intent
import android.widget.Button
import android.widget.EditText

class login : AppCompatActivity() {

    private lateinit var ingresarbtn: Button
    private lateinit var atrasbtn: Button
    private lateinit var passwdEdit: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        ingresarbtn = findViewById(R.id.ingresarbtn)
        atrasbtn = findViewById(R.id.atrasbtn)
        passwdEdit = findViewById(R.id.passwdEdit)

        ingresarbtn.setOnClickListener {
            val password = passwdEdit.text.toString()

            if (password == "1234") {
                val intent = Intent(this, formulario::class.java)
                startActivity(intent)
            } else {
                passwdEdit.error = "Contraseña incorrecta"
            }
        }

        atrasbtn.setOnClickListener {
            val intent = Intent(this, sesiones::class.java)
            startActivity(intent)
        }
    }
}