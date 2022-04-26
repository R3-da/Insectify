package com.example.insectify

import android.os.Bundle
import android.window.SplashScreen
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.insectify.ui.theme.InsectifyTheme
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import java.time.chrono.JapaneseEra.values

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavHostController
    override fun onCreate(savedInstantState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstantState)
        setContent {
            InsectifyTheme {
                navController = rememberNavController()
                SetupNavGraph(navController = navController)
            }
        }
    }
}