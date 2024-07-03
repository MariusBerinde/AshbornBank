package com.example.ashborn.repository

import com.example.ashborn.dao.AshbornDao
import com.example.ashborn.data.User
import kotlinx.coroutines.flow.Flow

class OfflineUserRepository(private val ashbornDao: AshbornDao):UserRepository{

    override suspend fun upsertUser(utente: User) = ashbornDao.upsertUser(utente)

    override suspend fun deleteUser(utente: User) = ashbornDao.deleteUser(utente)


   override fun getUserByClientCode(clientCode:String): Flow<User> = ashbornDao.getUserByClientCode(clientCode)

    override fun isPinCorrect(aCodCliente:String, aPin:String): Flow<Boolean> = ashbornDao.isPinCorrect(aCodCliente, aPin)
}