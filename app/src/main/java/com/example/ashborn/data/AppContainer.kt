package com.example.ashborn.data

import android.content.Context
import com.example.ashborn.db.AshbornDb
import com.example.ashborn.repository.OfflineUserRepository
import com.example.ashborn.repository.UserRepository

interface AppContainer {
    val userRepository : UserRepository
}

class AppDataContainer(
    private val context: Context
):AppContainer{
    override val userRepository:UserRepository by lazy {
        OfflineUserRepository(AshbornDb.getDatabase(context).ashbornDao())
    }

}