package com.example.myapplication.data.repository

import com.example.myapplication.data.entity.User
import com.example.myapplication.data.room.UserDao
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao){
    fun users(): Flow<List<User>> = userDao.users()
    fun getUserWithId(userID: Int): User? = userDao.getUserWithId(userID)

    /**
     * Add an user to the database if it does not exist
     *
     * @return the id of the newly added/created user
     */
    suspend fun addUser(user: User): Long {

        return when (val local = userDao.getUserWithFirstName(user.firstName!!)) {
            null -> userDao.insert(user)
            else -> local.uid
        }
    }
}