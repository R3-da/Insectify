package com.reda.insectify

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DetailsLayout() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            TitleItem("App", true)
        }

        item {
            ListItem(
                R.drawable.ic_logos_tensorflow, "Model Description",
                "This app uses mobilenet pretrained model which is tweaked to match our app's needs, the database covers 291 insect types."
            )
        }

        item {
            ListItem(
                R.drawable.ic_iconoir_precision_tool,
                "Accuracy",
                "55.78%"
            )
        }

        item {
            ListItem(
                R.drawable.ic_mdi_target_variant,
                "Loss",
                "2.7908"
            )
        }

        item {
            ListItem(
                R.drawable.ic_clarity_alert_line,
                "About",
                "This app should only be used as assistance and not as a concrete scientific reference.",
                true
            )
        }

        item {
            TitleItem("Credits")
        }

        item {
            ListItem(
                R.drawable.ic_univ,
                "Supervised By",
                "Computer science, networks and multimedia 2021-2022 professors, faculty of science and techniques mohammedia, university of hassan 2 casablanca."
            )
        }

        item {
            ListItem(
                R.drawable.ic_zondicons_book_reference,
                "Reference Github Repositories",
                "Implement model : https://www.youtube.com/watch?v=s_XOVkjXQbU.\nCapture and import image : https://github.com/MakeItEasyDev/Jetpack-Compose-Capture-Image-Or-Choose-from-Gallery."
            )
        }

        item {
            ListItem(
                R.drawable.ic_ant_design_database_outlined,
                "Database",
                "The database is provided by : https://www.kaggle.com/datasets/kmldas/insect identification-from-habitus-image.\nFor more info about insects visit : https://www.gbif.org/species."
            )
        }

        item {
            ListItem(
                R.drawable.ic_twotone_developer_mode,
                "Developed By",
                "R3da.",
                true
            )
        }
    }
}

@Composable
fun ListItem(icon: Int, text: String, subText: String?, isLast: Boolean = false) {
    val uriHandler = LocalUriHandler.current
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(24.dp).padding(top = 4.dp),
            tint = colorResource(R.color.blue_light)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = text,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            subText?.let {
                ClickableTextWithLinks(it, uriHandler)
            }
            if (!isLast) {
                Spacer(modifier = Modifier.height(16.dp))
                Divider(
                    color = colorResource(R.color.grey).copy(alpha = 0.5f),
                    thickness = 0.5.dp
                )
            }
        }
    }
}

@Composable
fun ClickableTextWithLinks(text: String, uriHandler: androidx.compose.ui.platform.UriHandler) {
    val urlPattern = Regex("https?://[^\\s]+")
    val annotatedString = buildAnnotatedString {
        var lastIndex = 0
        urlPattern.findAll(text).forEach { matchResult ->
            val url = matchResult.value
            append(text.substring(lastIndex, matchResult.range.first))
            
            pushStringAnnotation(tag = "URL", annotation = url)
            pushStyle(SpanStyle(
                color = colorResource(R.color.blue_light),
                textDecoration = TextDecoration.Underline
            ))
            append(url)
            pop()
            pop()
            
            lastIndex = matchResult.range.last + 1
        }
        append(text.substring(lastIndex))
    }
    
    ClickableText(
        text = annotatedString,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        onClick = { offset ->
            annotatedString.getStringAnnotations(tag = "URL", start = offset, end = offset)
                .firstOrNull()?.let { annotation ->
                    uriHandler.openUri(annotation.item)
                }
        },
        style = androidx.compose.ui.text.TextStyle(
            fontSize = 14.sp,
            color = Color.Gray,
            lineHeight = 20.sp
        )
    )
}

@Composable
fun TitleItem(title: String, isFirst: Boolean = false) {
    if (!isFirst) {
        Divider(
            color = colorResource(R.color.grey).copy(alpha = 0.5f),
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            thickness = 0.5.dp
        )
    }
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 24.dp, bottom = 8.dp),
        text = title.uppercase(),
        fontSize = 12.sp,
        fontWeight = FontWeight.ExtraBold,
        color = colorResource(R.color.blue_light)
    )
}
