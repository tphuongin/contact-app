package com.example.contactappv1.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "users")
data class DataUser(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo
    var name: String,

    @ColumnInfo (name = "phone_number")
    var phoneNumer: String,

    @ColumnInfo
    var email:String? = null,

    @ColumnInfo(name = "avatar_id")
    var srcAvatar: String? = null
): Serializable