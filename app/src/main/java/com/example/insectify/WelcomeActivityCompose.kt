package com.example.insectify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.insectify.destinations.LanguageActivityLayoutDestination
import com.example.insectify.destinations.WelcomeActivityLayoutDestination
import com.example.insectify.ui.theme.InsectifyTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(start = true)
@Composable
fun WelcomeActivityLayout(navController: NavController) {
    MaterialTheme {
        ConstraintLayout (
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
        ) {
            val (languagebuttons, bottombutton) = createRefs()
            Box(
                modifier = Modifier
                    .constrainAs(languagebuttons) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
            ) {
                // Create references for the composables to constrain

            }
            Row (
                modifier = Modifier
                    .background(Color(0xFF7FF661))
                    .fillMaxWidth()
                    .constrainAs(bottombutton) {
                        bottom.linkTo(parent.bottom)
                    },
                horizontalArrangement = Arrangement.Center
            ) {
                BottomButton(navController,
                    buttonText = "GET STARTED",
                    "LanguageActivityLayoutDestination")
            }
        }
    }
}

@Composable
fun BottomButton(
    navController: NavController,
    buttonText: String,
    destinationClassName: String
) {
    MaterialTheme {
        var isClicked by remember { mutableStateOf(false) }
        var firstClicked = false
        val surfaceColor: Color by animateColorAsState(
            if (isClicked) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
        )
        // Material Components like Button, Card, Switch, etc.
            Box(
                Modifier.padding(
                    start = 30.dp,
                    end = 30.dp,
                    top = 20.dp,
                    bottom = 20.dp
                )
            ) {
                Button(
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = surfaceColor,
                        contentColor = Color.Black),
                    onClick = {
                        isClicked = !isClicked
                        navController.navigate(route = Screen.LanguageScreen.route)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)

                ) {
                    // Inner content including an icon and a text label
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(text = "$buttonText",fontSize = 30.sp)
                }
            }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello 1 $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    InsectifyTheme {
    }
}
