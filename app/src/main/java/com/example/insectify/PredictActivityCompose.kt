package com.example.insectify

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.insectify.ui.theme.InsectifyTheme


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun PredictLayout(navController: NavController) {
    MaterialTheme{
        Scaffold(
            topBar = { TopAppBar(title = {Text("Insectify")},backgroundColor = colorResource(R.color.blue_light))  },
            drawerContent = { Text(text = "Drawer Menu 1") },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Row (
                        modifier = Modifier.weight(1f)
                            ) {

                    }
                    Row (
                       modifier = Modifier
                           .weight(4f)
                            ) {
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .padding(
                                    start = 40.dp,
                                    end = 40.dp,
                                    bottom = 10.dp
                                )
                                .fillMaxSize(),
                            backgroundColor = colorResource(R.color.grey)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                Text(
                                    "Upload Image",
                                    Modifier.padding(20.dp)
                                        .align(Alignment.Center),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .padding(
                                start = 20.dp,
                                end = 20.dp,
                                bottom = 10.dp
                            )
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = colorResource(R.color.blue_light),
                                contentColor = Color.Black
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            onClick = {
                            }
                        ) {
                            Text(text = "Upload", fontSize = 20.sp)
                        }
                        Button(
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = colorResource(R.color.blue_light),
                                contentColor = Color.Black
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            onClick = {
                            }
                        ) {
                            Text(text = "Camera", fontSize = 20.sp)
                        }
                    }
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .padding(
                                start = 20.dp,
                                end = 20.dp,
                                bottom = 10.dp
                            )
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = colorResource(R.color.green_harsh),
                                contentColor = Color.Black
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            onClick = {
                            }
                        ) {
                            Text(text = "Predict", fontSize = 20.sp)
                        }
                    }
                    Row (
                        modifier= Modifier.weight(3f)
                            ) {

                    }
                }
            }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PredictScreenPreview() {
    InsectifyTheme() {
        PredictLayout(navController = rememberNavController())
    }
}