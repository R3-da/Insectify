package com.example.insectify

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.insectify.ui.theme.InsectifyTheme
import com.ramcosta.composedestinations.annotation.Destination

@Destination(start = true)
@Composable
fun WelcomeActivityLayout(navController: NavController) {
    MaterialTheme {
        ConstraintLayout (
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
        ) {
            val (iconBox, bottomButton) = createRefs()
            Box(
                modifier = Modifier
                    .constrainAs(iconBox) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
            ) {
                // Create references for the composable to constrain

            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 30.dp,
                        end = 30.dp,
                        top = 20.dp,
                        bottom = 20.dp
                    )
                    .constrainAs(bottomButton) {
                        bottom.linkTo(parent.bottom)
                    },
                horizontalArrangement = Arrangement.Center
            ) {
                BottomButton(navController,
                    buttonText = "GET STARTED",
                    Screen.LanguageScreen.route)
            }
        }
    }
}

@Composable
fun BottomButton(
    navController: NavController,
    buttonText: String,
    destinationRoute: String
) {
    MaterialTheme {
        var isClicked by remember { mutableStateOf(false) }
        val surfaceColor: Color by animateColorAsState(
            if (isClicked) colorResource(R.color.green_harsh) else colorResource(R.color.grey),
        )
        // Material Components like Button, Card, Switch, etc.
                Button(
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = surfaceColor,
                        contentColor = Color.Black),
                    onClick = {
                        isClicked = !isClicked
                        navController.navigate(route = destinationRoute)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(65.dp)

                ) {
                    // Inner content including an icon and a text label
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(text = buttonText,fontSize =  25.sp)

                }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    InsectifyTheme {
    }
}
