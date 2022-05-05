package com.example.insectify

import android.annotation.SuppressLint
import android.util.Log
import android.view.textclassifier.TextLanguage
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
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
    languageLabels: List<String> = listOf("English","Français","العربية")
) {
    var selectedLanguage by remember {mutableStateOf("")}

    when (selectedLanguage) {
        "English" -> print("English")
        "Français" -> print("Français")
        "العربية" -> print("العربية")
        else -> { // Note the block
            print("default")
        }
    }

    MaterialTheme {
        Scaffold(
            topBar = { TopAppBar(title = {Text(stringResource(R.string.language_top_bar))},backgroundColor = colorResource(R.color.blue_light))  },
            content = {
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Spacer(modifier = Modifier.weight(2.5f))
                        // Create references for the composables to constrain
                    for (languageLabel in languageLabels) {
                        Spacer(modifier = Modifier.weight(0.25f))
                        Row (
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize()
                                .padding(
                                    start = 20.dp,
                                    end = 20.dp,
                                    bottom = 10.dp
                                ),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Spacer(modifier = Modifier.weight(0.5f))
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                            ) {
                                LanguageButton(languageLabel = languageLabel,selectedLanguage){
                                    selectedLanguage = it
                                }
                            }
                            Spacer(modifier = Modifier.weight(0.5f))
                        }
                    }
                    Spacer(modifier = Modifier.weight(0.25f))
                    Spacer(modifier = Modifier.weight(2.5f))
                    Row (
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .padding(
                                start = 20.dp,
                                end = 20.dp,
                                bottom = 10.dp
                            )
                    ) {
                        BottomButton(
                            navigator,
                            buttonText = stringResource(id = R.string.continue_button) ,
                            Screen.PredictScreen.route)
                    }
                }
            }
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun LanguageButton(languageLabel: String, selectedLanguage: String,languageChange: (String) -> Unit){
    MaterialTheme {
        val surfaceColor: Color by animateColorAsState(
            if (selectedLanguage == languageLabel) colorResource(R.color.blue_light) else colorResource(R.color.grey),
        )
        // Material Components like Button, Card, Switch, etc.
        Button(
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = surfaceColor,
                contentColor = Color.Black),
            onClick = {
                languageChange(languageLabel)
            },
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(text = "$languageLabel",fontSize = 15.sp)
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