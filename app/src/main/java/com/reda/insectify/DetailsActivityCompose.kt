package com.reda.insectify

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                LazyColumn {
                    item {
                        titleItem("App", true)
                    }

                   item {
                       listItem(R.drawable.ic_logos_tensorflow, "Model Description",
                           "This app uses mobilnet pretrained model which is tweaked to match our app's needs, the database covers 291 insect types.")
                   }

                    item {
                        listItem(R.drawable.ic_iconoir_precision_tool,
                            "Accuracy",
                            "55.78%")
                    }

                    item {
                        listItem(R.drawable.ic_mdi_target_variant,
                            "Loss",
                            "2.7908")
                    }

                    item {
                        listItem(R.drawable.ic_clarity_alert_line,
                            "About",
                            "This app should only be used as assistance and not as a concrete scientific reference.",
                        true)
                    }

                    item {
                        titleItem("Credits")
                    }

                    item {
                        listItem(R.drawable.ic_univ,
                            "Supervised By",
                            "Computer science, networks and multimedia 2021-2022 professors, faculty of science and techniques mohammedia, university of hassan 2 casablanca.")
                    }

                    item {
                        listItem(R.drawable.ic_zondicons_book_reference,
                            "Reference Github Repositories",
                            "Implement model : https://www.youtube.com/watch?v=s_XOVkjXQbU.\nCapture and import image : https://github.com/MakeItEasyDev/Jetpack-Compose-Capture-Image-Or-Choose-from-Gallery.")
                    }

                    item {
                        listItem(R.drawable.ic_ant_design_database_outlined,
                            "Database",
                            "The database is provided by : https://www.kaggle.com/datasets/kmldas/insect-identification-from-habitus-image.\nFor more info about insects visit : https://www.gbif.org/species.")
                    }

                    item {
                        listItem(R.drawable.ic_twotone_developer_mode,
                            "Developed By",
                            "R3da.",
                        true)
                    }
                }
            }
        )
    }
}

@Composable
fun listItem(icon : Int, text : String, subText : String?, isLast : Boolean = false) {
    Row (
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Image(
            modifier = Modifier.weight(1f).padding(start = 14.dp, end = 14.dp),
            painter = painterResource(icon),
            contentDescription = "content description"
        )
        Column (modifier = Modifier.weight(5f)) {

            Text(
                text = text,
                modifier = Modifier
                .fillMaxWidth(),
                maxLines = 1,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            subText?.let{
                Text(text = subText, modifier = Modifier.fillMaxWidth(), fontSize = 13.sp, style = MaterialTheme.typography.body2)
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                )
            }
            if (!isLast) {
                Divider(
                    color = colorResource(R.color.grey),
                    modifier = Modifier
                        .fillMaxWidth(),
                    thickness = 0.5f.dp
                )
            }
        }
    }
}

@Composable
fun titleItem(title : String, isFirst : Boolean = false) {
    if (!isFirst) {
        Divider(
            color = colorResource(R.color.grey),
            modifier = Modifier
                .fillMaxWidth(),
            thickness = 0.5f.dp
        )
    }
    Text(modifier = Modifier
        .fillMaxWidth()
        .padding(
            start = 20.dp,
            top = 10.dp
        ),
        text = title,
    fontSize = 13.sp,
    fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier
        .fillMaxWidth()
        .height(9.dp)
    )
}


