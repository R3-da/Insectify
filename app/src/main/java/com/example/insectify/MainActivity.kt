package com.example.insectify

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.insectify.ui.theme.InsectifyTheme

class MainActivity : AppCompatActivity() {
    lateinit var navController: NavHostController
    override fun onCreate(savedInstantState: Bundle?) {
        super.onCreate(savedInstantState)
        setContent {
            InsectifyTheme() {
                navController = rememberNavController()
                SetupNavGraph(navController = navController)
            }
        }
    }

}