package com.example.myapplication.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.data.entity.Reminder
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ReminderDao {

    @Query("SELECT * FROM reminder WHERE uID = :uID")
    abstract fun getReminderWithId(uID: Long): Reminder?

    @Query("SELECT * FROM reminder LIMIT 15")
    abstract fun reminders(): Flow<List<Reminder>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(entity: Reminder): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(entities: Collection<Reminder>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun update(entity: Reminder)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun updateAll(entities: Collection<Reminder>)

    @Delete
    abstract suspend fun delete(entity: Reminder): Int
}