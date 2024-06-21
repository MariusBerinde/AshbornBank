package com.example.ashborn.data

import android.content.Context
import com.example.ashborn.db.UserDb
import com.example.ashborn.repository.OfflineUserRepository
import com.example.ashborn.repository.UserRepository

interface AppContainer {
    val userRepository : UserRepository
}

class AppDataContainer(
    private val context: Context
):AppContainer{
    override val userRepository:UserRepository by lazy {
        OfflineUserRepository(UserDb.getDatabase(context).userDao())
    }

}