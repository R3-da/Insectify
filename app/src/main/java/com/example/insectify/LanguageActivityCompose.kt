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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.insectify.ui.theme.InsectifyTheme

class LanguageActivityCompose : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InsectifyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting2("Android")
                }
            }
        }
    }
}

@Composable
fun LanguageLayout() {
    MaterialTheme {
        Row () {
            LanguageButton(languageLabel = "English")
            BottomButton(buttonText = "CONTINUE")
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
        Column(
            modifier = Modifier
                .background(Color(0xFF7BB661))
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
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
}

@Composable
fun Greeting2(name: String) {
    Text(text = "Hello 2 $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    InsectifyTheme {
        LanguageLayout()
    }
}