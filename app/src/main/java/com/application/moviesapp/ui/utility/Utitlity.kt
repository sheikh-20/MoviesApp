package com.application.moviesapp.ui.utility

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.application.moviesapp.BuildConfig
import com.application.moviesapp.R
import com.application.moviesapp.domain.model.Stream
import java.io.File
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.floor

val String.toImageUrl: String
    get() {
        return BuildConfig.IMAGE_BASE_URL + this
    }


val String.toYoutubeDuration: String
    get() {
        val pattern = "\\d+H|\\d+M|\\d+S"
        val regex = Regex(pattern)

        val matches = regex.findAll(this)

        return matches.map {
            it.value.lowercase()
        }.joinToString(" ")
    }

val Double.toOneDecimal: String
    get() {
        return "%.1f".format(this)
    }

val LazyListState.isScrolled: Boolean
    get() = firstVisibleItemIndex > 0 || firstVisibleItemScrollOffset > 0

val String.getStream: Stream
    get() {
        val iTag = Regex("itag=\\S+")
        val mimeType = Regex("mime_type=\\S+")
        val resolution = Regex("res=\\S+")
        val fps = Regex("fps=\\S+")
        val type = Regex("type=\\S+")

        return Stream(
            iTag = iTag.find(this)?.value?.let { it.split("itag=")[1] } ?: "",
            mimeType = mimeType.find(this)?.value?.let { it.split("mime_type=")[1] } ?: "",
            resolution = resolution.find(this)?.value?.let { it.split("res=")[1] } ?: "",
            fps = fps.find(this)?.value?.let { it.split("fps=")[1] } ?: "",
            type = type.find(this)?.value?.let { it.split("type=")[1] } ?: ""
        )
    }



val VERBOSE_NOTIFICATION_CHANNEL_NAME: CharSequence =
    "Verbose WorkManager Notifications"
const val VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION =
    "Shows notifications whenever work starts"
const val CHANNEL_ID = "VERBOSE_NOTIFICATION"
const val NOTIFICATION_ID = 1


fun makeStatusNotification(message: String, context: Context) {

    // Make a channel if necessary
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val name = VERBOSE_NOTIFICATION_CHANNEL_NAME
        val description =
            VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance)
        channel.description = description

        // Add the channel
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        notificationManager?.createNotificationChannel(channel)
    }

    // Create the notification
    val builder = NotificationCompat.Builder(context,
        CHANNEL_ID
    )
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(context.getString(R.string.app_name))
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVibrate(LongArray(0))

    // Show the notification
    NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
}

fun String.getFileSize(context: Context): String {
    val file = File(context.filesDir, "/output/${this}")
    return "${floor(file.length().toDouble() / 1024 / 1024).toInt()}"
}

fun Long.formatMinSec(): String {
    return if (this == 0L) {
        "..."
    } else {
        String.format(
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(this),
            TimeUnit.MILLISECONDS.toSeconds(this) -
                    TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(this)
                    )
        )
    }
}

@Composable
fun SetLanguage(language: String = "English (US)") {
    val locale = Locale(
        when (language) {
            "English (US)" -> "en"
            "Tamil" -> "ta"
            "Arabic" -> "ar"
            else -> "en"
        }
    )
    Locale.setDefault(locale)
    val configuration = LocalConfiguration.current
    configuration.setLocale(locale)
    configuration.setLocale(locale)

    val resources = LocalContext.current.resources
    resources.updateConfiguration(configuration, resources.displayMetrics)

}