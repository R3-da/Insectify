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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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

    val max3Ind = remember { mutableStateListOf<String?>(null, null, null)}
    val max3Score = remember { mutableListOf<Float?>(null, null, null)}

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
                        backgroundColor = colorResource(R.color.grey)
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
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        onClick = {

                                isPredictClicked = true
                                val resized: Bitmap = Bitmap.createScaledBitmap(bitmap!!, 224, 224, true)
// Creates inputs for reference.
                                val tBuffer = TensorImage.fromBitmap(resized)

// Runs model inference and gets result.

                                val outputs = model2.process(tBuffer).probabilityAsCategoryList.apply {
                                    sortByDescending { it.score }
                                }.take(3)

                                for (i in 0..2) {
                                    max3Ind[i] = outputs[i].label
                                    max3Score[i] = outputs[i].score
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
                Spacer(
                    modifier = Modifier.weight(0.3f)
                )
                Column (
                    modifier = Modifier
                        .weight(3f)
                        .fillMaxHeight(),
                        ) {
                    AnimatedVisibility(
                        visible = isPredictClicked,
                        enter = fadeIn(animationSpec = tween(durationMillis = 500))
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(3f)
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (isPredictClicked) {
                                if (max3Score[0] != 0.0f) {
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        text = insectsLabels[max3Ind[0].toString()] as String + " : " + "%.2f".format(
                                            max3Score[0]!! * 100
                                        ) + "%",
                                        fontSize = 15.sp,
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Bold
                                    )
                                    if (max3Score[1] != 0.0f) {
                                        Spacer(
                                            modifier = Modifier
                                                .height(20.dp)
                                        )
                                        Text(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            text = insectsLabels[max3Ind[1].toString()] as String + " : " + "%.2f".format(
                                                max3Score[1]!! * 100
                                            ) + "%",
                                            fontSize = 15.sp,
                                            textAlign = TextAlign.Center,
                                            fontWeight = FontWeight.Bold
                                        )
                                        if (max3Score[2] != 0.0f) {
                                            Spacer(
                                                modifier = Modifier
                                                    .height(20.dp)
                                            )
                                            Text(
                                                modifier = Modifier
                                                    .fillMaxWidth(),
                                                text = insectsLabels[max3Ind[2].toString()] as String + " : " + "%.2f".format(
                                                    max3Score[2]!! * 100
                                                ) + "%",
                                                fontSize = 15.sp,
                                                textAlign = TextAlign.Center,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                                if (max3Score[0]!! + max3Score[1]!! + max3Score[2]!! != 1.0f) {
                                    Spacer(
                                        modifier = Modifier
                                            .height(20.dp)
                                    )
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        text = "other : " + "%.2f".format(100 - (max3Score[0]!! * 100 + max3Score[1]!! * 100 + max3Score[2]!! * 100)) + "%",
                                        fontSize = 15.sp,
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Bold
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
