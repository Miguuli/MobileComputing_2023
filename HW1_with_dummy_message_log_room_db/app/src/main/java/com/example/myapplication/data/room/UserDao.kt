package com.example.myapplication.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.data.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
abstract class UserDao {

    @Query(value = "SELECT * FROM users WHERE name = :first_name")
    abstract suspend fun getUserWithFirstName(name: String): User?

    @Query("SELECT * FROM users WHERE id = :uid")
    abstract fun getUserWithId(userID: Int): User?

    @Query("SELECT * FROM categories LIMIT 15")
    abstract fun users(): Flow<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(entity: User): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(entities: Collection<User>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun update(entity: User)

    @Delete
    abstract suspend fun delete(entity: User): Int
}