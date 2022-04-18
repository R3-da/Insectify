package com.example.insectify

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.insectify.ui.theme.InsectifyTheme


@Composable
fun PredictLayout(navController: NavController) {
    MaterialTheme{
        ConstraintLayout (
            modifier = Modifier.fillMaxSize()
                ){
            val (imageField, predictButton) = createRefs()

            Box (
                modifier = Modifier
                    .padding(
                        start = 30.dp,
                        end = 30.dp
                    )
                    .constrainAs(imageField) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    ) {
                Box (
                    modifier = Modifier
                        .background(Color(0xFF7BB661))
                        .fillMaxWidth()
                        .aspectRatio(1f)

                )
            }


            Row (
                modifier = Modifier
                    .background(Color(0xFF7FF661))
                    .fillMaxWidth()
                    .constrainAs(predictButton){
                        bottom.linkTo(parent.bottom)
                    },
                horizontalArrangement = Arrangement.Center
                    ) {
                        PredictButton()
            }
        }
    }
}

@Composable
fun PredictButton() {
    MaterialTheme{
        var isClicked by remember  { mutableStateOf(false)}
        val surfaceColor: Color by animateColorAsState(
            if (isClicked) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
        )

        Box(
            modifier = Modifier.padding(
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
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                onClick = {
                    isClicked = !isClicked
                }
            ) {
                    Text(text = "Predict", fontSize = 30.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PredictScreenPreview() {
    InsectifyTheme() {

        PredictLayout(navController = rememberNavController())

    }
}