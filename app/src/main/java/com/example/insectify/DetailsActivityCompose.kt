package com.example.insectify

import android.annotation.SuppressLint
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DetailsLayout(navController: NavController) {
    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = {},
                    backgroundColor = colorResource(R.color.blue_light),
                    navigationIcon = {
                        // show drawer icon
                        IconButton(
                            onClick = {
                                navController.backQueue.clear()
                                navController.navigate(route = Screen.PredictScreen.route)
                            }
                        ) {
                            Icon(painterResource(R.drawable.ic_baseline_arrow_back_24), contentDescription = "")
                        }
                    })
            },
            content = {
                Text(text = "Hello World !")
            }
        )
    }
}
