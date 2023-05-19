package com.example.core.domain.businesslogic.notification

class NotificationUseCases {
    //configure_notification()
    /*
    private fun configure_notification() {
        val name = "NotificationChannelName"
        val descriptionText = "NotificationChannelDescriptionText"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel("CHANNEL_ID", name, importance).apply {
            description = descriptionText
            enableLights(true)
            enableVibration(true)
            setSound(Uri.parse("android.resource://"+ app.packageName + "/" + "raw/wakey_wakey"),
                audioAttributes)
        }
        val notificationManager: NotificationManager =
            app.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun setNotifications(
        uid: Long, reminderTime: String, content: String?,
        locationX: Double?, locationY: Double?
    ) {
        queueNotification(uid, reminderTime, 0, content!!,
            "Created reminder: $reminderTime", locationX, locationY)
        val distance = getDistanceToDestination(locationX!!, locationY!!)
        if(distance <= 100){
            setLocationNotification(uid, reminderTime, content,
                locationX, locationY)
        }
        val time_delta = reminder_to_delta(reminderTime)
        val offset_ms = 15*60*1000
        val time_delta_15_min_offset = time_delta - offset_ms

        queueNotification(uid, reminderTime, time_delta, content,
            "Reminder due now!: $reminderTime", locationX, locationY)

        if(time_delta >= offset_ms){
            queueNotification(uid, reminderTime, time_delta_15_min_offset, content,
                "Reminder due in 15 minutes", locationX, locationY)
        }
    }

    fun setEditNotification(uid: Long, reminderTime: String, content: String?) {
        queueEditNotification(0, "Edited reminder: $reminderTime", content)
    }

    fun setRemoveNotification(uid: Long) {
        queueDeleteNotification(0, "Deleted reminder", uid.toString())
    }

    fun setLocationNotification(uid: Long, reminderTime: String, content: String, locationX: Double, locationY: Double) {
        queueLocationNotification(uid,5,
            "Reminder nearby", reminderTime, content, locationX, locationY)
    }

    private fun queueLocationNotification(
        uid: Long, time_delta: Long, info: String,
        reminderTime: String, content: String,
        locationX: Double,
        locationY: Double
    ) {
        val workManager = WorkManager.getInstance(app)
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val notificationWorker = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(time_delta, TimeUnit.MILLISECONDS)
            .setConstraints(constraints)
            .build()

        workManager.enqueue(notificationWorker)

        workManager.getWorkInfoByIdLiveData(notificationWorker.id)
            .observeForever { workInfo ->
                if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                    editReminderVisibility(uid, reminderTime, true, content, locationX, locationY)
                    tts.speak("$info.$content", TextToSpeech.QUEUE_ADD, Bundle.EMPTY, "message")
                    createSuccessNotification(content, info)
                }
            }
    }

    private fun queueNotification(
        uid: Long,
        reminderTime: String,
        time_delta: Long,
        content: String,
        info: String,
        locationX: Double?,
        locationY: Double?
    ){
        val workManager = WorkManager.getInstance(app)
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val notificationWorker = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(time_delta, TimeUnit.MILLISECONDS)
            .setConstraints(constraints)
            .build()

        workManager.enqueue(notificationWorker)

        workManager.getWorkInfoByIdLiveData(notificationWorker.id)
            .observeForever { workInfo ->
                if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                    if(info.contains("Reminder due now")){
                        editReminderVisibility(uid, reminderTime, true, content, locationX, locationY)
                    }
                    createSuccessNotification(content, info)
                }
            }
    }

    fun queueEditNotification(time_delta: Long, info: String, content: String?){
        val workManager = WorkManager.getInstance(app)
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val notificationWorker = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(time_delta, TimeUnit.MILLISECONDS)
            .setConstraints(constraints)
            .build()

        workManager.enqueue(notificationWorker)

        workManager.getWorkInfoByIdLiveData(notificationWorker.id)
            .observeForever { workInfo ->
                if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                    createSuccessNotification(content, info)
                }
            }
    }

    fun queueDeleteNotification(time_delta: Long, info: String, content: String?){
        val workManager = WorkManager.getInstance(app)
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val notificationWorker = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(time_delta, TimeUnit.MILLISECONDS)
            .setConstraints(constraints)
            .build()

        workManager.enqueue(notificationWorker)

        workManager.getWorkInfoByIdLiveData(notificationWorker.id)
            .observeForever { workInfo ->
                if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                    createSuccessNotification(content, info)
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

    private fun createSuccessNotification(content: String?, info: String) {
        val notificationId = 1
        val builder = NotificationCompat.Builder(app, "CHANNEL_ID")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(info)
            .setContentText(content!!)
            .setVibrate(longArrayOf(500,500,500,500,500))
            //.setSound(Uri.parse("android.resource://"+ app.packageName + "/" + "raw/chime_short"))
            .setPriority(NotificationCompat.PRIORITY_MAX)

        val intent = PendingIntent.getActivity(app, 0,
            Intent(app, MainActivity::class.java), PendingIntent.FLAG_IMMUTABLE)

        builder.setContentIntent(intent)

        with(NotificationManagerCompat.from(app)) {
            //notificationId is unique for each notification that you define
            notify(notificationId, builder.build())
        }
    }

    private fun createErrorNotification(){
        val notificationId = 1
        val builder = NotificationCompat.Builder(app, "CHANNEL_ID")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Error!")
            .setContentText("Your countdown did not complete")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(app)) {
            //notificationId is unique for each notification that you define
            notify(notificationId, builder.build())
        }
    }
    */
}