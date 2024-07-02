package com.example.ashborn.repository

import androidx.lifecycle.LiveData
import com.example.ashborn.dao.AshbornDao
import com.example.ashborn.data.User
import kotlinx.coroutines.flow.Flow

class OfflineUserRepository(private val ashbornDao: AshbornDao):UserRepository{

    override suspend fun upsertUser(utente: User) = ashbornDao.upsertUser(utente)

    override suspend fun deleteUser(utente: User) = ashbornDao.deleteUser(utente)


    override fun getUserByClientCode(clientCode:String): LiveData<User?> = ashbornDao.getUserByClientCode(clientCode)

    override fun isPinCorrect(aCodCliente:String, aPin:String): Flow<Boolean> = ashbornDao.isPinCorrect(aCodCliente, aPin)
}