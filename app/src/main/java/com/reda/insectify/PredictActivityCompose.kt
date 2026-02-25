package com.reda.insectify

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
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import com.reda.insectify.ml.Model
import org.json.JSONObject
import org.tensorflow.lite.support.image.TensorImage
import kotlinx.coroutines.launch
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import java.io.BufferedReader
import java.io.InputStreamReader


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PredictLayout() {

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

    val model = Model.newInstance(context)

    val max3Ind = remember { mutableStateListOf<String?>(
        null, null, null, null, null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null)}
    val max3Score = remember { mutableListOf<Float?>(
        null, null, null, null, null, null, null, null, null, null,
        null, null, null, null, null, null, null, null, null, null
    )}

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
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

    val legacyPhotoPickerLauncher = rememberLauncherForActivityResult(
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                } else {
                    legacyPhotoPickerLauncher.launch("image/*")
                }
            }
        } else {
            Toast.makeText(context, "Permission Denied!", Toast.LENGTH_SHORT).show()
        }
    }

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    )
    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetPeekHeight = if (isPredictClicked) 164.dp else 64.dp,
        sheetBackgroundColor = Color.Transparent,
        sheetElevation = 0.dp,
        sheetContent = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(
                        colorResource(R.color.red_calm).copy(alpha = 0.85f),
                        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
            ) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 16.dp, vertical = 24.dp)) {

                    if (isPredictClicked) {
                        val displayCount = minOf(10, MAX_RESULTS)
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                            var sum = 0.0f

                            // Display the top prediction separately with "most likely" label
                            if (max3Score[0] != null && max3Score[0]!! > 0.01f) {
                                item {
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 12.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        backgroundColor = colorResource(R.color.test_color).copy(alpha = 0.7f),
                                        elevation = 0.dp
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(12.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .padding(end = 8.dp),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Text(
                                                    text = "Most Likely : ${"%.2f".format(max3Score[0]!! * 100)}%",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = colorResource(R.color.green_harsh),
                                                    modifier = Modifier.padding(bottom = 8.dp)
                                                )
                                                val uriHandler = LocalUriHandler.current
                                                val insectName = insectsLabels[max3Ind[0].toString()].toString()
                                                val annotatedNameString = buildAnnotatedString {
                                                    pushStyle(SpanStyle(
                                                        color = colorResource(R.color.blue_light),
                                                        fontWeight = FontWeight.Bold,
                                                        textDecoration = TextDecoration.Underline
                                                    ))
                                                    append(insectName)
                                                    addStringAnnotation(tag = "URL", annotation = "https://www.gbif.org/species/${max3Ind[0]}", start = 0, end = insectName.length)
                                                    pop()
                                                }
                                                ClickableText(
                                                    text = annotatedNameString,
                                                    style = TextStyle(textAlign = TextAlign.Center, fontSize = 16.sp),
                                                    modifier = Modifier.fillMaxWidth(),
                                                    onClick = { offset ->
                                                        annotatedNameString.getStringAnnotations("URL", offset, offset)
                                                            .firstOrNull()?.let { annotation ->
                                                                uriHandler.openUri(annotation.item)
                                                            }
                                                    }
                                                )
                                            }

                                            if (bitmap != null) {
                                                Card(
                                                    shape = RoundedCornerShape(8.dp),
                                                    modifier = Modifier.size(80.dp),
                                                    elevation = 2.dp
                                                ) {
                                                    Image(
                                                        bitmap = bitmap!!.asImageBitmap(),
                                                        contentDescription = null,
                                                        contentScale = ContentScale.Crop,
                                                        modifier = Modifier.fillMaxSize()
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                                sum += max3Score[0]!!

                                // spacer between the main (most likely) prediction and the rest
                                item {
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }

                            item {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 16.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    backgroundColor = colorResource(R.color.test_color).copy(alpha = 0.7f),
                                    elevation = 0.dp
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp)
                                    ) {

                                        // Display remaining predictions
                                        val validIndices = mutableListOf<Int>()
                                        for (i in 1 until displayCount) {
                                            if (max3Score[i] != null && max3Score[i]!! > 0.01f) {
                                                validIndices.add(i)
                                                sum += max3Score[i]!!
                                            }
                                        }

                                        for ((index, i) in validIndices.withIndex()) {
                                            PredictItem(
                                                insectName = insectsLabels[max3Ind[i].toString()].toString(),
                                                percentage = "%.2f".format(max3Score[i]!! * 100),
                                                insectId = max3Ind[i].toString()
                                            )
                                            // Add horizontal divider between rows (but not after the last one)
                                            if (index < validIndices.size - 1) {
                                                Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color.Gray.copy(alpha = 0.3f))
                                            }
                                        }

                                        // Add divider before "Others" if there are valid predictions
                                        if (validIndices.isNotEmpty()) {
                                            Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color.Gray.copy(alpha = 0.3f))
                                        }

                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "Others",
                                                fontSize = 15.sp,
                                                color = Color.Gray,
                                                modifier = Modifier.weight(2f),
                                                textAlign = TextAlign.Center
                                            )
                                            Divider(
                                                modifier = Modifier
                                                    .width(2.dp)
                                                    .height(24.dp),
                                                color = Color.Gray.copy(alpha = 0.5f)
                                            )
                                            Text(
                                                text = "${"%.2f".format(((1f - sum).coerceAtLeast(0f)) * 100)}%",
                                                fontSize = 15.sp,
                                                textAlign = TextAlign.Center,
                                                color = Color.Gray,
                                                modifier = Modifier.weight(1f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    } else {

                        Text(
                            text = "Pick a Pic!",
                            fontSize = 16.sp,
                            color = Color.White.copy(alpha = 0.7f),
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
        Card(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .aspectRatio(1f),
            backgroundColor = Color.LightGray,
            elevation = 8.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        } else {
                            legacyPhotoPickerLauncher.launch("image/*")
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                if (bitmap == null) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(R.drawable.ic_outline_add_photo_alternate_24),
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Tap to upload an image", color = Color.Gray)
                    }
                } else {
                    Image(
                        contentDescription = null,
                        bitmap = bitmap!!.asImageBitmap(),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(R.color.blue_light)),
                modifier = Modifier.weight(1f).height(56.dp),
                onClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    } else {
                        legacyPhotoPickerLauncher.launch("image/*")
                    }
                }
            ) {
                Icon(painterResource(R.drawable.ic_outline_photo_library_24), contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(R.string.gallery_button))
            }

            Button(
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(R.color.blue_light)),
                modifier = Modifier.weight(1f).height(56.dp),
                onClick = {
                    val permission = Manifest.permission.CAMERA
                    if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                        cameraLauncher.launch()
                    } else {
                        isCameraSelected = true
                        permissionLauncher.launch(permission)
                    }
                }
            ) {
                Icon(painterResource(R.drawable.ic_outline_photo_camera_24), contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(R.string.camera_button))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(R.color.green_harsh)),
            modifier = Modifier.fillMaxWidth().height(56.dp),
            onClick = {
                bitmap?.let {
                    isPredictClicked = true
                    val resized: Bitmap = Bitmap.createScaledBitmap(it, 224, 224, true)
                    val tBuffer = TensorImage.fromBitmap(resized)
                    val outputs = model.process(tBuffer).probabilityAsCategoryList.apply {
                        sortByDescending { it.score }
                    }.take(MAX_RESULTS)

                    for (i in 0 until MAX_RESULTS) {
                        if (i < outputs.size) {
                            max3Ind[i] = outputs[i].label
                            max3Score[i] = outputs[i].score
                        } else {
                            max3Ind[i] = null
                            max3Score[i] = null
                        }
                    }

                    // show the sheet to present results (sheet is dismissible)
                    scope.launch { scaffoldState.bottomSheetState.expand() }
                } ?: run {
                    Toast.makeText(context, "Please upload an image!", Toast.LENGTH_SHORT).show()
                }
            }
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_outline_magnifier),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(R.string.predict_button), fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

    }
}

}

@Composable
fun PredictItem(insectName: String, percentage: String, insectId: String) {
    val uriHandler = LocalUriHandler.current
    val annotatedNameString = buildAnnotatedString {
        pushStyle(SpanStyle(
            color = colorResource(R.color.blue_light),
            fontWeight = FontWeight.Bold,
            textDecoration = TextDecoration.Underline
        ))
        append(insectName)
        addStringAnnotation(tag = "URL", annotation = "https://www.gbif.org/species/$insectId", start = 0, end = insectName.length)
        pop()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ClickableText(
            modifier = Modifier.weight(2f),
            text = annotatedNameString,
            style = TextStyle(textAlign = TextAlign.Center, fontSize = 16.sp),
            onClick = { offset ->
                annotatedNameString.getStringAnnotations("URL", offset, offset)
                    .firstOrNull()?.let { annotation ->
                        uriHandler.openUri(annotation.item)
                    }
            }
        )
        Divider(
            modifier = Modifier
                .width(2.dp)
                .height(24.dp),
            color = Color.Gray.copy(alpha = 0.5f)
        )
        Text(
            text = "$percentage%",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = colorResource(R.color.green_harsh),
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Bold
        )
    }
}

