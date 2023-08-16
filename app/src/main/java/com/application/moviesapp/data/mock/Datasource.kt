package com.application.moviesapp.data.mock

import androidx.annotation.DrawableRes
import com.application.moviesapp.R

data class Affirmation(@DrawableRes val imageSrc: Int)
object Datasource {
    fun getMockDataList(): List<Affirmation> {
        return listOf(
            Affirmation(R.drawable.image1),
            Affirmation(R.drawable.image2),
            Affirmation(R.drawable.image3),
            Affirmation(R.drawable.image4),
            Affirmation(R.drawable.image5),
            Affirmation(R.drawable.image6),
        )
    }
}