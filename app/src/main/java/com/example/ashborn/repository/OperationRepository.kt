package com.example.ashborn.repository

import com.example.ashborn.dao.AshbornDao
import com.example.ashborn.data.Carta
import com.example.ashborn.data.Conto
import com.example.ashborn.data.Operation
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class OperationRepository(private val ashbornDao: AshbornDao) {
    //suspend fun insertOperation(operation: Operation) = ashbornDao.insertOperation(operation)
    //suspend fun insertAllOperations(listOperation: List<Operation>) = ashbornDao.insertAllOperations(listOperation)
    suspend fun deleteOperation(operation: Operation) = ashbornDao.deleteOperation(operation)
    fun getOperazioniCarta(idCarta:Long,from: LocalDateTime, upTo: LocalDateTime, offset: Int, limit: Int) : Flow<MutableList<Operation>> = ashbornDao.getOperazioniCarta(idCarta,from, upTo, offset, limit)
    fun getOperazioniConto(codConto: String, from: LocalDateTime, upTo: LocalDateTime, offset: Int, limit: Int): Flow<MutableList<Operation>> = ashbornDao.getOperazioniConto(codConto, from, upTo ,offset, limit)
    suspend  fun executeInstantTransaction(operation: Operation) = ashbornDao.executeInstantTransaction(operation)
    suspend fun executeTransaction(operation: Operation)  = ashbornDao.executeTransaction(operation)
    suspend fun completePendingOperations() = ashbornDao.completePendingOperations()
    suspend fun executePaymentTransaction(operation: Operation) = ashbornDao.executePaymentTransaction(operation)
}

class CardRepository(private val ashbornDao: AshbornDao){
    //suspend fun insertCarta(carta: Carta) = ashbornDao.insertCarta(carta)
    /* cancellare una carta significa mettere il suo stato a cancellato*/
    //suspend fun aggiornaStatoCarta(idCarta:Long, nuovoStato: Stato) = ashbornDao.aggiornaStatoCarta(idCarta,nuovoStato)
    fun getCarte(codUtente:String) :Flow<MutableList<Carta>> = ashbornDao.getCarte(codUtente)
    //fun bloccaCarta(clientCode: String, cardCode: Long) = CoroutineScope(Dispatchers.IO).launch{ashbornDao.aggiornaStatoCarta(cardCode.toLong(), Stato.BLOCCATO)}
}

class ContoRepository(private val ashbornDao: AshbornDao){
    //fun inserisciConto(conto: Conto) = ashbornDao.inserisciConto(conto)
    fun getConti(codCliente:String) : Flow<MutableList<Conto>>   = ashbornDao.getConti(codCliente)
    //fun getContoByCodConto(codConto: String): Flow<Conto> = ashbornDao.getContoByCodConto(codConto)
    //fun aggiornaSaldo(clientCode: String, bankAccount: String, amount: Double) = CoroutineScope(Dispatchers.IO).launch{ashbornDao.aggiornaSaldo(clientCode, bankAccount, amount)}
}