package com.application.moviesapp.service

import android.Manifest
import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.datastore.core.DataStore
import com.application.moviesapp.data.repository.NotificationPreferenceRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PushNotificationService: FirebaseMessagingService() {

    @Inject
    lateinit var  repository: NotificationPreferenceRepository

    @Inject
    lateinit var coroutineScope: CoroutineScope

    companion object {
        enum class CHANNEL_ID {
            GENERAL_NOTIFICATION, APP_UPDATES
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(message: RemoteMessage) {
        val title: String = message.notification?.title ?: ""
        val text: String = message.notification?.body ?: ""

        coroutineScope.launch {
            repository.readGeneralNotificationPreference.collect {
                if (it.data) {
                    showNotification(channelId = CHANNEL_ID.GENERAL_NOTIFICATION.name, title = title, text = text, notificationId = 1)
                }
            }

            repository.readAppUpdatesPreference.collect {
                if (it.data) {
                    showNotification(channelId = CHANNEL_ID.APP_UPDATES.name, title = title, text = text, notificationId = 2)
                }
            }
        }

        super.onMessageReceived(message)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotification(channelId: String, title: String = "", text: String = "", notificationId: Int) {

        val channel = NotificationChannel(
            channelId,
            "Heads Up Notification",
            NotificationManager.IMPORTANCE_HIGH
        )

        val notification: Notification.Builder = Notification.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(com.application.moviesapp.R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        NotificationManagerCompat.from(this).notify(notificationId, notification.build())
    }
}