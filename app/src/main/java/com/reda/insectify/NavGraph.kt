package com.reda.insectify

import androidx.compose.animation.core.tween
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

@Composable
fun MainScreen() {
    val pagerState = rememberPagerState(pageCount = { 2 })
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        // Invisible Top Bar with Animated Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 4.dp)
                .padding(vertical = 8.dp)
                .background(Color.White.copy(alpha = 0f))
        ) {
            AnimatedTopBarButton(
                currentPage = pagerState.currentPage,
                onNavigate = { targetPage ->
                    scope.launch {
                        pagerState.animateScrollToPage(targetPage)
                    }
                }
            )
        }

        // Main Content with HorizontalPager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {
                0 -> PredictLayout()
                1 -> DetailsLayout()
            }
        }
    }
}

@Composable
fun AnimatedTopBarButton(currentPage: Int, onNavigate: (Int) -> Unit) {
    // Elastic easing curve for smooth animation
    val elasticEasing = CubicBezierEasing(0.34f, 1.56f, 0.64f, 1.0f)
    
    // Animate the progress (0 = page 0, 1 = page 1)
    val progress by animateFloatAsState(
        targetValue = currentPage.toFloat(),
        animationSpec = tween(durationMillis = 700, easing = elasticEasing),
        label = "buttonTransformAnimation"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        // Expansion width: expands to ~150dp at progress 0.5, then contracts back to 48dp
        val expansionFactor = kotlin.math.sin(progress * 3.14159f) // 0 -> 1 -> 0
        val expandedWidth = 150.dp
        val minWidth = 64.dp
        val currentWidth = minWidth + (expandedWidth - minWidth) * expansionFactor

        // Alignment: starts right (page 0), moves to left (page 1)
        val isOnLeft = progress > 0.5f

        // Show icon with smooth fade in/out at edges
        val detailsIconAlpha = kotlin.math.max(0f, 1f - (progress / 0.5f).coerceIn(0f, 1f))
        val backIconAlpha = kotlin.math.max(0f, ((progress - 0.5f) / 0.5f).coerceIn(0f, 1f))

        Box(
            modifier = Modifier
                .height(48.dp)
                .width(currentWidth)
                .align(if (isOnLeft) Alignment.CenterStart else Alignment.CenterEnd)
        ) {
            // Back Button (fades in as we move to page 1)
            if (backIconAlpha > 0.01f) {
                FloatingNavButton(
                    icon = R.drawable.ic_baseline_arrow_back_24,
                    onClick = { onNavigate(0) },
                    modifier = Modifier
                        .align(Alignment.Center)
                        .alpha(backIconAlpha)
                )
            }

            // Details Button (fades out as we move to page 1)
            if (detailsIconAlpha > 0.01f) {
                FloatingNavButton(
                    icon = R.drawable.ic_outline_info_24,
                    onClick = { onNavigate(1) },
                    modifier = Modifier
                        .align(Alignment.Center)
                        .alpha(detailsIconAlpha)
                )
            }
        }
    }
}

@Composable
fun FloatingNavButton(icon: Int, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.size(48.dp),
        shape = CircleShape,
        color = colorResource(R.color.blue_light).copy(alpha = 0.9f),
        elevation = 6.dp
    ) {
        IconButton(onClick = onClick) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
