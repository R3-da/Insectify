package com.example.insectify

sealed class Screen (val route:String) {
    object WelcomeScreen: Screen(route = "welcome_screen")
    object PredictScreen: Screen(route = "predict_screen")
    object DetailsScreen: Screen(route = "details_screen")
}
