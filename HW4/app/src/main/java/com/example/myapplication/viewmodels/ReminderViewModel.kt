package com.example.myapplication.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import androidx.compose.runtime.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.example.myapplication.R
import com.example.myapplication.data.entity.Reminder
import com.example.myapplication.data.repository.ReminderRepository
import com.example.myapplication.domain.ReminderStore
import com.example.myapplication.ui.Graph
import com.example.myapplication.ui.MainActivity
import com.example.myapplication.util.NotificationWorker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.random.Random
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.DurationUnit
import java.time.*

@SuppressLint("MissingPermission")
class ReminderViewModel(private val app: Application,
                        private val reminderRepository: ReminderRepository = Graph.reminderRepository)
    : ViewModel(){
    private val _selected_reminder = MutableStateFlow<Reminder?>(null)

    private val _state = MutableStateFlow(ReminderStore())
    val state: StateFlow<ReminderStore>
        get() = _state
    var enabled by  mutableStateOf(false)
        private set
    var locationByGps: Location? = null

    val gpsLocationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            locationByGps = location
        }

        @Deprecated("Deprecated in Java")
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }
    fun editReminder(time: String, message_content: String,  uid: Long) {
        viewModelScope.launch {
            val reminder = Reminder(uid = uid, reminderTime = time, content = message_content,
                    locationX = 0.0,
                locationY = 0.0)
            reminderRepository.editReminder(reminder = reminder)
            setEditNotification(reminder.uid, reminder.reminderTime, reminder.content)
            println("edited_message: $message_content")
        }
    }

    fun editReminderVisibility(
        uid: Long,
        reminderTime: String,
        enabled: Boolean,
        content: String,
        locationX: Double?,
        locationY: Double?
    ) {
        viewModelScope.launch {
            val reminder = Reminder(uid = uid, reminderTime = reminderTime , content = content, enabled = enabled,
                locationX = locationX,
                locationY = locationY)
            reminderRepository.editReminderVisibility(reminder = reminder)
            println("reminder visibility: $enabled")
        }
    }

    fun addReminder(location: String, message_content: String, reminderTime: String){
        viewModelScope.launch {
            if(reminderTime.isEmpty()) return@launch

            val destination_locationX = getDestinationLocationX(location)
            val destination_locationY = getDestinationLocationY(location)

            println("location: $location")
            val reminder = Reminder(
                locationX = destination_locationX,
                locationY = destination_locationY,
                uid = Random.nextLong(), content = message_content,
                creationTime = Date().time, reminderTime = reminderTime
            )
            reminderRepository.addReminder(reminder = reminder)
            setNotifications(reminder.uid, reminder.reminderTime, reminder.content,
                reminder.locationX, reminder.locationY)
        }
    }

    fun getDestinationLocationX(location: String): Double{
        if (location.contains("Home")) {
            return 65.061489
        }
        else if(location.contains("Work")) {
            return 65.059196
        }
        else if(location.contains("University")) {
            return 65.059289
        }
        return 0.0
    }

    fun getDestinationLocationY(location: String): Double{
        if (location.contains("Home")) {
            return 25.485139
        }
        else if(location.contains("Work")) {
            return 25.441067
        }
        else if(location.contains("University")) {
            return 25.466833
        }
        return 0.0
    }

    fun getDistanceToDestination(locationX: Double, locationY: Double): Float {
        val results: FloatArray = floatArrayOf(elements = FloatArray(3))

        Location.distanceBetween(
            locationByGps!!.latitude, locationByGps!!.longitude,
            locationX, locationY,
            results
        )
        println("Distance from current to reminder location: ${results[0]} meters")
        return results[0]
    }

    fun removeReminder(uid: Long) {
        viewModelScope.launch {
            reminderRepository.deleteReminder(uid = uid)
            setRemoveNotification(uid)
        }
    }

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

    init {
        val locationManager = app.applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (hasGps) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                5000,
                0F,
                gpsLocationListener
            )
        }
        configure_notification()
        viewModelScope.launch{
            combine(
                reminderRepository.reminders().onEach { list->
                    if (list.isNotEmpty() && _selected_reminder.value == null) {
                        _selected_reminder.value = list[0]
                    }
                },
                _selected_reminder
            ){ messages, selected_message->
                ReminderStore(
                    reminders = messages, selected_reminder = selected_message
                )
            }.collect{_state.value = it }
        }
    }
}

class ReminderViewModelFactory(private val app: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ReminderViewModel(app) as T
    }
}
