package com.example.ashborn.repository

import androidx.lifecycle.LiveData
import com.example.ashborn.data.User
import kotlinx.coroutines.flow.Flow

interface UserRepository{


    suspend fun upsertUser(utente: User)

    suspend fun deleteUser(utente: User)

    fun getUserByClientCode(clientCode:String): LiveData<User?>

    fun isPinCorrect(aCodCliente:String, aPin:String): Flow<Boolean>

}