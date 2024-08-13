package com.example.ashborn.dao
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.ashborn.data.Carta
import com.example.ashborn.data.Conto
import com.example.ashborn.data.Operation
import com.example.ashborn.data.Stato
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

   // @Query("SELECT * FROM operations WHERE clientCode = :clientCode AND dateO >= :from AND dateO <= :upTo LIMIT :limit OFFSET :offset")
   //fun getOperations(clientCode: String, from: LocalDateTime, upTo: LocalDateTime, offset: Int, limit: Int): Flow<MutableList<Operation>>


   @Transaction
   suspend fun insertAllOperations(listOperation: List<Operation>) = listOperation.forEach{insertOperation(it)}



   @Insert(onConflict = OnConflictStrategy.IGNORE)
   suspend fun insertCarta(carta:Carta)

   @Query("Select * from carte where codUtente= :codUtente and statoCarta <> 'CANCELLATO'")
   fun getCarte(codUtente:String):Flow<MutableList<Carta>>

   /*cancellare una carta significa mettere il suo stato a cancellato
   * */
   @Query(" update carte set statoCarta=:nuovoStato where nrCarta=:idCarta")
   suspend fun aggiornaStatoCarta(idCarta:Long,nuovoStato:Stato)


   @Query("select * from operations where cardCode=:idCarta AND dateO >= :from AND dateO <= :upTo LIMIT :limit OFFSET :offset")
   // @Query("select * from operations where cardCode=:idCarta  LIMIT :limit OFFSET :offset")
   fun getOperazioniCarta(idCarta:Long,from: LocalDateTime, upTo: LocalDateTime, offset: Int, limit: Int) : Flow<MutableList<Operation>>
   //fun getOperazioniCarte(idCarta:Long,from: LocalDateTime, upTo: LocalDateTime, offset: Int, limit: Int) : Flow<MutableList<Operation>>

   @Query("SELECT * FROM operations WHERE bankAccount = :codConto AND dateO >= :from AND dateO <= :upTo LIMIT :limit OFFSET :offset")
   //@Query("SELECT * FROM operations WHERE bankAccount = :codConto  LIMIT :limit OFFSET :offset")
   fun getOperazioniConto(codConto: String, from: LocalDateTime, upTo: LocalDateTime, offset: Int, limit: Int): Flow<MutableList<Operation>>

   @Insert(onConflict = OnConflictStrategy.IGNORE)
   fun inserisciConto(conto: Conto)

   @Query("select * from conti where codCliente = :codCliente")
   fun getConti(codCliente:String) : Flow<MutableList<Conto>>

   @Query("select * from conti where codConto = :codConto")
   fun getContoByCodConto(codConto: String): Flow<Conto>

   @Query("update conti set saldo = saldo + :amount where codConto = :bankAccount and codCliente = :clientCode")
   suspend fun aggiornaSaldo(
      clientCode: String,
      bankAccount: String,
      amount: Double,
   )
}