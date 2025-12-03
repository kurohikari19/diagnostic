package com.example.diagnostic.cartelera

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.diagnostic.R

class sesiones : AppCompatActivity() {

    private lateinit var adminButton: Button
    private lateinit var userButton: Button
    private lateinit var exitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sesiones)

        adminButton = findViewById(R.id.adminButton)
        userButton = findViewById(R.id.userButton)
        exitButton = findViewById(R.id.exitButton)


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
}