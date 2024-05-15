package com.example.vodo.presentation.components.splash_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.vodo.R
import kotlinx.coroutines.delay

@Composable
fun  SplashScreen(onNavigate: () -> Unit) {

    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.splash_anim)
    )

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){


        LottieAnimation(composition = composition, speed = 1.5f, iterations = 2, restartOnPlay = true)

        LaunchedEffect(key1 = true){
            delay(2000)
            onNavigate()
        }

    }



}