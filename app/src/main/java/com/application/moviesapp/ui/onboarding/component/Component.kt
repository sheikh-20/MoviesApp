package com.application.moviesapp.ui.onboarding.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun SocialLoginComponent(modifier: Modifier = Modifier, @DrawableRes icon: Int, onClick: () -> Unit) {
    Button(onClick = onClick,
        shape = RoundedCornerShape(30),
        border = BorderStroke(width = .5.dp, color =  Color.LightGray),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)) {

        Icon(painter = painterResource(id = icon),
            contentDescription = null,
            modifier = modifier
                .size(30.dp)
                .padding(4.dp))
    }
}