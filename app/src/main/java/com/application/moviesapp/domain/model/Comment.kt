package com.application.moviesapp.domain.model

data class Comment(
    val imageUrl: String? = null,
    val userName: String? = null,
    val comment: String? = null,
    val likes: Int? = null,
    val postedDate: String? = null,
)

object CommentRepository {

    fun getComments(): List<Comment> {
        return listOf(
            Comment(
                imageUrl = "https://storage.googleapis.com/yogazzz.appspot.com/app_config/yoga_recommendation/recommendation3/Screenshot%20from%202024-02-21%2016-29-47.png",
                userName = "Sheikh",
                comment = "Excellent movie",
                likes = 100,
                postedDate = "2 days ago"),

            Comment(
                imageUrl = "https://storage.googleapis.com/yogazzz.appspot.com/app_config/yoga_recommendation/recommendation3/Screenshot%20from%202024-02-21%2016-29-47.png",
                userName = "Gamora",
                comment = "Good film",
                likes = 120,
                postedDate = "2 days ago")
        )
    }
}