package com.example.myapplication.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.data.entity.Message
import kotlinx.coroutines.flow.Flow

@Dao
abstract class MessageDao {

    @Query("SELECT * FROM message WHERE uID = :uID")
    abstract fun getMessageWithId(uID: Long): Message?

    @Query("SELECT * FROM message LIMIT 15")
    abstract fun messages(): Flow<List<Message>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(entity: Message): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(entities: Collection<Message>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun update(entity: Message)

    @Delete
    abstract suspend fun delete(entity: Message): Int
}