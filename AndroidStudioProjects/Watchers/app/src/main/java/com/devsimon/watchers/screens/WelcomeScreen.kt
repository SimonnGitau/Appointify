package com.devsimon.watchers.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.devsimon.watchers.R


@Composable
fun MotionPictureExample(navHostController: NavHostController) {

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier
        .verticalScroll(rememberScrollState())) {
            val transitionState = rememberInfiniteTransition(label = "")

            val translationX by transitionState.animateFloat(
                initialValue = -200f,
                targetValue = 200f,
                animationSpec = infiniteRepeatable(tween(10000), RepeatMode.Reverse), label = ""
            )

            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = R.drawable.theater),
                    contentDescription = "Motion Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer(
                            translationX = translationX,
                            scaleX = 1.5f,
                            scaleY = 1.5f
                        )
                )
            }
            Onboard(navHostController)
        }
    }
}

@Composable
fun PageIndicator(currentPage: Int, pageCount: Int) {
    Box(modifier = Modifier.fillMaxSize()) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pageCount) { iteration ->
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(
                            if (currentPage == iteration) androidx.compose.ui.graphics.Color.Red
                            else androidx.compose.ui.graphics.Color.Gray
                        )
                        .size(5.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Onboard(navHostController: NavHostController) {
    Column(modifier = Modifier
        .fillMaxSize()) {
        Surface(
            modifier = Modifier
                .height(500.dp)
                .padding(top = 20.dp, start = 10.dp, end = 10.dp)
                .clip(RoundedCornerShape(16.dp)),
            tonalElevation = 4.dp
        ) {
            val pagerState = rememberPagerState(pageCount = {
                3
            })
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) {
                // Our page content
                when (it) {
                    0 -> Image(painter = painterResource(id = R.drawable.blue_beetle), contentDescription = "",
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Crop)
                    1 -> Image(painter = painterResource(id = R.drawable.gran_turismo), contentDescription = "",
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Crop)
                    2 -> Image(painter = painterResource(id = R.drawable.oppenheimer), contentDescription = "",
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Crop)
                }
            }

            PageIndicator(pagerState.currentPage, pagerState.pageCount)
        }
        val text = "Watch latest trending movies, anytime, anywhere!"

        Text(text = text,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            color = androidx.compose.ui.graphics.Color.White,
            fontFamily = FontFamily(Font(R.font.g_e_regular)),
            )

        Button(onClick = {navHostController.navigate("toLogIn")},
            border = BorderStroke(2.dp, androidx.compose.ui.graphics.Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(50.dp),
        ){
            Text(text = "Resume",
                color = androidx.compose.ui.graphics.Color.White,
                fontFamily = FontFamily(Font(R.font.gimbal_extended_regular))
            )
        }

        Row(modifier = Modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,) {
            Text(text = "New here?",
                color = androidx.compose.ui.graphics.Color.White,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.gibson_regular))
            )
            Text(text = " Create Account",
                Modifier.clickable {
                    navHostController.navigate("toLogIn")
                },
                color = androidx.compose.ui.graphics.Color.White,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.gibson_bold)),
                fontSize = 18.sp
            )
        }
    }
}