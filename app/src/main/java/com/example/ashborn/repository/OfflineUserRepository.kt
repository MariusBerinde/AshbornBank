package com.example.ashborn.repository

import androidx.lifecycle.LiveData
import com.example.ashborn.dao.UserDao
import com.example.ashborn.data.User
import kotlinx.coroutines.flow.Flow

class OfflineUserRepository(private val userDao: UserDao):UserRepository{

    override suspend fun upsertUser(utente: User) = userDao.upsertUser(utente)

    override suspend fun deleteUser(utente: User) = userDao.deleteUser(utente)


    override fun getUserByClientCode(clientCode:String): LiveData<User?> = userDao.getUserByClientCode(clientCode)

    override fun isPinCorrect(aCodCliente:String, aPin:String): Flow<Boolean> = userDao.isPinCorrect(aCodCliente, aPin)
}