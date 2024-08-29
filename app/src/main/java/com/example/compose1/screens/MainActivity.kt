package com.example.compose1.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Change the status bar color
        window.statusBarColor = Color.Transparent.toArgb()
        // Set light or dark status bar icons
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars =
            true // or false

        setContent {
            MyApp()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login")
    {
        composable("register") { ShowRegistrationPage(navController) }
        composable("login") { ShowLoginPage(navController) }
        composable("home/{userEmail}") { backStackEntry ->
            val userEmail = backStackEntry.arguments?.getString("userEmail")
            ShowHomePage(navController, userEmail.toString())
        }
    }
}



