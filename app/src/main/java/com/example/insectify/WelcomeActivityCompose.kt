package com.example.insectify

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.insectify.ui.theme.InsectifyTheme
import com.example.insectify.viewmodel.WelcomeViewModel
import com.ramcosta.composedestinations.annotation.Destination

@Destination(start = true)
@Composable
fun WelcomeActivityLayout(
    navController: NavController,
    welcomeViewModel: WelcomeViewModel = hiltViewModel()) {
    MaterialTheme {
        Column (
            modifier = Modifier
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.weight(9f))
            Row (
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                        bottom = 10.dp
                    )
            ) {
                var isClicked by remember { mutableStateOf(false) }
                val surfaceColor: Color by animateColorAsState(
                    if (isClicked)
                        colorResource(R.color.green_harsh)
                    else
                        colorResource(R.color.grey),
                    finishedListener = {
                        welcomeViewModel.saveOnBoardingState(completed = true)
                        navController.popBackStack()
                        navController.navigate(route = Screen.PredictScreen.route)
                    }
                )
                // Material Components like Button, Card, Switch, etc.
                Button(
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = surfaceColor,
                        contentColor = Color.Black),
                    onClick = {
                        isClicked = !isClicked
                    },
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    // Inner content including an icon and a text label
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(text = stringResource(R.string.get_started_button),fontSize =  15.sp)

                }
            }
        }
    }
}

/*@Composable
fun BottomButton(
    navController: NavController,
    buttonText: String,
    destinationRoute: String
) {
    MaterialTheme {
        var isClicked by remember { mutableStateOf(false) }
        val surfaceColor: Color by animateColorAsState(
            if (isClicked)
                colorResource(R.color.green_harsh)
            else
                colorResource(R.color.grey),
            finishedListener = {
                welcomeViewModel.saveOnBoardingState(completed = true)
                navController.popBackStack()
                navController.navigate(route = Screen.PredictScreen.route)
            }
        )
        // Material Components like Button, Card, Switch, etc.
                Button(
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = surfaceColor,
                        contentColor = Color.Black),
                    onClick = {
                        isClicked = !isClicked
                    },
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    // Inner content including an icon and a text label
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(text = stringResource(R.string.get_started_button),fontSize =  15.sp)

                }
    }
}*/

/*@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    InsectifyTheme {
        WelcomeActivityLayout(navController = rememberNavController())
    }
}*/
