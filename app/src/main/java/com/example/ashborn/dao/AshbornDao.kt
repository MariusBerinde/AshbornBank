package com.example.ashborn.dao
import androidx.compose.ui.text.style.LineBreak
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.ashborn.data.Operation
import com.example.ashborn.data.User
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

/**
 * serve a  prendere dati i dati dell'utente dal db
 */
@Dao
interface AshbornDao {

   @Upsert
   suspend fun upsertUser(utente:User)

   @Delete
   suspend fun deleteUser(utente: User)

   @Query ("Select * from users where clientCode= :clientCode " )
   fun getUserByClientCode(clientCode:String): Flow<User>
  //  fun getUserByClientCode(clientCode:String): LiveData<User?>

   @Query ("SELECT EXISTS(SELECT * from users where clientCode = :aCodCliente AND pin = :aPin ) ")
   fun isPinCorrect(aCodCliente:String,aPin:String):Flow<Boolean>

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertOperation(operation: Operation)

   @Delete
   suspend fun deleteOperation(operation: Operation)

   @Query("SELECT * FROM operations WHERE clientCode = :clientCode AND dateO >= :from AND dateO <= :upTo LIMIT :limit OFFSET :offset")
   fun getOperations(clientCode: String, from: LocalDateTime, upTo: LocalDateTime, offset: Int, limit: Int): Flow<MutableList<Operation>>


   @Transaction
   suspend fun insertAllOperations(listOperation: List<Operation>) = listOperation.forEach{insertOperation(it)}
}