package com.example.insectify

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.insectify.ui.theme.InsectifyTheme

@Composable
fun PreditButton() {

    Button(
        shape = RoundedCornerShape(15.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = surfaceColor,
            contentColor = Color.Black
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    )

}

@Preview(showBackground = true)
@Composable
fun PredictScreenPreview() {
    InsectifyTheme() {

        PreditButton()

    }
}