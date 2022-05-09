package com.example.insectify

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.insectify.ml.Model1
import org.json.JSONObject
import org.tensorflow.lite.support.image.TensorImage
import java.io.BufferedReader
import java.io.InputStreamReader


@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun PredictLayout(navController: NavController) {

    val MAX_RESULTS = 20

    var isCameraSelected = false
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    val inputStream = LocalContext.current.assets.open("insectsDict.json")
    val bR = BufferedReader(InputStreamReader(inputStream))
    var line: String?

    val responseStrBuilder = StringBuilder()
    while (bR.readLine().also { line = it } != null) {
        responseStrBuilder.append(line)
    }
    inputStream.close()

    val insectsLabels = JSONObject(responseStrBuilder.toString())

    var isPredictClicked by remember {mutableStateOf(false)}

    val model2 = Model1.newInstance(context)

    val max3Ind = remember { mutableStateListOf<String?>(
        null, null, null, null, null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null)}
    val max3Score = remember { mutableListOf<Float?>(
        null, null, null, null, null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null
    )}

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            isPredictClicked = false
            bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source).copy(Bitmap.Config.ARGB_8888, true)
            }
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { btm: Bitmap? ->
        if(btm != null) {
            isPredictClicked = false
            bitmap = btm
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            if (isCameraSelected) {
                cameraLauncher.launch()
            } else {
                galleryLauncher.launch("image/*")
            }
        } else {
            Toast.makeText(context, "Permission Denied!", Toast.LENGTH_SHORT).show()
        }
    }

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
                     }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Spacer(
                    modifier = Modifier.weight(0.7f)
                )
                Row(
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
                        backgroundColor = colorResource(R.color.grey),
                        elevation = 5.dp
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable {
                                    if (bitmap == null) {
                                        when (PackageManager.PERMISSION_GRANTED) {
                                            ContextCompat.checkSelfPermission(
                                                context, Manifest.permission.READ_EXTERNAL_STORAGE
                                            ) -> {
                                                galleryLauncher.launch("image/*")
                                            }
                                            else -> {
                                                isCameraSelected = false
                                                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                                            }
                                        }
                                    }
                                }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_outline_add_photo_alternate_24),
                                contentDescription = "content description",
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .alpha(0.1f)
                                    .fillMaxSize(0.3f)
                            )
                        }

                        bitmap?.let { it1 ->
                            Image(
                                contentDescription = null,
                                bitmap = it1.asImageBitmap(),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
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
                        elevation = ButtonDefaults.elevation(5.dp),
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        onClick = {
                            when (PackageManager.PERMISSION_GRANTED) {
                                ContextCompat.checkSelfPermission(
                                    context, Manifest.permission.READ_EXTERNAL_STORAGE
                                ) -> {
                                    galleryLauncher.launch("image/*")
                                }
                                else -> {
                                    isCameraSelected = false
                                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                                }
                            }
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.gallery_button) + " ",
                            fontSize = 15.sp
                        )
                        Icon(
                            painter = painterResource(R.drawable.ic_outline_photo_library_24),
                            contentDescription = "content description"
                        )
                    }
                    Button(
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = colorResource(R.color.blue_light),
                            contentColor = Color.Black
                        ),
                        elevation = ButtonDefaults.elevation(5.dp),
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        onClick = {
                            when (PackageManager.PERMISSION_GRANTED) {
                                ContextCompat.checkSelfPermission(
                                    context, Manifest.permission.CAMERA
                                ) -> {
                                    cameraLauncher.launch()
                                }
                                else -> {
                                    isCameraSelected = true
                                    permissionLauncher.launch(Manifest.permission.CAMERA)
                                }
                            }
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.camera_button) + " ",
                            fontSize = 15.sp
                        )
                        Icon(
                            painter = painterResource(R.drawable.ic_outline_photo_camera_24),
                            contentDescription = "content description"
                        )
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
                        elevation = ButtonDefaults.elevation(5.dp),
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        onClick = {
                            bitmap?.let {
                                isPredictClicked = true
                                val resized: Bitmap = Bitmap.createScaledBitmap(bitmap!!, 224, 224, true)

                                val tBuffer = TensorImage.fromBitmap(resized)

                                val outputs = model2.process(tBuffer).probabilityAsCategoryList.apply {
                                    sortByDescending { it.score }
                                }.take(MAX_RESULTS)

                                for (i in 0 until MAX_RESULTS) {
                                    max3Ind[i] = outputs[i].label
                                    max3Score[i] = outputs[i].score
                                }
                            } ?: run {
                                Toast.makeText(context, "Please upload an image !", Toast.LENGTH_SHORT).show()
                            }


                        }
                    ) {
                        Text(
                            text = stringResource(R.string.predict_button) + " ",
                            fontSize = 15.sp
                        )
                    }
                    Spacer(
                        modifier = Modifier.weight(0.5f)
                    )
                }
                Column (
                    modifier = Modifier
                        .weight(3.3f)
                        .fillMaxHeight(),
                        ) {
                    AnimatedVisibility(
                        visible = isPredictClicked,
                        enter = fadeIn(animationSpec = tween(durationMillis = 500))
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (isPredictClicked) {

                                var sum = 0.0f
                                for (i in 0 until MAX_RESULTS) {
                                    if (max3Score[i] != 0.0f) {
                                        item {
                                            PredictItem(
                                                predictString = insectsLabels[max3Ind[i].toString()] as String + " : " + "%.2f".format(
                                                    max3Score[i]!! * 100
                                                ) + "%"
                                            , max3Ind[i].toString())
                                        }
                                        sum += max3Score[i]!!.toFloat()
                                    } else {
                                        break
                                    }
                                }
                                if (sum != 1.0f) {

                                    item {
                                        Spacer(
                                            modifier = Modifier
                                                .height(20.dp)
                                        )
                                        Text(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            text = "others : " + "%.2f".format(100 - sum * 100) + "%",
                                            fontSize = 15.sp,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PredictItem(predictString : String, insectId : String) {

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
            ) {
        val annotatedLinkString: AnnotatedString = buildAnnotatedString {

            val str = predictString
            val startIndex = 0
            val endIndex = str.indexOf(":") - 1
            append(str)
            addStyle(
                style = SpanStyle(
                    color = Color(0xff64B5F6),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline,
                ), start = startIndex, end = endIndex
            )

            addStringAnnotation(
                tag = "URL",
                annotation = "https://www.gbif.org/species/$insectId",
                start = startIndex,
                end = endIndex
            )

        }

        val uriHandler = LocalUriHandler.current

        ClickableText(
            modifier = Modifier
                .weight(1f)
                .padding(
                    top = 10.dp
                )
                .fillMaxHeight(),
            text = annotatedLinkString,
            style = TextStyle(
                textAlign = TextAlign.Center),
            onClick = {
                annotatedLinkString
                    .getStringAnnotations("URL", it, it)
                    .firstOrNull()?.let { stringAnnotation ->
                        uriHandler.openUri(stringAnnotation.item)
                    }
            }
        )
    }
}

