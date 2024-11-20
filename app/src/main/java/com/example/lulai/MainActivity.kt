package com.example.lulai

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lulai.modules.authentication.LoginView
import com.example.lulai.modules.authentication.SignUpView
import com.example.lulai.ui.theme.LulAITheme
import com.example.lulai.services.UnifiedLocationManager
import com.example.lulai.navigation.*

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val unifiedLocationManager = UnifiedLocationManager(this) // Initialize location manager
        val sharedPreferences: SharedPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        Log.d("MainActivity", "Is user logged in? $isLoggedIn") // Debug log

        setContent {
            LulAITheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = if (!isLoggedIn) "home" else "welcome" // Fix logic
                ) {
                    composable("welcome") {
                        WelcomeView(navController)
                    }
                    composable("login") {
                        LoginView(navController) {
                            sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
                            navController.navigate("home") {
                                popUpTo("welcome") { inclusive = true }
                            }
                        }
                    }
                    composable("signUp") {
                        SignUpView(navController, unifiedLocationManager)
                    }
                    composable("home") {
                        TabBarView(navController)
                    }
                }
            }
        }
    }
}
