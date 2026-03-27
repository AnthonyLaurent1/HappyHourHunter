package com.supdevinci.happyhourhunter.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.supdevinci.happyhourhunter.R
import com.supdevinci.happyhourhunter.ui.theme.AccentBlue
import com.supdevinci.happyhourhunter.ui.theme.BackgroundCream
import com.supdevinci.happyhourhunter.ui.theme.SurfaceSoftPink
import com.supdevinci.happyhourhunter.ui.theme.TextPrimary
import com.supdevinci.happyhourhunter.ui.theme.TextSecondary

@Composable
fun SplashScreen() {
    val composition = rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.cocktail)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundCream)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            composition = composition.value,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
        )

        Text(
            text = "Happy Hour Hunter",
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary
        )

        Text(
            text = "Preparation de votre selection...",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )

        androidx.compose.foundation.layout.Spacer(
            modifier = Modifier.height(24.dp)
        )

        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(999.dp)),
            color = AccentBlue,
            trackColor = SurfaceSoftPink
        )
    }
}
