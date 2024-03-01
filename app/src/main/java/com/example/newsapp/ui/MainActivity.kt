package com.example.newsapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.newsapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val newsNavHostFragment = supportFragmentManager.findFragmentById(R.id.newsNavHostFragment)
        val navController = newsNavHostFragment?.findNavController()

        navController?.let {
            bottomNavigationView?.setupWithNavController(it)
        }

    }
}