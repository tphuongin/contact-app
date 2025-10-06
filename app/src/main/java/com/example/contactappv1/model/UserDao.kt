package com.example.contactappv1.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.contactappv1.model.DataUser

@Dao
interface UserDao {
    @Query("SELECT * FROM USERS")
    fun getAll(): LiveData<List<DataUser>>
    @Query("SElECT * FROM USERS WHERE id = :iduser")
    fun getUserById(iduser: Int): LiveData<DataUser>
    @Query("SELECT * FROM USERS WHERE name LIKE (:keyword)")
    fun findUsersByName(keyword: String): LiveData<List<DataUser>>
    @Insert
    suspend fun addUser(user: DataUser)
    @Update
    suspend fun updateInfor(user: DataUser)
    @Delete
    suspend fun deleteContact(user: DataUser)
}