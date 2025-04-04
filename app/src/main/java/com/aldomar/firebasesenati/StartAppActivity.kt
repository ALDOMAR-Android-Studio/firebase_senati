package com.aldomar.firebasesenati

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class StartAppActivity : AppCompatActivity() {

    private lateinit var buttonResgistry: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_start_app)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        buttonResgistry = findViewById(R.id.buttonResgistry)

        buttonResgistry.setOnClickListener {
            val intent = Intent(this, RegistryActivity::class.java)
            Toast.makeText(this, "Registro", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }

    }
}