package uk.ac.tees.mad.bloodbond.notificaion

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import uk.ac.tees.mad.bloodbond.R
import java.util.Calendar

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create channel (for Android O+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "daily_channel",
                "Daily Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Get the message passed from scheduler
        val message = intent?.getStringExtra("message") ?: "Stay healthy!"

        val notification = NotificationCompat.Builder(context, "daily_channel")
            .setContentTitle("BloodBond") // app name
            .setContentText(message)
            .setSmallIcon(R.mipmap.ic_launcher) // ✅ app icon // replace with your icon
            .setAutoCancel(true)
            .build()

        // Show notification
        val id = System.currentTimeMillis().toInt()
        notificationManager.notify(id, notification)
    }
}


fun scheduleDailyNotifications(context: Context) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val messages = listOf(
        "Help the needy ones by donating the blood",
        "Don't wait in blood bank, reach out to the donors now"
    )

    val times = listOf(9 to 0, 18 to 0) // 9 AM, 6 PM

    for (i in times.indices) {
        val (hour, minute) = times[i]

        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("message", messages[i])
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            i,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }
}


fun showInstantNotification(context: Context) {
    val intent = Intent(context, NotificationReceiver::class.java).apply {
        putExtra("message", "⚡ Instant Demo Notification")
    }
    context.sendBroadcast(intent)
}