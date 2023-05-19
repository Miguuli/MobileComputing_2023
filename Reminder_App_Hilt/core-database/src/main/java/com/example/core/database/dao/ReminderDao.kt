package com.example.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.core.database.entity.ReminderEntity
import com.example.core.model.entity.Reminder
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {
    @Query("SELECT * FROM reminders WHERE uID = :uID")
    fun getReminderWithId(uID: Long): ReminderEntity?

    @Query("SELECT * FROM reminders LIMIT 15")
    fun reminders(): Flow<List<ReminderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ReminderEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: Collection<ReminderEntity>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(entity: ReminderEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAll(entities: Collection<ReminderEntity>)

    @Delete
    suspend fun delete(entity: ReminderEntity): Int
}