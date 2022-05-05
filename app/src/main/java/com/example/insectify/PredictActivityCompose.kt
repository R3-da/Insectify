package com.example.insectify

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Space
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu

import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.insectify.ui.theme.InsectifyTheme
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun PredictLayout(navController: NavController) {
    var isCameraSelected = false
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        bitmap = if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        } else {
            val source = ImageDecoder.createSource(context.contentResolver, uri!!)
            ImageDecoder.decodeBitmap(source)
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { btm: Bitmap? ->
        bitmap = btm
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
                     },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Spacer (
                        modifier = Modifier.weight(0.7f)
                            )
                    Row (
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
                            ) {
                                Icon(painter = painterResource(R.drawable.ic_outline_add_photo_alternate_24),
                                    contentDescription = "content description",
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .alpha(0.1f)
                                        .fillMaxSize(0.3f)
                                )
                            }

                            bitmap?.let { it1 ->
                                Image(
                                    contentDescription = null ,
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
                            Text(text = stringResource(R.string.gallery_button) + " ", fontSize = dpToSp(15.dp))
                            Icon(painter = painterResource(R.drawable.ic_outline_photo_library_24) ,contentDescription = "content description")
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
                            Text(text = stringResource(R.string.camera_button)+" ", fontSize = dpToSp(15.dp))
                            Icon(painter = painterResource(R.drawable.ic_outline_photo_camera_24),contentDescription = "content description")
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
                            }
                        ) {
                            Text(text = stringResource(R.string.predict_button) + " ", fontSize = dpToSp(15.dp))
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
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = "Prediction : 100%",
                            fontSize = dpToSp(15.dp),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier
                            .height(20.dp))
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = "Prediction : 100%",
                            fontSize = dpToSp(15.dp),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier
                            .height(20.dp))
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = "Prediction : 100%",
                            fontSize = dpToSp(15.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun dpToSp(dp: Dp) = with(LocalDensity.current) { dp.toSp() }

@Preview(showBackground = true)
@Composable
fun PredictScreenPreview() {
    InsectifyTheme() {
        PredictLayout(navController = rememberNavController())
    }
}