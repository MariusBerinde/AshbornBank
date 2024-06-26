package com.example.ashborn.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.ashborn.data.User
import kotlinx.coroutines.flow.Flow

/**
 * serve a  prendere dati i dati dell'utente dal db
 */
@Dao
interface UserDao {
   @Upsert
   suspend fun upsertUser(utente:User)

   @Delete
   suspend fun deleteUser(utente: User)

   @Query ("Select * from users where " + "clientCode= :id limit 1" )
   fun getUserById(id:String):Flow<User>
   //@Query ("Select * from users where " + "id= :id limit 1" )
   // fun getUserById(id:Int):Flow<User>

}