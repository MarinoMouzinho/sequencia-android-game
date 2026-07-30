package com.example.sequenciagame

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/*
* 5× value 2 cards
* 6× value 3 cards
* 6× value 4 cards
* 6× value 5 cards
* 6× value 6 cards
* 5× value 7 cards
* 4× value 8 cards
* 3× value 9 cards
* 3× value 10 cards
* 3× value 11 cards
* 3× value 12 cards
* 3× Wild Cards
* 2× Skip Cards
* 2× Reverse Cards
* */

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
    }
}