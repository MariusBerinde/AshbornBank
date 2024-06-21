package com.example.ashborn.repository

import com.example.ashborn.data.User
import kotlinx.coroutines.flow.Flow

interface UserRepository{
    suspend fun upsertUser(utente: User)

    suspend fun deleteUser(utente: User)

    fun getUserById(id:Int): Flow<User>

}