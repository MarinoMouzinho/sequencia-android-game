package com.example.sequenciagame.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sequenciagame.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.btnNewGame).setOnClickListener {
            //TODO: Otimizar abrir Game Activity
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnConfigGame).setOnClickListener {
            // TODO: Chamar Modal Config Game
        }

        findViewById<Button>(R.id.btnConfigProfile).setOnClickListener {
            // TODO: Chamar Modal Config Profile
        }

        findViewById<Button>(R.id.btnExit).setOnClickListener {
            finish()
        }
    }
}