package com.example.ashborn.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "users")
data class User(
    val name:String,
    val surname:String,
    val dateOfBirth:String,
    val pin:String,

     @PrimaryKey
    val clientCode:String,


)

