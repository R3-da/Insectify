package com.example.insectify

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun SetupNavGraph(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = Screen.WelcomeScreen.route) {
        composable(
            route = Screen.WelcomeScreen.route
        ) {
            WelcomeActivityLayout(navController)
        }
        composable(
            route = Screen.PredictScreen.route
        ) {
            PredictLayout(navController)
        }
        composable(
            route = Screen.DetailsScreen.route
        ) {
            DetailsLayout(navController)
        }
    }
}