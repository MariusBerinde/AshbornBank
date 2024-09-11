package com.example.ashborn.dao
import android.util.Log
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.ashborn.data.Avviso
import com.example.ashborn.data.Carta
import com.example.ashborn.data.Conto
import com.example.ashborn.data.Operation
import com.example.ashborn.data.OperationStatus
import com.example.ashborn.data.Stato
import com.example.ashborn.data.StatoAvviso
import com.example.ashborn.data.TransactionType
import com.example.ashborn.data.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * @brief serve a  interagire con ashborndb
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
   suspend fun deleteOperationB(operation: Operation)

   @Transaction
   suspend fun deleteOperation(operation: Operation) {
      aggiornaSaldo(clientCode = operation.clientCode, operation.bankAccount, operation.amount)
      updateOperationStatus(operation.id, OperationStatus.CANCELED)
   }

   @Query("UPDATE operations SET operationStatus = :status WHERE id = :operationId")
   fun updateOperationStatus(operationId: Long, status: OperationStatus)

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

   @Query("select * from operations where cardCode=:idCarta AND dateO >= :from AND dateO <= :upTo AND operationStatus <> 'CANCELED' LIMIT :limit OFFSET :offset")
   fun getOperazioniCarta(idCarta:Long,from: LocalDateTime, upTo: LocalDateTime, offset: Int, limit: Int) : Flow<MutableList<Operation>>

   @Query("SELECT * FROM operations WHERE bankAccount = :codConto AND dateO >= :from AND dateO <= :upTo AND operationStatus <> 'CANCELED' ORDER BY dateO DESC LIMIT :limit OFFSET :offset")
  // @Query("SELECT * FROM operations WHERE bankAccount = :codConto AND  operationStatus <> 'CANCELED' LIMIT :limit OFFSET :offset")
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

   @Query("select * from avvisi where destinatario= :codCliente")
   fun getAvvisi(codCliente: String): Flow<MutableList<Avviso>>

   @Query("update avvisi set stato = :nuovoStato where id = :id")
   fun aggiornaStatoAvviso(id: Long, nuovoStato: StatoAvviso)

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   fun aggiungiAvviso(avviso:Avviso)

   @Transaction
   suspend fun executeTransaction(operation: Operation) {
      val nameFun = object {}.javaClass.enclosingMethod?.name
      Log.d(nameFun, "Enter")
      aggiornaSaldo(operation.clientCode,operation.bankAccount,-operation.amount)
      Log.d(nameFun, "Amount Updated")
      insertOperation(operation)
      Log.d(nameFun, "Exit")
   }

   @Transaction
   suspend fun executeInstantTransaction(operation: Operation) {

      val nameFun = object {}.javaClass.enclosingMethod?.name
      Log.d(nameFun, "Enter")
      aggiornaSaldo(operation.clientCode, operation.bankAccount, -operation.amount)
      insertOperation(operation)
      if(isAshbornIban(operation.iban)){
         Log.d(nameFun,"passato is valid iban")
         aggiornaSaldoIban(operation.iban,operation.amount)
         val conto = getContoByIban(operation.iban)
         val codClienteDest = conto.codCliente
         val codContoDest = conto.codConto
         val newOp = Operation(
            clientCode = codClienteDest,
            dateO = operation.dateO,
            dateV = operation.dateV,
            description = operation.description,
            recipient = operation.recipient,
            amount = operation.amount,
            transactionType = TransactionType.DEPOSIT,
            operationType = operation.operationType,
            bankAccount = codContoDest,
            iban = operation.iban,
            cardCode = null,
            operationStatus = OperationStatus.DONE
         )
         Log.d(nameFun,"dettagli operazione creata $newOp")
         insertOperation(newOp)
         aggiornaSaldo(codClienteDest, codContoDest, newOp.amount)
      }
      Log.d(nameFun, "Exit")

   }

   @Transaction
   suspend fun executePaymentTransaction(operation: Operation) {

      val nameFun = object {}.javaClass.enclosingMethod?.name
      Log.d(nameFun, "Enter")
      if(operation.cardCode != null && isAshbornCard(operation.cardCode.toLong())){
         Log.d(nameFun,"is valid payment")
         val carta:Carta = getCardByCardNumber(operation.cardCode)
         insertOperation(operation)
         aggiornaSaldoConto(carta.codConto, operation.amount)
         aggiornaSaldoCarta(operation.cardCode, operation.amount)
      }
      Log.d(nameFun, "Exit")

   }

   @Query("UPDATE conti SET saldo = saldo + :amount WHERE codConto = :codConto")
   fun aggiornaSaldoConto(codConto: String, amount: Double)

   @Query("SELECT * FROM carte WHERE nrCarta = :cardCode")
   fun getCardByCardNumber(cardCode: Long): Carta

   @Query("UPDATE carte SET saldo = saldo + :amount WHERE nrCarta = :cardCode")
   fun aggiornaSaldoCarta(cardCode: Long, amount: Double)

   @Query("select exists (select nrCarta from carte where nrCarta = :cardCode)")
   fun isAshbornCard(cardCode: Long): Boolean

   @Query("select * from conti where iban = :iban")
   fun getContoByIban(iban: String): Conto


   @Query("update conti set saldo = saldo + :amount where iban = :iban")
   fun aggiornaSaldoIban(iban: String, amount: Double)



   @Query("select exists (select iban from conti where iban= :iban)")
   fun isAshbornIban(iban: String): Boolean

   @Transaction
   suspend fun completePendingOperations() {
      val nameFun = object{}.javaClass.enclosingMethod?.name
      val todayDate = LocalDate.now()
      val todayDateTime = LocalDateTime.of(todayDate.year, todayDate.month, todayDate.dayOfMonth, 17,0,0)
      val operations = getAllScheduledPendingOperations(todayDateTime)
      for (op in operations.first()) {
         updateOperationStatus(op.id, OperationStatus.DONE)
         Log.d(nameFun, "operazione mandante fatta: $op")
         if(isAshbornIban(op.iban)) {
            val conto = getContoByIban(op.iban)
            val codClienteDest = conto.codCliente
            val codContoDest = conto.codConto
            val newOp = Operation(
               clientCode = codClienteDest,
               dateO = op.dateO,
               dateV = op.dateV,
               description = op.description,
               recipient = op.recipient,
               amount = op.amount - 1.0,
               transactionType = TransactionType.DEPOSIT,
               operationType = op.operationType,
               bankAccount = codContoDest,
               iban = op.iban,
               cardCode = op.cardCode,
               operationStatus = OperationStatus.DONE
            )
            insertOperation(newOp)
            aggiornaSaldo(codClienteDest, codContoDest, newOp.amount)
            Log.d(nameFun, "operazione ricevente fatta: $newOp")

         }
      }
   }

   @Query("SELECT * FROM operations WHERE operationStatus = 'PENDING' AND dateO <= :today")
   fun getAllScheduledPendingOperations(today: LocalDateTime): Flow<MutableList<Operation>>


}