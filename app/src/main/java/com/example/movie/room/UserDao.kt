package com.example.movie.room

import androidx.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM User WHERE username like :username and password like :password")
    fun login(username: String, password: String):Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUser(user: User): Long

    @Update
    fun updateItem(user: User):Int
}