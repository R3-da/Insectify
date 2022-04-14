package com.example.insectify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.insectify.ui.theme.InsectifyTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun LanguageActivityLayout(
    navigator: NavController,
    languageLabels: List<String> = listOf("English","French","Arabic")
) {
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
                Column(
                    modifier = Modifier.background(Color(0xFF7BB661))
                ) {
                    for (languageLabel in languageLabels) {
                        Row (
                            modifier = Modifier
                                .background(Color(0xFF7FF661))
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            LanguageButton(languageLabel = languageLabel)
                        }
                    }
                }
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
                BottomButton(
                    navigator,
                    buttonText = "CONTINUE",
                    "")
            }
        }
    }
}

@Composable
fun LanguageButton(languageLabel: String) {
    MaterialTheme {
        var isClicked by remember { mutableStateOf(false) }
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
                },
                modifier = Modifier
                    .width(200.dp)
                    .height(60.dp)

            ) {
                // Inner content including an icon and a text label
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(text = "$languageLabel",fontSize = 30.sp)
            }
        }
    }
}

@Composable
fun TestButtonChangeColor(colorName: String) {
    MaterialTheme {
        Box (
            Modifier.padding(
                start = 30.dp,
                end = 30.dp,
                top = 20.dp,
                bottom = 20.dp
            )
        ) {
            TestButton("buttonName")

        }
    }
}

@Composable
fun TestButton(buttonName: String) {
    MaterialTheme {
        var clickNum by remember {mutableStateOf(0)}
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Red,
                contentColor = Color.Black),
            onClick = {
                clickNum++
            }
        ) {
            Text(text = "${buttonName}",fontSize = 30.sp)
        }
    }
}

@Composable
fun Greeting2(name: String) {
    Text(text = "Hello 2 $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    InsectifyTheme {
        TestButtonChangeColor("Button Name")
    }
}