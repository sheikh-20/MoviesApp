package com.application.moviesapp.ui.language

data class Language(val title: String, val language: List<String>)

val language = listOf<Language>(
    Language(
        title = "Suggested", listOf("English (US)", "English (UK)")
    ),
    Language(
        title = "Language", listOf("Tamil", "Arabic")
    ),
)