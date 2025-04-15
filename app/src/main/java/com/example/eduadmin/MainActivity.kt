package com.example.eduadmin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.eduadmin.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigation()

    }

    private fun setupNavigation() {
        val navHostFragment= supportFragmentManager.findFragmentById(R.id.fragmentContainerViewStudent) as NavHostFragment
        val navController= navHostFragment.navController
        binding.bnbStudent.setupWithNavController(navController)
        binding.bnbStudent.setOnItemSelectedListener { menuItem ->
            if (navController.currentDestination?.id != menuItem.itemId) {
                navController.navigate(menuItem.itemId)
            }
            true
        }
    }
}