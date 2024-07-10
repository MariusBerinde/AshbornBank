package com.example.ashborn.repository

import com.example.ashborn.dao.AshbornDao
import com.example.ashborn.data.Carta
import com.example.ashborn.data.Conto
import com.example.ashborn.data.Operation
import com.example.ashborn.data.Stato
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class OperationRepository(private val ashbornDao: AshbornDao) {

    suspend fun insertOperation(operation: Operation) = ashbornDao.insertOperation(operation)

    suspend fun insertAllOperations(listOperation: List<Operation>) = ashbornDao.insertAllOperations(listOperation)

    suspend fun deleteOperation(operation: Operation) = ashbornDao.deleteOperation(operation)

    //fun getOperations(clientCode: String, from: LocalDateTime, upTo: LocalDateTime, offset: Int, limit: Int) = ashbornDao.getOperations(clientCode, from, upTo, offset, limit)
    //fun getOperations(clientCode: String, from: LocalDateTime, upTo: LocalDateTime, offset: Int, limit: Int) = ashbornDao.getOperations(clientCode, from, upTo, offset, limit)

    fun getOperazioniCarte(idCarta:Long,from: LocalDateTime, upTo: LocalDateTime, offset: Int, limit: Int) : Flow<MutableList<Operation>> = ashbornDao.getOperazioniCarte(idCarta,from, upTo, offset, limit)

    fun getOperazioniConto(codConto: String, from: LocalDateTime, upTo: LocalDateTime, offset: Int, limit: Int): Flow<MutableList<Operation>> = ashbornDao.getOperazioniConto(codConto, from, upTo ,offset, limit)


    // se incrementiamo operazioni su carta @Insert(onConflict = OnConflictStrategy.IGNORE)

}

class CardRepository(private val ashbornDao: AshbornDao){
    suspend fun insertCarta(carta: Carta) = ashbornDao.insertCarta(carta)

    /*cancellare una carta significa mettere il suo stato a cancellato
    * */
    suspend fun aggiornaStatoCarta(idCarta:Long, nuovoStato: Stato) = ashbornDao.aggiornaStatoCarta(idCarta,nuovoStato)

    fun getCarte(codUtente:String) :Flow<MutableList<Carta>> = ashbornDao.getCarte(codUtente)

}

class ContoRepository(private val ashbornDao: AshbornDao){

    fun inserisciConto(conto: Conto) = ashbornDao.inserisciConto(conto)

    fun getConti(codCliente:String) : Flow<MutableList<Conto>>   = ashbornDao.getConti(codCliente)
}