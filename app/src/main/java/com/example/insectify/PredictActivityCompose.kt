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
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    val (imageField, uploadRow, predictButton) = createRefs()

                    Card(
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .padding(
                                start = 40.dp,
                                end = 40.dp
                            )
                            .fillMaxWidth()
                            .aspectRatio(0.99f)
                            .constrainAs(imageField) {
                                top.linkTo(parent.top, margin = 70.dp)
                            },
                        backgroundColor = colorResource(R.color.grey),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Text(
                                "Upload Image",
                                Modifier.padding(16.dp)
                                    .align(Alignment.Center),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                        Row(
                            modifier = Modifier
                                .padding(
                                    top = 40.dp,
                                    start = 20.dp,
                                    end = 20.dp,
                                )
                                .height(65.dp)
                                .fillMaxWidth()
                                .constrainAs(uploadRow) {
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    top.linkTo(imageField.bottom)
                                },
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
                                    .height(65.dp),
                                onClick = {
                                }
                            ) {
                                Text(text = "Upload", fontSize = 30.sp)
                            }
                            Button(
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = colorResource(R.color.blue_light),
                                    contentColor = Color.Black
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(65.dp),
                                onClick = {
                                }
                            ) {
                                Text(text = "Camera", fontSize = 30.sp)
                            }
                        }
                        Row(
                            modifier = Modifier
                                .padding(
                                    top = 40.dp,
                                    start = 20.dp,
                                    end = 20.dp,
                                )
                                .height(65.dp)
                                .fillMaxWidth()
                                .constrainAs(predictButton) {
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    top.linkTo(uploadRow.bottom)
                                },
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
                                    .height(65.dp),
                                onClick = {
                                }
                            ) {
                                Text(text = "Predict", fontSize = 30.sp)
                            }
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