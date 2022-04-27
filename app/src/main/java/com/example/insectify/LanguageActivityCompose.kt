package com.example.insectify

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.insectify.ui.theme.InsectifyTheme
import com.ramcosta.composedestinations.annotation.Destination

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Destination
@Composable
fun LanguageActivityLayout(
    navigator: NavController,
    languageLabels: List<String> = listOf("English","French","Arabic")
) {
    MaterialTheme {
        Scaffold(
            topBar = { TopAppBar(title = {Text("Language")},backgroundColor = colorResource(R.color.blue_light))  },
            drawerContent = { Text(text = "Drawer Menu 1") },
            content = {
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
                        Column {
                            for (languageLabel in languageLabels) {
                                Row (
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            top = 20.dp,
                                            bottom = 20.dp
                                        ),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    LanguageButton(languageLabel = languageLabel)
                                }
                            }
                        }
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
                            .constrainAs(bottombutton) {
                                bottom.linkTo(parent.bottom)
                            },
                        horizontalArrangement = Arrangement.Center
                    ) {
                        BottomButton(
                            navigator,
                            buttonText = "CONTINUE",
                            Screen.PredictScreen.route)
                    }
                }
                      }
        )

    }
}

@Composable
fun LanguageButton(languageLabel: String) {
    MaterialTheme {
        var isClicked by remember { mutableStateOf(false) }
        val surfaceColor: Color by animateColorAsState(
            if (isClicked) colorResource(R.color.blue_light) else colorResource(R.color.grey) ,
        )
        // Material Components like Button, Card, Switch, etc.
            Button(
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = surfaceColor,
                    contentColor = Color.Black),
                onClick = {
                    isClicked = !isClicked
                },
                modifier = Modifier
                    .width(150.dp)
                    .height(65.dp)

            ) {
                // Inner content including an icon and a text label
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(text = "$languageLabel",fontSize = 25.sp)
            }
    }
}

@Preview(showBackground = true)
@Composable
fun LanguageScreenPreview() {
    InsectifyTheme {
        LanguageActivityLayout(navigator = rememberNavController())
    }
}