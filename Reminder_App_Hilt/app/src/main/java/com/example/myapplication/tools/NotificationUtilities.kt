package com.example.myapplication.tools

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.myapplication.Graph
import com.example.myapplication.R
import java.util.Date
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.DurationUnit

fun setNotifications(
    uid: Long, reminderTime: String, content: String?,
    intent: PendingIntent
) {
    queueNotification(uid, reminderTime, 0, content!!,
        "Created reminder: $reminderTime", intent)

    val time_delta = reminder_to_delta(reminderTime)
    val offset_ms = 15*60*1000
    val time_delta_15_min_offset = time_delta - offset_ms

    queueNotification(uid, reminderTime, time_delta, content,
        "Reminder due now!: $reminderTime", intent)

    if(time_delta >= offset_ms){
        queueNotification(uid, reminderTime, time_delta_15_min_offset, content,
            "Reminder due in 15 minutes", intent)
    }
}

fun setEditNotification(uid: Long, reminderTime: String, content: String?,
                        intent: PendingIntent
) {
    queueEditNotification(0, "Edited reminder: $reminderTime",
        content, intent)
}

fun setRemoveNotification(uid: Long, intent: PendingIntent) {
    queueDeleteNotification(0, "Deleted reminder", uid.toString(),
        intent)
}

fun queueNotification(
    uid: Long,
    reminderTime: String,
    time_delta: Long,
    content: String,
    info: String,
    intent: PendingIntent
){
    val workManager = WorkManager.getInstance(Graph.appContext)
    val constraints = Constraints.Builder()
        .build()

    val notificationWorker = OneTimeWorkRequestBuilder<NotificationWorker>()
        .setInitialDelay(time_delta, TimeUnit.MILLISECONDS)
        .setConstraints(constraints)
        .build()

    workManager.enqueue(notificationWorker)

    workManager.getWorkInfoByIdLiveData(notificationWorker.id)
        .observeForever { workInfo ->
            if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                println("workInfo.state: ${workInfo.state}")
                if(info.contains("Reminder due now")){
                    //editReminderVisibility(uid, reminderTime, true, content, locationX, locationY)
                }
                createSuccessNotification(content, info, intent)
            }
        }
}

fun queueEditNotification(time_delta: Long, info: String, content: String?,
                          intent: PendingIntent
){
    val workManager = WorkManager.getInstance(Graph.appContext)
    val constraints = Constraints.Builder()
        .build()

    val notificationWorker = OneTimeWorkRequestBuilder<NotificationWorker>()
        .setInitialDelay(time_delta, TimeUnit.MILLISECONDS)
        .setConstraints(constraints)
        .build()

    workManager.enqueue(notificationWorker)

    workManager.getWorkInfoByIdLiveData(notificationWorker.id)
        .observeForever { workInfo ->
            if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                createSuccessNotification(content, info, intent)
            }
        }
}

fun queueDeleteNotification(time_delta: Long, info: String, content: String?,
                            intent: PendingIntent
){
    val workManager = WorkManager.getInstance(Graph.appContext)
    val constraints = Constraints.Builder()
        .build()

    val notificationWorker = OneTimeWorkRequestBuilder<NotificationWorker>()
        .setInitialDelay(time_delta, TimeUnit.MILLISECONDS)
        .setConstraints(constraints)
        .build()

    workManager.enqueue(notificationWorker)

    workManager.getWorkInfoByIdLiveData(notificationWorker.id)
        .observeForever { workInfo ->
            if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                createSuccessNotification(content, info, intent)
            }
        }
}

private fun reminder_to_delta(reminderTime: String): Long {
    val remindertime_split: List<String>?
    val remindertime_hours: kotlin.time.Duration?
    val remindertime_minutes: kotlin.time.Duration?

    if(reminderTime.contains(".")){
        remindertime_split = reminderTime.split(".")
        remindertime_hours = remindertime_split[0].toLong().hours
        remindertime_minutes = remindertime_split[1].toLong().minutes
    }else{
        remindertime_hours = reminderTime.toLong().hours
        remindertime_minutes = 0.minutes
    }

    val custom_time = Date()
    custom_time.hours = remindertime_hours.toInt(DurationUnit.HOURS)
    custom_time.minutes = remindertime_minutes.toInt(DurationUnit.MINUTES)
    custom_time.seconds = 0

    val current_time = Date().time
    println("current_time: $current_time")
    println("converted_remindertime: ${custom_time.time}")

    val time_delta = custom_time.time - current_time
    println("time delta: $time_delta")
    return time_delta
}

@SuppressLint("MissingPermission")
fun createErrorNotification(icon: Int){
    val notificationId = 1
    val builder = NotificationCompat.Builder(Graph.appContext, "CHANNEL_ID")
        .setSmallIcon(icon)
        .setContentTitle("Error!")
        .setContentText("Your countdown did not complete")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    with(NotificationManagerCompat.from(Graph.appContext)) {
        //notificationId is unique for each notification that you define
        notify(notificationId, builder.build())
    }
}
@SuppressLint("MissingPermission")
fun createSuccessNotification(content: String?, info: String, intent: PendingIntent) {
    val notificationId = 1
    val builder = NotificationCompat.Builder(Graph.appContext, "CHANNEL_ID")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(info)
        .setContentText(content!!)
        .setVibrate(longArrayOf(500,500,500,500,500))
        //.setSound(Uri.parse("android.resource://"+ app.packageName + "/" + "raw/chime_short"))
        .setPriority(NotificationCompat.PRIORITY_MAX)

    builder.setContentIntent(intent)

    with(NotificationManagerCompat.from(Graph.appContext)) {
        //notificationId is unique for each notification that you define
        notify(notificationId, builder.build())
    }
}
class NotificationWorker (
    appContext: Context,
    workerParams: WorkerParameters,
): Worker(appContext, workerParams) {
    override fun doWork(): Result {
        return try {
            for (i in 0..10) {
                println("Counted $i")
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}