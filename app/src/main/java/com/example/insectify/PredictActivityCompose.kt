package com.example.insectify

import android.annotation.SuppressLint
import android.widget.Space
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu

import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
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
            topBar = {
                TopAppBar(title = {Text("Insectify")},
                    backgroundColor = colorResource(R.color.blue_light),
                    actions = {
                        IconButton(onClick = {
                            navController.navigate(route = Screen.DetailsScreen.route)
                        }) {
                            Icon(painterResource(R.drawable.ic_outline_info_24), contentDescription = "")
                        }
                    })
                     },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Spacer (
                        modifier = Modifier.weight(0.7f)
                            )
                    Row (
                       modifier = Modifier
                           .weight(4f)
                        .fillMaxWidth(),
                           horizontalArrangement = Arrangement.Center
                            ) {
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .padding(
                                    start = 40.dp,
                                    end = 40.dp,
                                    bottom = 10.dp
                                )
                                .aspectRatio(0.99f)
                                .fillMaxSize(),
                            backgroundColor = colorResource(R.color.grey)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                Icon(painter = painterResource(R.drawable.ic_outline_add_photo_alternate_24),
                                    contentDescription = "content description",
                                    modifier = Modifier.align(Alignment.Center).alpha(0.1f).fillMaxSize(0.3f)
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
                            Text(text = "Gallery ", fontSize = 15.sp)
                            Icon(painter = painterResource(R.drawable.ic_outline_photo_library_24) ,contentDescription = "content description")
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
                            Text(text = "Camera ", fontSize = 15.sp)
                            Icon(painter = painterResource(R.drawable.ic_outline_photo_camera_24),contentDescription = "content description")
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
                        Spacer(
                            modifier = Modifier.weight(0.5f)
                        )
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
                            Text(text = "Predict", fontSize = 15.sp)
                        }
                        Spacer(
                            modifier = Modifier.weight(0.5f)
                        )
                    }
                    Spacer(
                        modifier = Modifier.weight(0.3f)
                    )
                    Column (
                        modifier= Modifier
                            .weight(3f)
                            .fillMaxSize()
                            ) {
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize(),
                            text = "Prediction : 100%",
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize(),
                            text = "Prediction : 100%",
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize(),
                            text = "Prediction : 100%",
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center
                        )
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