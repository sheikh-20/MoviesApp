package com.application.moviesapp.data.mock

import android.util.TimeUtils
import androidx.annotation.DrawableRes
import com.application.moviesapp.R
import timber.log.Timber
import java.util.Calendar


data class Affirmation(@DrawableRes val imageSrc: Int)

data class Categories(val name: String)

data class Sort(val name: String)

data class Period(val name: String)

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

    fun getCategories(): List<Categories> {
        return listOf(
            Categories("Movie"),
            Categories("TV Shows"),
            Categories("K-Drama"),
        )
    }

    fun getSort(): List<Sort> {
        return listOf(
            Sort("Popularity"),
            Sort("Latest Release")
        )
    }

    fun getPeriod(): List<Period> {

        val time = mutableListOf<Period>(Period(name = "All Periods"))

        val currentYear = Calendar.getInstance()[Calendar.YEAR]

        for (x in currentYear downTo 2000) {
            time.add(Period(x.toString()))
        }

        return time
    }
}